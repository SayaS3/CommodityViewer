# Aplikacja Prognozująca Surowce

## Wprowadzenie

Aplikacja Prognozująca Surowce to narzędzie stworzone w języku Java przy użyciu frameworka Spring. Celem aplikacji jest
prognozowanie wartości surowców na podstawie historycznych danych, przy wykorzystaniu testów statystycznych oraz modeli
prognozowania.

## Wymagania

- Java 17 lub nowsza
- Python 3.6 lub nowszy
- Baza danych MySQL na porcie 3306

## Konfiguracja

1. **Baza danych:**
    - Utwórz schemat bazy danych, korzystając z pliku `schema.sql` znajdującego się w folderze `resources/sql`.
    - W pliku `application.yml` ustaw odpowiednie dane logowania do bazy danych: `url`, `username` i `password`.

2. **Python:**
    - W folderze `resources/python` znajduje się skrypt `main.py`. Upewnij się, że masz zainstalowany Python w wersji
      3.6 lub nowszej.
    - ustaw w skrypcie swoj login i haslo do bazy danych mysql:
    - engine = create_engine('mysql+mysqlconnector://root:bazahaslo@localhost/commodity')
    - gdzie "root" to login a "bazahaslo" to haslo do bazy, "commodity" to nazwa bazy danych
    - Zainstaluj wymagane biblioteki Python, wykonując polecenie: `pip install -r requirements.txt` znajdujacym sie w folderze resources/python w projekcie.

## Uruchomienie

1. Uruchom aplikację Java, korzystając z dostępnego narzędzia budującego (Maven).

2. Uruchom skrypt Pythona `main.py`, aby przeprowadzić testy statystyczne i prognozowanie.

## Opis funkcji

- **Test ADF (Augmented Dickey-Fuller):**
    - Sprawdza stacjonarność szeregów czasowych dla każdego surowca.
    - Wyniki testów zapisywane są w bazie danych w tabeli `adf_results`.

- **Prognozowanie za pomocą modelu Holt-Winters:**
    - Model ten uwzględnia składowe autoregresywne, sezonowe oraz trendu.
    - Prognozy zapisywane są w bazie danych w tabeli `holtwinters`.

## Uwagi

- Aplikacja zakłada, że dane wejściowe są dostępne w bazie danych MySQL.
- Upewnij się, że wszystkie wymagane zależności (zarówno Java, jak i Python) zostały zainstalowane przed uruchomieniem
  aplikacji.

## Autor

Lukasz Koziol


