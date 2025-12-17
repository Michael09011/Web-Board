# 🚀 Web-Board

**Web-Board** is a comprehensive Spring Boot-based web application sample project. It serves as a robust baseline for modern web development, utilizing Server-Side Rendering (SSR) with Thymeleaf and providing essential modules for Member Management, Boards, and Notices.

<p align="center">
<img width="686" height="956" alt="스크린샷 2025-12-18 012026" src="https://github.com/user-attachments/assets/2229f494-f8c8-42d4-a0f7-a9fd26c63617" />
</p>
---

## 📋 Requirements
- [x] **Java 17** or higher
- [x] **Gradle** (Using the included `gradlew` wrapper is highly recommended)

---

## 🛠 Tech Stack

### Backend
- **Language**: Java 17 (Gradle Toolchain)
- **Framework**: Spring Boot 3.5.3
- **Security**: Spring Security 6
- **Data Access**: Spring Data JPA (Hibernate)
- **Database**: MySQL (`com.mysql:mysql-connector-j`)

### Frontend & Utilities
- **Rendering**: Thymeleaf (Server-Side Rendering)
- **Security Integration**: Thymeleaf Extras SpringSecurity6
- **Utilities**: Lombok, Spring Boot DevTools
- **Build Tool**: Gradle (Dependency Management: 1.1.7)

---

## 🏗 Project Structure

```text
src/
 └─ main/
     ├─ java/                # Java source code (Controller, Service, Repository)
     └─ resources/
         ├─ templates/       # Thymeleaf templates (.html)
         ├─ static/          # Static resources (CSS, JS, Images)
         ├─ application.properties  # Application configuration
         └─ schema.sql       # Initial database schema
uploads/                     # File storage (Local development/testing only)
``` 
---

## 🚀 Getting Started (Windows)

### 1. Build
To compile the project and build the artifacts, run:
```powershell
./gradlew.bat clean build
```

### 2. Run
You can start the application using the Spring Boot Gradle plugin:
```
PowerShell

./gradlew.bat bootRun
```
Note: Or execute the generated JAR file directly after building:
```
PowerShell

java -jar build/libs/<generated-jar-name>.jar
```
### 3. Test
To execute the test suite, run:
```
PowerShell

./gradlew.bat test
```
---

## ⚙️ Development Reference

* **Configuration**: All application settings and environment variables are located in `src/main/resources/application.properties`.
* **Database Schema**: Initial table definitions and constraints can be found in `src/main/resources/schema.sql`.
* **File Uploads**: Files uploaded during runtime are stored in the `/uploads` directory at the project root (specifically for local development and testing).

> [!IMPORTANT]
> Actual dependencies and specific versions are defined in the `build.gradle` file. It is highly recommended to keep these dependencies up-to-date to ensure optimal security and performance.
