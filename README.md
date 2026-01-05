# Library Management System

A robust, enterprise-grade backend application built with **Java** and the **Spring Framework**. This project serves as a demonstration of my ability to design scalable, type-safe systems with a focus on clean architecture and data integrity.

## 🚀 Technical Stack
* **Language:** Java 17+
* **Framework:** Spring Boot 3.x
* **Data Access:** Spring Data JPA / Hibernate
* **Database:** MySQL / PostgreSQL
* **Build Tool:** Maven
* **Security:** Spring Security (JWT-based authentication)

## 🛠️ Key Features
* **Member Management:** Full CRUD operations for library members with role-based access control.
* **Book Inventory:** Real-time tracking of book availability, categories, and authors.
* **Transaction Logic:** Automated handling of book loans, returns, and overdue calculations.
* **RESTful API:** Clean, documented endpoints for seamless frontend integration.
* **Database Optimization:** Efficient indexing and schema design for handling large datasets.

## 🏗️ Architecture
The project follows a **Layered Architecture** to ensure separation of concerns:
1. **Controller Layer:** Handles HTTP requests and REST mapping.
2. **Service Layer:** Encapsulates business logic and transaction management.
3. **Repository Layer:** Manages data persistence using the Repository pattern.
4. **DTO Layer:** Ensures secure data transfer without exposing internal entities.

## 🚦 How to Run
1. Clone the repository: `git clone https://github.com/aarm29077/Library-Management-System.git`
2. Update `application.properties` with your database credentials.
3. Run the application: `mvn spring-boot:run`

---
*Developed as a personal project to maintain technical expertise in the Java/Spring ecosystem while working professionally in Python/Django.*
