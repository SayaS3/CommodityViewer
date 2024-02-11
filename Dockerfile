# Użyj obrazu Debian z OpenJDK 20
FROM openjdk:23-jdk


# Instaluj potrzebne pakiety
RUN microdnf install -y python3 python3-pip python3-devel gcc gcc-c++
# Dodaj alias dla "python" na "python3"
RUN ln -s /usr/bin/python3 /usr/bin/python
# Instaluj bibliotekę obsługującą MySQL
RUN pip3 install --no-cache-dir pymysql

# Dodaj kilka przykładowych bibliotek Pythona
RUN pip3 install --no-cache-dir statsmodels pandas sqlalchemy
RUN pip3 install --no-cache-dir --upgrade sqlalchemy

# Skopiuj pliki z projektu do kontenera
COPY CommodityViewer-0.0.1-SNAPSHOT.jar /app.jar
COPY src/main/resources/python/main.py /src/main/resources/python/main.py


# Uruchom aplikację Java
CMD ["java", "-jar", "/app.jar"]
