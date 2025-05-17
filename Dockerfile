# Start from a lightweight OpenJDK runtime
FROM eclipse-temurin:17-jre-alpine

# Set a working directory
WORKDIR /app

# Copy the fat JAR produced by Maven
COPY target/leave-tracker.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
