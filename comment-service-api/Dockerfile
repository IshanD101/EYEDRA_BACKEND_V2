# Use OpenJDK 11 as the base image
FROM openjdk:11-jre-slim

# Set the working directory
WORKDIR /app

# Copy the pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build the application
RUN ./mvnw clean package -DskipTests

# Expose the application port
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "target/comment-service-api-1.0-SNAPSHOT.jar"]
