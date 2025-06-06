# Usa una imagen de Java oficial
FROM openjdk:17-jdk-slim

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el JAR generado al contenedor
COPY target/*.jar app.jar

# Expone el puerto (Render lo usa automáticamente con env PORT)
EXPOSE 8080

# Ejecuta la app usando el puerto dinámico de Render
ENTRYPOINT ["java", "-Dserver.port=$PORT", "-jar", "app.jar"]
