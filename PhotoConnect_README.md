# PhotoConnect

PhotoConnect is a Spring Boot based enterprise web application designed
to connect photographers with clients through a secure, scalable, and
modular architecture.

The system demonstrates layered architecture, role-based authentication,
DTO usage, event handling, and secure enterprise development practices.

------------------------------------------------------------------------

## Overview

PhotoConnect follows a structured enterprise architecture:

-   Controller Layer (Web)
-   Service Layer (Business Logic)
-   Repository Layer (Data Access)
-   Security Layer (Authentication & Authorization)
-   DTO Layer (Data Transfer Objects)
-   Event Handling Layer

------------------------------------------------------------------------

## Project Structure

    com.example.PhotoConnect
    │
    ├── config        → Configuration classes
    ├── controller    → Web controllers
    ├── dto           → Data Transfer Objects
    ├── event         → Event-driven components
    ├── model         → JPA Entities
    ├── repository    → Spring Data JPA repositories
    ├── security      → Security configuration
    └── service       → Business logic layer

------------------------------------------------------------------------

## Technology Stack

-   Java 25
-   Spring Boot
-   Spring Security
-   Spring Data JPA (Hibernate)
-   MySQL / H2 Database
-   Maven

------------------------------------------------------------------------

## Security Features

-   Spring Security configuration
-   Role-based authorization
-   BCrypt password encryption
-   Secure endpoint protection
-   Authentication filter chain

------------------------------------------------------------------------

##  Core Features

### User Features

-   User registration
-   Secure login/logout
-   Profile management
-   View photographers
-   View portfolios

### Admin Features

-   Manage photographers
-   Upload portfolio images
-   Manage availability
-   Assign roles
-   Control system access

------------------------------------------------------------------------

##  Enterprise Design Principles

-   Layered Architecture
-   Separation of Concerns
-   DTO Pattern
-   Dependency Injection
-   Event-driven components
-   Secure authentication flow

------------------------------------------------------------------------

##  Installation & Setup

### 1️ Clone Repository

    git clone https://github.com/Kaarthiraghav/PhotoConnect.git
    cd PhotoConnect

### 2️ Configure Database

Update `application.properties`:

    spring.datasource.url=jdbc:mysql://localhost:3306/photoconnect
    spring.datasource.username=yourusername
    spring.datasource.password=yourpassword
    spring.jpa.hibernate.ddl-auto=update

### 3️ Build & Run

    mvn clean install
    mvn spring-boot:run

Application runs at:

    http://localhost:8080

------------------------------------------------------------------------

##  Scalability & Extensibility

The modular structure allows easy addition of:

-   REST APIs
-   Swagger documentation
-   JWT authentication
-   Cloud file storage
-   Pagination & sorting
-   Caching mechanisms

------------------------------------------------------------------------

##  Learning Outcomes

This project demonstrates:

-   Enterprise-level Spring Boot architecture
-   Secure authentication and authorization
-   DTO abstraction
-   Event-driven development
-   Clean layered backend design

------------------------------------------------------------------------

##  Author

Developed as part of an enterprise-level Spring Boot application
project.
