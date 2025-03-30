FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY target/gateway-service-api-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8070

ENV EUREKA_SERVER=http://localhost:8761/eureka/
ENV JWT_SECRET=ThisIsA32ByteLongSecretKeyForJWT!!

ENTRYPOINT ["java", "-jar", "app.jar"]