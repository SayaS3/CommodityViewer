# Aplikacja Prognozująca Surowce

## Wprowadzenie

Aplikacja Prognozująca Surowce to narzędzie stworzone w języku Java przy użyciu frameworka Spring. Celem aplikacji jest prognozowanie wartości surowców na podstawie historycznych danych, przy wykorzystaniu testów statystycznych oraz modeli prognozowania.


## Konfiguracja

1. **Baza danych:**
    - Utwórz schemat bazy danych, korzystając z pliku `schema.sql` znajdującego się w folderze `resources/sql`.
    - W pliku `application.yml` ustaw odpowiednie dane logowania do bazy danych: `url`, `username` i `password`.

2. **Python:**
    - W folderze `resources/python` znajduje się skrypt `main.py`. Upewnij się, że masz zainstalowany Python w wersji 3.6 lub nowszej.
    - Ustaw w skrypcie swoj login i haslo do bazy danych MySQL:
        ```python
        engine = create_engine('mysql+mysqlconnector://root:bazahaslo@localhost/commodity')
        ```
    - Zainstaluj wymagane biblioteki Python, wykonując polecenie: `pip install -r requirements.txt` znajdującym się w folderze `resources/python` w projekcie.

## Konfiguracja za pomoca Dockera

    - Uruchom aplikację, bazę danych oraz Pythona za pomocą Dockera, wykonując polecenie:
        ```bash
        docker-compose up
        ```

## Opis funkcji

- **Test ADF (Augmented Dickey-Fuller):**
    - Sprawdza stacjonarność szeregów czasowych dla każdego surowca.
    - Wyniki testów zapisywane są w bazie danych w tabeli `adf_results`.

- **Prognozowanie za pomocą modelu Holt-Winters:**
    - Model ten uwzględnia składowe autoregresywne, sezonowe oraz trendu.
    - Prognozy zapisywane są w bazie danych w tabeli `holtwinters`.

## Uruchomienie

1. Uruchom aplikację Java, korzystając z dostępnego narzędzia budującego (Maven).

## Autor

Lukasz Koziol

---
