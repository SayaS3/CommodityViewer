import mysql.connector
import pandas as pd
from statsmodels.tsa.stattools import adfuller
from statsmodels.tsa.holtwinters import ExponentialSmoothing
from sqlalchemy import create_engine
from sqlalchemy import text

engine = create_engine('mysql+mysqlconnector://root:bazahaslo@localhost/commodity')

# Pobranie danych z tabeli commodities
query_commodity_data = "SELECT * FROM commodities"
df_commodity_data = pd.read_sql(query_commodity_data, engine)

# Pobranie danych z tabeli data_points
query_data_point = "SELECT * FROM data_points"
df_data_point = pd.read_sql(query_data_point, engine)

# Usunięcie wierszy z wartością "." w kolumnie value
df_data_point = df_data_point[df_data_point['value'] != "."]

# Konwersja kolumny value na typ float
df_data_point['value'] = df_data_point['value'].astype(float)

# Sortowanie danych według daty dla każdego surowca
df_data_point = df_data_point.sort_values(by=['commodity_id', 'timestamp'])

df_data_point['timestamp'] = pd.to_datetime(df_data_point['timestamp'])
df_data_point.set_index('timestamp', inplace=True)


def delete_previous_adf_results(commodity_id):
    engine = create_engine('mysql+mysqlconnector://root:bazahaslo@localhost/commodity')
    with engine.connect() as connection:
        trans = connection.begin()  # Rozpocznij transakcję
        try:
            connection.execute(text("DELETE FROM adf_results WHERE commodity_id = :commodity_id"), {'commodity_id': commodity_id})
            trans.commit()  # Zatwierdź transakcję

        except Exception as e:
            trans.rollback()  # Wycofaj transakcję w przypadku błędu


def connect_to_database():
    try:
        engine = create_engine('mysql+mysqlconnector://root:bazahaslo@localhost/commodity')
        return engine
    except Exception as e:
        print(f"Error connecting to the database: {e}")
        return None


def adf_test(series, commodity_id):
    try:
        result = adfuller(series)
        is_stationary = result[1] <= 0.05

        engine = connect_to_database()

        if engine:
            data = {
                'commodity_id': commodity_id,
                'adf_statistic': result[0],
                'p_value': result[1],
                'critical_value_1_percent': result[4]['1%'],
                'critical_value_5_percent': result[4]['5%'],
                'critical_value_10_percent': result[4]['10%'],
                'is_stationary': is_stationary
            }
            df = pd.DataFrame([data])
            df.to_sql('adf_results', con=engine, if_exists='append', index=False)
            print("ADF test results saved to the database.")
        else:
            print("Unable to connect to the database. ADF test results not saved.")

    except Exception as e:
        print(f"Error in ADF test: {e}")


def delete_previous_forecasts(commodity_id):
    engine = create_engine('mysql+mysqlconnector://root:bazahaslo@localhost/commodity')
    with engine.connect() as connection:
        trans = connection.begin()  # Rozpocznij transakcję
        try:
            result = connection.execute(text("DELETE FROM forecasts WHERE commodity_id = :commodity_id"),
                                        {"commodity_id": commodity_id})
            trans.commit()  # Zatwierdź transakcję
        except Exception as e:
            trans.rollback()  # Wycofaj transakcję w przypadku błędu


def save_forecast_to_db(commodity_id, forecast):
    engine = create_engine('mysql+mysqlconnector://root:bazahaslo@localhost/commodity')
    with engine.connect() as connection:
        trans = connection.begin()  # Rozpocznij transakcję
        try:
            for date, value in forecast.items():
                connection.execute(text(
                    "INSERT INTO forecasts (commodity_id, forecast_date, forecast_value) VALUES (:commodity_id, :forecast_date, :forecast_value)"),
                    {"commodity_id": commodity_id, "forecast_date": date, "forecast_value": value})
            trans.commit()  # Zatwierdź transakcję
        except Exception as e:
            trans.rollback()  # Wycofaj transakcję w przypadku błędu


def holt_winters_forecast(series):
    try:
        if not isinstance(series, pd.Series) or series.empty:
            raise ValueError("Input series must be a non-empty pandas Series.")

        model = ExponentialSmoothing(series, trend='add', seasonal='add', seasonal_periods=12, freq=None)
        model_fit = model.fit()
        forecast = model_fit.forecast(steps=12)

        # Pobierz ostatnią dostępną datę
        last_date = series.index[-1]

        date_range = pd.date_range(start=last_date + pd.offsets.Day(1), periods=12, freq='D')

        # Ustaw zakres dat jako indeks dla prognozy
        forecast.index = date_range

        return forecast

    except Exception as e:
        print(f"An error occurred: {e}")
        return None


# Wykonaj test ADF i prognozowanie dla surowców
for commodity_id in df_commodity_data['id']:
    series = df_data_point[df_data_point['commodity_id'] == commodity_id]['value']

    # Usuń poprzednie wyniki dla tego surowca
    delete_previous_adf_results(commodity_id)
    adf_test(series, commodity_id)

    # Usuń stare prognozy
    delete_previous_forecasts(commodity_id)
    forecast = holt_winters_forecast(series)
    # Zapisz nowe prognozy
    save_forecast_to_db(commodity_id, forecast)

# Zamknięcie połączenia z bazą danych
engine.dispose()
