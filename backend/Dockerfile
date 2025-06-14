# Utilise une image OpenJDK 17 légère comme base
FROM openjdk:17-jdk-slim

# Définit le répertoire de travail dans le conteneur
WORKDIR /app

# Copie le fichier JAR de votre application dans le conteneur
# Assurez-vous que le nom du fichier JAR correspond exactement au vôtre
# Ex: COPY vehicule-0.0.1-SNAPSHOT.jar app.jar
COPY vehicule-0.0.1-SNAPSHOT.jar app.jar

# Expose le port sur lequel l'application Spring Boot écoute
EXPOSE 9000

# Commande par défaut pour exécuter l'application
# Exécute le fichier JAR copié
CMD ["java", "-jar", "app.jar"]

