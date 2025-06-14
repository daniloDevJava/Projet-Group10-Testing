# Étape 1 : Construire l'application
FROM maven:3.9.4-eclipse-temurin-21 AS builder

LABEL authors="danilo"

WORKDIR /vehicule

COPY pom.xml .


#RUN mvn dependency:go-offline -B

# Copier le code source de l'application
COPY src ./src

# Compiler et créer le fichier .jar
RUN mvn clean package

# Étape 2 : Construire une image légère pour exécuter l'application
FROM openjdk:21-jdk-slim

WORKDIR /vehicule

COPY --from=builder /vehicule/target/vehicule-0.0.1-SNAPSHOT.jar vehicule.jar

# Étape 4 : Définir les variables d'environnement pour la connexion à PostgreSQL
ENV DB_HOST=postgres-container
ENV DB_PORT=5432
ENV DB_NAME=vehicule
ENV DB_USER=group10
ENV DB_PASSWORD=10-sftw-testing-java

EXPOSE 9000


# Commande pour exécuter l'application
CMD ["java", "-jar", "vehicule.jar"]
