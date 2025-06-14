# frontend/Dockerfile
# Dockerfile pour votre application React (Vite) en mode développement.

# Utilise l'image officielle Node.js (version 20, Alpine pour une taille réduite).
FROM node:20-alpine

# Définit le répertoire de travail à l'intérieur du conteneur.
WORKDIR /app

# Copie les fichiers de configuration des dépendances pour profiter du cache Docker.
# Cela permet de réutiliser les couches de dépendances si package.json/yarn.lock n'ont pas changé.
COPY package.json yarn.lock* package-lock.json* ./

# Installe toutes les dépendances du projet.
RUN npm install

# Copie le reste du code source de l'application dans le conteneur.
COPY . .

# Expose le port par défaut du serveur de développement Vite.
EXPOSE 5173

# Définit la commande par défaut pour démarrer le serveur de développement Vite.
# "--host 0.0.0.0" est essentiel pour que le serveur soit accessible depuis l'extérieur du conteneur
# (y compris depuis votre machine hôte via le port mapping).
CMD ["npm", "run", "dev", "--", "--host", "0.0.0.0"]
