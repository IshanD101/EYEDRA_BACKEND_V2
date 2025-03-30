FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY target/reaction-service-api-*.jar app.jar

EXPOSE 8084 5001

ENTRYPOINT ["java", "-jar", "app.jar"]
