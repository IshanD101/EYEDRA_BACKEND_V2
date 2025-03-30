# Use OpenJDK 17 as the base image
FROM eclipse-temurin:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the target directory (after Maven build)
COPY target/post-service-api-*.jar app.jar

# Expose the application port (match your `server.port=8081`)
EXPOSE 8081

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
