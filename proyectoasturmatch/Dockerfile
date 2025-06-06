FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copiar el código fuente
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

# Ejecutar mvnw para compilar
COPY src src
RUN chmod +x ./mvnw && ./mvnw clean package -DskipTests

# Etapa 2: imagen final
FROM eclipse-temurin:21-jre
WORKDIR /

# Exponer el puerto 8080
EXPOSE 8080
VOLUME /tmp

# Copiar el .jar generado desde la etapa de build
COPY --from=build /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

