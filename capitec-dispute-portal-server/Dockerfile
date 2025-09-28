# Use OpenJDK 17 as base image
FROM openjdk:17-jdk-slim

# Install wget for health checks

# Set working directory
WORKDIR /app

# Copy the JAR file
COPY target/capitec-dispute-portal-server.jar app.jar

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
