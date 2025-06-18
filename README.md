# Properlize – Application de Gestion de Véhicules et Utilisateurs

Properlize est une application web complète destinée à la gestion centralisée de véhicules et d’utilisateurs, conçue dans le cadre d’un projet académique. Elle met en œuvre une architecture moderne, une interface intuitive, une API sécurisée, ainsi qu'une stratégie de tests rigoureuse.

---

## Objectifs du projet

- Gérer un parc de véhicules (création, modification, suppression, recherche)
- Gérer les comptes utilisateurs (ajout, modification, authentification sécurisée)
- Implémenter des tests automatisés à plusieurs niveaux
- Déployer l’application avec un minimum de configuration

---

## Architecture technique

- Frontend : React + Vite
- Backend : Spring Boot (API REST)
- Base de données : PostgreSQL
- Authentification : JWT (access & refresh tokens)

---

## Fonctionnalités principales

### Utilisateurs
- Authentification (login, token refresh)
- Ajout / mise à jour / suppression d'utilisateurs
- Changement de mot de passe sécurisé

### Véhicules
- Création, édition, suppression de véhicules
- Recherche par prix ou immatriculation
- Validation des champs et gestion des doublons

---

## Tests effectués

Une stratégie de test en trois niveaux a été mise en place pour garantir la qualité de l’application :

| Type de test | Modules testés | Outils utilisés |
|--------------|----------------|-----------------|
| Tests unitaires | Services du backend (userService, vehicleService, tokenService) | JUnit 5, Mockito |
| Tests d'intégration | Endpoints REST, couche DAO (avec base H2) | SpringBootTest |
| Tests End-to-End (E2E) | Interface utilisateur, navigation, formulaire, interactions avec API | Playwright (navigateur Chromium) |

---

## Exemple de données de test

- Emails valides et invalides
- Mots de passe forts et faibles
- Prix de véhicules positifs et négatifs
- Numéros d’immatriculation uniques et en doublon
- Tokens expirés, invalides, et valides

---

## Installation et exécution

### Prérequis

- Node.js (v18+)
- Java 17+
- PostgreSQL
- Maven

### Installation

# Cloner le dépôt
git clone https://github.com/daniloDevJava/Projet-Group10-Testing

# Backend
cd backend
./mvnw spring-boot:run

# Frontend
cd frontend/properlize
npm install
npm run dev
