# Etapa 1: Construcción de la aplicación
FROM gradle:8.7-jdk17-alpine AS build

WORKDIR /app

# Copia los archivos de configuración de Gradle primero
COPY homebanking/build.gradle homebanking/settings.gradle /app/

# Copia el resto del código fuente
COPY homebanking /app

# Construye el proyecto
RUN gradle build --no-daemon

# Verificar que el archivo JAR se generó correctamente
RUN ls -al /app/build/libs/

# Etapa 2: Imagen final
FROM openjdk:17-jdk-alpine

WORKDIR /app

# Copia el archivo JAR generado del paso de construcción
COPY --from=build /app/build/libs/homebanking-0.0.1-SNAPSHOT.jar app.jar

# Expone el puerto en el que la aplicación se ejecutará
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
