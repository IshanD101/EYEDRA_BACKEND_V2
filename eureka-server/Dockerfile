# Use the OpenJDK 17 Alpine image as the base image
FROM openjdk:17-jdk-alpine

# Set the working directory
WORKDIR /app

# Copy the built jar file from the target folder
COPY target/eureka-server-0.0.1-SNAPSHOT.jar eureka-server.jar

# Expose the Eureka server port
EXPOSE 8761

# Run the Eureka Server
ENTRYPOINT ["java", "-jar", "/app/eureka-server.jar"]
