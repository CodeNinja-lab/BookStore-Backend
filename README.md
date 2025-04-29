# 📚 Backend de l'application BookStore

Ce projet constitue le **backend** de l'application de vente de livres en ligne **BookStore**, développé avec **Spring Boot**. Il fournit une API RESTful pour gérer les livres, les utilisateurs, les commandes et les paiements.

---

## 🚀 Fonctionnalités principales

- 📚 Gestion des livres (CRUD)
- 👤 Gestion des utilisateurs (inscription, authentification)
- 🛒 Gestion du panier
- 🧾 Traitement des commandes
- 💳 Simulation de paiement
- 🔐 Sécurité avec JWT (JSON Web Token)
- 🌍 API REST bien structurée

---

## 🛠️ Technologies utilisées

- Java 17+
- Spring Boot
- Spring Data JPA
- Spring Security
- JWT pour l’authentification
- Hibernate
- MySQL ou PostgreSQL (configurable)
- Maven

---

## ⚙️ Prérequis

Avant de lancer l'application, assurez-vous d'avoir installé :

- Java 17 ou supérieur
- Maven
- Une base de données SQL (par exemple : MySQL ou PostgreSQL)
- Un outil comme Postman pour tester l'API

---

## 💾 Installation et exécution

### 1. Cloner le dépôt

```bash
git clone https://github.com/IbrahimaAliouneMbodj/bookstore-backend.git
cd bookstore-backend
 
### Configurer la base de données

Modifiez le fichier src/main/resources/application.properties
   spring.datasource.url=jdbc:mysql://localhost:3306/bookstore
   spring.datasource.username=your_username
   spring.datasource.password=your_password

   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true

# JWT secret
   jwt.secret=your_jwt_secret_key
Lancer l'application
   mvn spring-boot:run

