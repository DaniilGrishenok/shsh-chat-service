
FROM openjdk:17-jdk-alpine

# Устанавливаем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем jar-файл сервиса в контейнер
COPY target/chat-service-0.0.1-SNAPSHOT.jar app.jar

# Указываем команду для запуска сервиса
ENTRYPOINT ["java","-jar","/app/app.jar"]
