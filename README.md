[![Release](https://img.shields.io/github/v/tag/masonakcamara/secure-military-leave-tracker)](https://github.com/masonakcamara/secure-military-leave-tracker/releases)
[![CI](https://github.com/masonakcamara/secure-military-leave-tracker/actions/workflows/ci.yml/badge.svg)](https://github.com/masonakcamara/secure-military-leave-tracker/actions/workflows/ci.yml)

# Secure Military Leave Tracker

A Java application to manage military leave requests securely. It offers both a command-line interface (CLI) and a JavaFX graphical interface, with strong authentication, approval workflows, and persistent storage.

## Features

- **User authentication** with BCrypt-hashed passwords  
- **Leave requests**: create, view, cancel, approve, and deny  
- **Role-based access**: regular users manage their own requests; admins review all pending requests  
- **In-memory CLI** and **JavaFX UI** for interactive workflows  
- **H2 embedded database** for persisting users and leave requests  
- **Structured logging** via SLF4J and Logback  
- **Automated unit and integration tests** with JUnit 5  
- **Continuous integration** with GitHub Actions (build, test, package)

## Tech Stack

- **Java 17**  
- **Maven** for build and dependency management  
- **H2** embedded database  
- **jBCrypt** for password hashing  
- **SLF4J + Logback** for logging  
- **JUnit 5** for testing  
- **JavaFX** for the graphical UI  

## Getting Started

### Prerequisites

- Java 17 or higher  
- Maven 3.x  
- Git (optional)  

### Build

```bash
mvn clean package
```

This produces a “fat” JAR in `target/leave-tracker.jar`.

### Run (CLI)

```bash
mvn exec:java -Dexec.mainClass="com.leavetracker.App"
```

### Run (JavaFX GUI)

```bash
mvn javafx:run
```

### Tests

```bash
mvn test
```

## Continuous Integration

A GitHub Actions workflow (`.github/workflows/ci.yml`) runs on every push and pull request to `main`, executing:

1. `mvn clean verify` (compile & tests)  
2. `mvn package` (builds shaded JAR)

## Project Structure

```
src/
  main/
    java/com/leavetracker/
      auth/           Authentication logic
      model/          Data classes (User, LeaveRequest)
      repository/     JDBC persistence layer
      service/        Business logic
      ui/             
        ConsoleUI     CLI menus and prompts
        fx/           JavaFX controllers and entry points
      util/           Database initialization helper
  main/
    resources/
      fx/             FXML views for JavaFX
  test/
    java/com/leavetracker/
      auth/           AuthService unit tests
      service/        LeaveService unit tests
      repository/     UserRepository & LeaveRequestRepository integration tests
pom.xml             Maven configuration and plugins
README.md           Project overview and instructions
LICENSE             MIT license
