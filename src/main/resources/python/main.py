import mysql.connector
import pandas as pd
from statsmodels.tsa.stattools import adfuller
from statsmodels.tsa.holtwinters import ExponentialSmoothing
from sqlalchemy import create_engine
from sqlalchemy import text

# Połączenie z bazą danych
conn = mysql.connector.connect(
    host="localhost",
    user="root",
    password="bazahaslo",
    database="commodity"
)

# Pobranie danych z tabeli commodity_data
query_commodity_data = "SELECT * FROM commodity_data"
df_commodity_data = pd.read_sql(query_commodity_data, conn)

# Pobranie danych z tabeli data_point
query_data_point = "SELECT * FROM data_point"
df_data_point = pd.read_sql(query_data_point, conn)

# Usunięcie wierszy z wartością "." w kolumnie value_column
df_data_point = df_data_point[df_data_point['value_column'] != "."]

# Konwersja kolumny value_column na typ float
df_data_point['value_column'] = df_data_point['value_column'].astype(float)

# Sortowanie danych według daty dla każdego surowca
df_data_point = df_data_point.sort_values(by=['commodity_id', 'date'])

df_data_point['date'] = pd.to_datetime(df_data_point['date'])
df_data_point.set_index('date', inplace=True)


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
            result = connection.execute(text("DELETE FROM forecasts WHERE commodity_id = :commodity_id"), {"commodity_id": commodity_id})
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

        model = ExponentialSmoothing(series, trend='add', seasonal='add', seasonal_periods=12, freq='MS')
        model_fit = model.fit()
        forecast = model_fit.forecast(steps=12)

        # Pobierz ostatnią dostępną datę
        last_date = series.index[-1]

        # Sprawdź różnicę między dwiema ostatnimi datami
        date_diff = series.index[-1] - series.index[-2]

        # Utwórz zakres dat na kolejne 12 dni/miesięcy
        if date_diff.days < 28:  # Jeśli dane są dzienne
            date_range = pd.date_range(start=last_date + pd.Timedelta(days=1), periods=12, freq='D')
        else:  # Jeśli dane są miesięczne
            date_range = pd.date_range(start=last_date + pd.offsets.MonthBegin(1), periods=12, freq='MS')

        # Ustaw zakres dat jako indeks dla prognozy
        forecast.index = date_range

        return forecast

    except Exception as e:
        print(f"An error occurred: {e}")
        return None


# Wykonaj test ADF i prognozowanie dla surowców z danymi miesięcznymi
for commodity_id in df_commodity_data[df_commodity_data['interval_column'] == 'monthly']['id']:

    series = df_data_point[df_data_point['commodity_id'] == commodity_id]['value_column']

    # Usuń poprzednie wyniki dla tego surowca
    delete_previous_adf_results(commodity_id)
    adf_test(series, commodity_id)

    # Usuń stare prognozy
    delete_previous_forecasts(commodity_id)
    forecast = holt_winters_forecast(series)
    # Zapisz nowe prognozy
    save_forecast_to_db(commodity_id, forecast)

# Wykonaj test ADF i prognozowanie dla surowców z danymi dziennymi
for commodity_id in df_commodity_data[df_commodity_data['interval_column'] == 'daily']['id']:

    series = df_data_point[df_data_point['commodity_id'] == commodity_id]['value_column']
    # Usuń poprzednie wyniki dla tego surowca
    delete_previous_adf_results(commodity_id)
    adf_test(series, commodity_id)

    # Usuń stare prognozy
    delete_previous_forecasts(commodity_id)

    forecast = holt_winters_forecast(series)
    # Zapisz nowe prognozy
    save_forecast_to_db(commodity_id, forecast)

# Zamknięcie połączenia z bazą danych
conn.close()
