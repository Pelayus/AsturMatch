# Etapa 1: build con Maven
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copiar solo el pom.xml primero para aprovechar cache de docker
COPY pom.xml .

# Copiar el resto del código fuente (asumiendo que está en src/)
COPY src ./src

# Construir el proyecto (sin tests)
RUN mvn clean package -DskipTests

# Etapa 2: imagen final
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copiar el .jar generado desde la etapa de build
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java -Dserver.port=$PORT -jar app.jar"]



