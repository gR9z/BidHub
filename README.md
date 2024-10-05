# BidHub

## English Version

### Introduction
BidHub is an auction creation application developed as part of a project for the ENI School of Computer Science's "Application Developer Designer" program. This application allows users to view a list of items available for auction, access individual product pages, and securely manage their accounts. The application includes complete CRUD functionality for all resources, an admin dashboard for comprehensive management, and features such as product category management and file hosting.

### Key Features
- **Auction Listings:** Displays a list of items available for auction with detailed single pages for each product.
- **User Authentication:** Secure login and registration system using Spring Security.
- **Bidding Logic:** Allows users to place bids on items.
- **User Profile:** Users can manage their profiles and view their bidding history.
- **CRUD Operations:** Full create, read, update, and delete functionality for all resources.
- **Admin Dashboard:** Admins have complete control over the platform, including user management, product category management, and more.
- **File Hosting:** Supports file uploads for product images and other relevant documents.

### Setup
1. Ensure Java SE 17 and SQL Server are installed on your system.
2. Clone or download the project to your local machine.
3. Create a `.env` file based on `.env.example` in the project root.
4. Add your own environment variables in the `.env` file
5. Run the application using your IDE.

### Managing Environment Variables

To avoid pushing sensitive information like credentials, follow these steps for managing environment variables:

1. **Create a `.env.example` file**  
   In the project root, create a `.env.example` file containing placeholders for required environment variables:
   ```bash
   DB_USER=your_db_username
   DB_PASS=your_db_password
2. **Add .env to `.gitignore`**  
   Ensure that the .env file, which will store actual credentials, is not committed by adding it to the `.gitignore` file:
   ```bash
   # .gitignore
   .env
   
### Technologies Used
- **Java Spring Boot:** For developing the backend and RESTful APIs.
- **Spring Security:** For securing the application.
- **JDBC:** For database interactions with SQL Server.
- **Thymeleaf:** For rendering the frontend.
- **SQL Server:** As the database management system.

### Contributors
This project was developed by:
- Cléa Le Corre
- Jean-Baptiste Marrec
- François Moisan
- Gaétan Royer (myself)

## Version Française

### Introduction
BidHub est une application de création d'enchères développée dans le cadre d'un projet de l'École Informatique ENI pour le programme "Concepteur Développeur d'Applications". Cette application permet aux utilisateurs de consulter une liste d'objets disponibles aux enchères, d'accéder à des pages individuelles pour chaque produit, et de gérer leurs comptes de manière sécurisée. L'application inclut une fonctionnalité CRUD complète pour toutes les ressources, un tableau de bord administrateur pour une gestion complète, ainsi que des fonctionnalités telles que la gestion des catégories de produits et l'hébergement de fichiers.

### Caractéristiques Clés
- **Liste des Enchères:** Affiche une liste d'articles disponibles aux enchères avec des pages détaillées pour chaque produit.
- **Authentification des Utilisateurs:** Système de connexion et d'inscription sécurisé utilisant Spring Security.
- **Logique d'Enchère:** Permet aux utilisateurs de placer des enchères sur les articles.
- **Profil Utilisateur:** Les utilisateurs peuvent gérer leurs profils et consulter leur historique d'enchères.
- **Opérations CRUD:** Fonctionnalité complète de création, lecture, mise à jour et suppression pour toutes les ressources.
- **Tableau de Bord Admin:** Les administrateurs ont un contrôle complet sur la plateforme, y compris la gestion des utilisateurs, des catégories de produits, et plus encore.
- **Hébergement de Fichiers:** Prend en charge les téléchargements de fichiers pour les images des produits et d'autres documents pertinents.

### Configuration
1. Assurez-vous que Java SE 17 et SQL Server sont installés sur votre système.
2. Clonez ou téléchargez le projet sur votre machine locale.
3. Créez un fichier `.env` basé sur `.env.example` dans le répertoire du projet.
4. Ajoutez vos propres variables d'environnement dans le fichier `.env`.
5. Exécutez l'application en utilisant votre IDE.

### Gestion des Variables d'Environnement
Pour éviter de pousser des informations sensibles comme des identifiants, suivez ces étapes pour gérer les variables d'environnement :

1. **Créez un fichier `.env.example`**  
   Dans le répertoire du projet, créez un fichier `.env.example` contenant des espaces réservés pour les variables d'environnement requises :
   ```bash
   DB_USER=your_db_username
   DB_PASS=your_db_password
2. **joutez .env à `.gitignore`**  
   Assurez-vous que le fichier .env, qui stockera les identifiants réels, n'est pas commis en l'ajoutant au fichier `.gitignore` :
   ```bash
   # .gitignore
   .env

### Technologies Utilisées
- **Java Spring Boot:** Pour développer le backend et les API RESTful.
- **Spring Security:** Pour sécuriser l'application.
- **JDBC:** Pour les interactions avec la base de données SQL Server.
- **Thymeleaf:** Pour rendre le frontend.
- **SQL Server:** Comme système de gestion de base de données.

### Contributeurs
Ce projet a été développé par :
- Cléa Le Corre
- Jean-Baptiste Marrec
- François Moisan
- Gaétan Royer (moi-même)
