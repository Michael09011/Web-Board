# 🚀 Web5 - Spring Boot Web Application Sample

Web5 is a Spring Boot–based web application sample project designed to provide a solid baseline for web development.
It uses server-side rendering (SSR) with Thymeleaf and includes a basic structure for managing members, boards, and notices.

## 📋 Requirements

Java 17 or higher

Gradle
(Using the included gradlew wrapper is recommended)

## 🛠 Tech Stack
Backend

Language: Java 17 (Gradle Toolchain)

Framework: Spring Boot 3.5.3

Web: spring-boot-starter-web

Template Engine

spring-boot-starter-thymeleaf

thymeleaf-extras-springsecurity6

Security: spring-boot-starter-security

Data Access: Spring Data JPA (Hibernate)

Database: MySQL (com.mysql:mysql-connector-j)

Utilities

Lombok

Spring Boot DevTools

Build & Plugins

Build Tool: Gradle

Dependency Management

io.spring.dependency-management (1.1.7)

Testing

JUnit Platform

spring-boot-starter-test

spring-security-test

Frontend

Rendering: Server-Side Rendering (Thymeleaf)

Static Resources

HTML, CSS, JavaScript

src/main/resources/static

🏗 Project Struc## 🏗 Project Structure 

'''
src/─ java/                         # Java source code
    └─ resources/
       ├─ templates/                # Thymeleaf templates
       ├─ static/                   # CSS, JS, Images
       └─ application.properties    # Application configuration

uploads/        uploads/                             # File uploads (local dev/test only)
'''


 (Windows)
1. Build
./gradlew.bat clean build

2. Run
./gradlew.bat bootRun


Or run the generated JAR:

java -jar build/libs/<generated-jar-name>.jar

3. Test
./gradlew.bat test

⚙️ Development Reference

Configuration
src/main/resources/application.properties

Database Schema
src/main/resources/schema.sql

File Uploads
Uploaded files are stored in the uploads/ directory at the project root
(for local development and testing only)

📝 Note

Actual dependencies and versions are defined in build.gradle.

It is recommended to keep dependencies up to date for security and performance.
