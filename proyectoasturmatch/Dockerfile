# Etapa 1: build con MavenAdd commentMore actions
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copiar solo el pom.xml primero para aprovechar cache de docker
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

# Ejecutar mvnw para descargar dependencias
RUN chmod +x ./mvnw && ./mvnw dependency:go-offline

# Copiar el resto del código fuente
COPY src src
RUN ./mvnw clean package -DskipTests

# Etapa 2: imagen final
FROM eclipse-temurin:21-jre
WORKDIR /

# Exponer el puerto 8080
EXPOSE 8080
VOLUME /tmp

# Copiar el .jar generado desde la etapa de build
COPY --from=build /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]



