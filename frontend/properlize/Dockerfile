# Étape 1 : Construction
FROM node:22 AS build

# Définir le répertoire de travail dans le conteneur
WORKDIR /app

# Copier le fichier package.json et package-lock.json
COPY package*.json ./

# Installer les dépendances
RUN npm install

# Copier le reste des fichiers du projet
COPY . .

# Construire l'application React
RUN npm run build

# Étape 2 : Servir avec nginx
FROM nginx:1.23.3-alpine

# Copier les fichiers de build de l'étape précédente vers le répertoire nginx
COPY --from=build /app/dist /usr/share/nginx/html

# Copier un fichier de configuration nginx personnalisé, si nécessaire
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Exposer le port 80
EXPOSE 80

# Commande par défaut pour démarrer nginx
CMD ["nginx", "-g", "daemon off;"]

