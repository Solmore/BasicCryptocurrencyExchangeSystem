FROM gradle:8.2-jdk17 AS build
WORKDIR /app
COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts settings.gradle.kts ./
RUN ./gradlew dependencies
COPY src src
RUN ./gradlew build -x test

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/build/libs/BasicCryptocurrencyExchangeSystem-0.0.1-SNAPSHOT.jar /app/BasicCryptocurrencyExchangeSystem-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/BasicCryptocurrencyExchangeSystem-0.0.1-SNAPSHOT.jar"]