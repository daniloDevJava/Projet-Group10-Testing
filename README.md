# Properlize – Application de Gestion de Véhicules et Utilisateurs

Properlize est une application web complète destinée à la gestion centralisée de véhicules et d’utilisateurs, conçue dans le cadre d’un projet académique. Elle met en œuvre une architecture moderne, une interface intuitive, une API sécurisée, ainsi qu'une stratégie de tests rigoureuse.

---

## 🎯 Objectifs du projet

- Gérer un parc de véhicules (création, modification, suppression, recherche)
- Gérer les comptes utilisateurs (ajout, modification, authentification sécurisée)
- Implémenter des tests automatisés à plusieurs niveaux
- Déployer l’application avec un minimum de configuration

---

## 🏗️ Architecture technique

- **Frontend** : React + Vite
- **Backend** : Spring Boot (API REST)
- **Base de données** : PostgreSQL
- **Authentification** : JWT (access & refresh tokens)

---

## 🚘 Fonctionnalités principales

### 🔐 Utilisateurs
- Authentification (login, refresh token)
- Création, mise à jour et suppression d'utilisateurs
- Changement de mot de passe sécurisé

### 🚗 Véhicules
- Création, modification et suppression
- Recherche par prix ou immatriculation
- Validation des champs & gestion des doublons

---

## ✅ Tests effectués

Une stratégie de test rigoureuse a été mise en œuvre à trois niveaux :

| Type de test         | Modules testés                                            | Outils                |
|----------------------|-----------------------------------------------------------|-----------------------|
| Tests unitaires      | Services (UserService, VehiculeService, JwtUtil, etc.)   | JUnit 5, Mockito      |
| Tests d’intégration  | Endpoints REST, DAO, Services avec base H2               | SpringBootTest        |
| Tests end-to-end     | Interface utilisateur, navigation et API                 | Playwright (Chromium) |

### 📁 Dossier des tests backend

📂 `vehicule/src/test/java`

#### 🔸 Tests unitaires :
- `JwtUtilTest`
- `PasswordValidator`

#### 🔸 Tests d’intégration :
- `UserControllerTest`
- `VehiculeControllerTest`
- `UserServiceTest`
- `VehiculeServiceTest`
- `JwtServiceIT`

---

## 🧪 Lancement des tests (hors pipeline CI)

```bash
cd vehicule
```

### 🔹 Tous les tests :
```bash
mvn test
```

### 🔹 Un seul fichier de test :
```bash
mvn -Dtest=NomDuFichierTest.java
```

**Exemple :**
```bash
mvn -Dtest=UserControllerTest.java
```

### 🔹 Couverture de test (JaCoCo) :
Ouvrir dans un navigateur :
```text
vehicule/target/site/jacoco/index.html
```

---

## 🐳 Lancement avec Docker

### Étapes

```bash
docker compose up --build -d
```

> ℹ️ **Si le conteneur `vehicule-backend` n’existe pas encore :**

```bash
docker compose up -d
```

- Backend accessible sur : `http://localhost:9000`
- Frontend (React + Vite) : `http://localhost:5173`

---

## ⚙️ Installation manuelle (hors Docker)

### Prérequis

- Node.js (v18+)
- Java 17+
- PostgreSQL
- Maven

### Étapes

```bash
# Cloner le dépôt
git clone https://github.com/daniloDevJava/Projet-Group10-Testing
```

#### Backend

```bash
cd vehicule
sudo apt install maven
mvn spring-boot:run
```

#### Frontend

```bash
cd frontend/properlize
npm install
npm run dev
```

---

## 📄 Exemple de jeux de données

- Emails valides et invalides
- Mots de passe forts et faibles
- Prix de véhicules valides et invalides
- Numéros d’immatriculation uniques ou doublons
- JWT expirés, valides et invalides

---

## 📄 Licence

Projet académique – Groupe 10 INF352

