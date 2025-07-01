# Étape 1 : Build avec Maven
FROM maven:3.9.4-eclipse-temurin-21 AS builder

LABEL authors="danilo"

WORKDIR /vehicule

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

# Étape 2 : Image finale avec JDK minimal
FROM openjdk:21-jdk-slim

WORKDIR /vehicule

# Copier le jar compilé depuis l'étape builder
COPY --from=builder /vehicule/target/vehicule-0.0.1-SNAPSHOT.jar vehicule.jar

# --- Variables d'environnement injectables via .env (via docker-compose)
ARG DB_HOST
ARG DB_PORT
ARG DB_NAME
ARG DB_USER
ARG DB_PASSWORD

ENV DB_HOST=${DB_HOST}
ENV DB_PORT=${DB_PORT}
ENV DB_NAME=${DB_NAME}
ENV DB_USER=${DB_USER}
ENV DB_PASSWORD=${DB_PASSWORD}

EXPOSE 9000

CMD ["java", "-jar", "vehicule.jar"]

