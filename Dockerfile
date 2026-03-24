# ЭТАП 1: Сборка
# образ, где уже есть Maven и JDK 21
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Сначала копируется только pom.xml, чтобы Docker закешировал зависимости
COPY pom.xml .
RUN mvn dependency:go-offline

# Теперь копируются исходники и собирается проект
COPY src ./src
RUN mvn clean package -DskipTests

# ЭТАП 2: Запуск
FROM bellsoft/liberica-openjdk-alpine:21
WORKDIR /app

# копируется готовый jar из первого этапа
COPY --from=build /app/target/*.jar app.jar

#  флаги для корректной работы Java в контейнере
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]