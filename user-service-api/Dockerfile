FROM openjdk:17-jdk-alpine

# Set the working directory
WORKDIR /app

# Copy the built jar file from target folder
COPY target/user-service-api-0.0.1-SNAPSHOT.jar user-service-api.jar

# Expose the application port
EXPOSE 8080

# Environment variables for JWT and Cloudinary
ENV JWT_SECRET=ThisIsA32ByteLongSecretKeyForJWT!! \
    CLOUDINARY_CLOUD_NAME=daqjxasd6 \
    CLOUDINARY_API_KEY=229549534632989 \
    CLOUDINARY_API_SECRET=Cyixar1q7p5jIcrsKa-wu2YvD2E

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app/user-service-api.jar"]
