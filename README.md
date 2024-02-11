# Aplikacja Prognozująca Ceny Surowców

## Wprowadzenie

Aplikacja Prognozująca Surowce to narzędzie stworzone w języku Java przy użyciu frameworka Spring. Celem aplikacji jest
prognozowanie wartości surowców na podstawie historycznych danych, przy wykorzystaniu testów statystycznych oraz modeli
prognozowania.

## Opis funkcji

- **Test ADF (Augmented Dickey-Fuller):**
    - Test ADF jest wykorzystywany do sprawdzenia stacjonarności szeregów czasowych dla każdego surowca.
    - Wyniki testów zapisywane są w bazie danych w tabeli `adf_results`.

- **Prognozowanie za pomocą modelu Holt-Winters:**
    - Model ten uwzględnia składowe autoregresywne, sezonowe oraz trendu.
    - Prognozy zapisywane są w bazie danych w tabeli `holtwinters`.

  Model Holt-Winters jest zaawansowanym narzędziem analizy szeregów czasowych, uwzględniającym trzy główne składowe:
    - **Składowa autoregresyjna (AR):** Model uwzględnia wpływ poprzednich wartości szeregów czasowych na bieżącą
      wartość prognozy.
    - **Składowa sezonowa (S):** Model uwzględnia cykliczne wzorce w danych, co pozwala przewidywać sezonowe zmiany.
    - **Składowa trendu (T):** Model uwzględnia ogólny kierunek, w jakim podążają dane w czasie.

- **Prognozowanie za pomocą modelu ARIMA:**
    - Model ten uwzględnia składowe autoregresywne, sezonowe oraz trendu.
    - Prognozy zapisywane są w bazie danych w tabeli `arima`.

  Model ARIMA (AutoRegressive Integrated Moving Average) to zaawansowany model analizy szeregów czasowych, który skupia
  się na trzech głównych składowych:
    - **Autoregresja (AR):** Model uwzględnia wpływ poprzednich wartości szeregów czasowych na bieżącą wartość prognozy.
    - **Różnicowanie (I):** Proces różnicowania pomaga w dostosowaniu danych do stacjonarności, co poprawia jakość
      prognozy.
    - **Ruchoma średnia (MA):** Model bierze pod uwagę bieżące wartości błędów prognoz, co wpływa na dokładność
      prognozy.

## Uruchomienie

Aby uruchomić aplikację wraz z bazą danych i środowiskiem Pythona, wykonaj poniższe kroki:

1. **Pobierz Docker:**
    - Upewnij się, że masz zainstalowany Docker na swoim systemie operacyjnym.
    - Możesz pobrać Docker ze strony [https://www.docker.com/get-started](https://www.docker.com/get-started).

2. **Uruchomienie za pomocą Dockera:**
    - Otwórz terminal w folderze projektu.
    - Wykonaj polecenie:
        ```bash
        docker-compose up
        ```

3. **Sprawdź działanie:**
    - Po zakończeniu procesu uruchamiania, aplikacja, baza danych i Python powinny być dostępne.
    - Otwórz przeglądarkę i odwiedź [http://localhost:8080](http://localhost:8080), aby skorzystać z aplikacji.

**Uwaga:** Upewnij się, że nie masz już zajętego portu 8080 na swoim systemie, aby uniknąć konfliktów.

## Wymagane narzędzia

Aby skorzystać z Docker Compose, musisz mieć zainstalowane:

- Docker
- Docker Compose

Możesz pobrać te narzędzia ze strony [https://www.docker.com/get-started](https://www.docker.com/get-started).

## Screenshots
![Przechwytywanie1](https://github.com/SayaS3/CommodityViewer/assets/122474783/b4c375f5-a0a1-4e4a-9da3-43844dc6781c)
![Przechwytywanie2](https://github.com/SayaS3/CommodityViewer/assets/122474783/744917a2-62e6-46b9-adfc-e97610e7ea2a)
![image](https://github.com/SayaS3/CommodityViewer/assets/122474783/95c1337f-df8e-4402-a84c-3d1716e22cd1)


## Autor

Łukasz Kozioł

---

# gfd
