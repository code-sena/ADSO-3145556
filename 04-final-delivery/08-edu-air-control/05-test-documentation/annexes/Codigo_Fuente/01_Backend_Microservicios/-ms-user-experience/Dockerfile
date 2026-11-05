FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

RUN sed -i 's/\r$//' mvnw && bash mvnw dependency:go-offline

COPY src src

RUN sed -i 's/\r$//' mvnw && bash mvnw clean package -DskipTests

FROM eclipse-temurin:17-jre

RUN apt-get update \
    && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 3006

ENTRYPOINT ["java", "-jar", "app.jar"]
