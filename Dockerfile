# Первый этап: сборка приложения
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app

# Копируем файлы для сборки
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

# Собираем приложение
RUN ./mvnw clean package -DskipTests

# Второй этап: создание минимального образа для запуска
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Копируем собранный JAR из первого этапа
COPY --from=builder /app/target/*.jar app.jar

# Настройки JVM для оптимальной работы в контейнере
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/./urandom"

# Запускаем приложение
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]