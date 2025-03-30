FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY target/gateway-service-api-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENV EUREKA_SERVER=https://eureka-server-226126989665.asia-south1.run.app
ENV JWT_SECRET=ThisIsA32ByteLongSecretKeyForJWT!!

ENTRYPOINT ["java", "-jar", "app.jar"]