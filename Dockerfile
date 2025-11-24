# 1. Build Stage
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Salta i test per velocizzare la build ed evitare errori di connessione DB durante la build
RUN mvn clean package -DskipTests

# 2. Run Stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
# Render imposterà la variabile PORT, Spring Boot la userà
ENV SERVER_PORT=8080
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]