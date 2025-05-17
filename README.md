# Secure Military Leave Tracker

A Java command line application to manage military leave requests securely. It includes user registration, login, leave submission, approval workflow, and persistent storage.

## Features

- Secure user registration and login with BCrypt password hashing
- Create, view, cancel, approve, and deny leave requests
- Role-based access: regular users manage their own requests; admins review all pending requests
- Embedded H2 database for storage of users and leave requests
- Structured logging with SLF4J and Logback
- Automated unit tests with JUnit 5

## Tech Stack

- **Java 17**
- **Maven** for build and dependency management
- **H2** embedded database
- **jbcrypt** for password hashing
- **SLF4J + Logback** for logging
- **JUnit 5** for automated testing

## Getting Started

### Prerequisites

- Java 17 or higher installed
- Maven 3.x installed
- Git (optional) for cloning the repository

### Build

```bash
mvn clean package
```

## Run

You can run the application from Maven:

```bash
mvn exec:java -Dexec.mainClass="com.leavetracker.App"
```

Or run the packaged jar:

```bash
java -jar target/secure-military-leave-tracker-1.0-SNAPSHOT.jar
```

## Tests

```bash
mvn test
```

## Project Structure

```
src/
  main/
    java/com/leavetracker/
      auth/           Authentication logic
      model/          Data classes (User, LeaveRequest)
      repository/     JDBC persistence layer
      service/        Business logic
      ui/             ConsoleUI for menus and prompts
      util/           Database setup helper
  test/
    java/com/leavetracker/   Unit tests for services
pom.xml                  Maven configuration and dependencies
```

## Next Steps

- Improve input validation and error handling
- Swap the command line interface for a lightweight JavaFX GUI or REST API
- Package and distribute as a standalone executable or Docker container  
