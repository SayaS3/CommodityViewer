# Aplikacja Prognozująca Surowce

## Wprowadzenie

Aplikacja Prognozująca Surowce to narzędzie stworzone w języku Java przy użyciu frameworka Spring. Celem aplikacji jest prognozowanie wartości surowców na podstawie historycznych danych, przy wykorzystaniu testów statystycznych oraz modeli prognozowania.

## Opis funkcji

- **Test ADF (Augmented Dickey-Fuller):**
    - Test ADF jest wykorzystywany do sprawdzenia stacjonarności szeregów czasowych dla każdego surowca.
    - Wyniki testów zapisywane są w bazie danych w tabeli `adf_results`.

- **Prognozowanie za pomocą modelu Holt-Winters:**
    - Model ten uwzględnia składowe autoregresywne, sezonowe oraz trendu.
    - Prognozy zapisywane są w bazie danych w tabeli `holtwinters`.

  Model Holt-Winters jest zaawansowanym narzędziem analizy szeregów czasowych, uwzględniającym trzy główne składowe:
    - **Składowa autoregresyjna (AR):** Model uwzględnia wpływ poprzednich wartości szeregów czasowych na bieżącą wartość prognozy.
    - **Składowa sezonowa (S):** Model uwzględnia cykliczne wzorce w danych, co pozwala przewidywać sezonowe zmiany.
    - **Składowa trendu (T):** Model uwzględnia ogólny kierunek, w jakim podążają dane w czasie.

- **Prognozowanie za pomocą modelu ARIMA:**
    - Model ten uwzględnia składowe autoregresywne, sezonowe oraz trendu.
    - Prognozy zapisywane są w bazie danych w tabeli `arima`.

  Model ARIMA (AutoRegressive Integrated Moving Average) to zaawansowany model analizy szeregów czasowych, który skupia się na trzech głównych składowych:
    - **Autoregresja (AR):** Model uwzględnia wpływ poprzednich wartości szeregów czasowych na bieżącą wartość prognozy.
    - **Różnicowanie (I):** Proces różnicowania pomaga w dostosowaniu danych do stacjonarności, co poprawia jakość prognozy.
    - **Ruchoma średnia (MA):** Model bierze pod uwagę bieżące wartości błędów prognoz, co wpływa na dokładność prognozy.

## Uruchomienie
Uruchom aplikację, bazę danych oraz Pythona za pomocą Dockera, wykonując polecenie:
```bash
docker-compose up
```

## Autor

Lukasz Koziol

---
