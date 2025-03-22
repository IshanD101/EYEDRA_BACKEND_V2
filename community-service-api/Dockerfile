FROM openjdk:17-jdk-alpine

# Set the working directory
WORKDIR /app

# Copy the built jar file from target folder
COPY target/community-service-api-0.0.1-SNAPSHOT.jar community-service-api.jar

# Expose the application port
EXPOSE 8083

# Environment variables for MongoDB and JWT
ENV JWT_SECRET=ThisIsA32ByteLongSecretKeyForJWT!! \
    SPRING_DATA_MONGODB_URI=mongodb://mongo:27017/eyedra_chat_db \
    EUREKA_SERVER_URI=http://eureka-server:8761/eureka/

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app/community-service-api.jar"]
