# Utiliser une image de base Java
FROM openjdk:17-jdk-slim

# Définir le répertoire de travail
WORKDIR /app

# Argument pour le nom du fichier JAR
ARG JAR_FILE=build/libs/*.jar

# Copier le fichier JAR de l'application dans le conteneur
COPY ${JAR_FILE} app.jar

# Exposer le port sur lequel l'application écoute
EXPOSE 8080

# Commande pour exécuter l'application
ENTRYPOINT ["java", "-jar", "app.jar"]