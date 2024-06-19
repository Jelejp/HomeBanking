FROM gradle:8.7-jdk17-alpine AS build

WORKDIR /app

COPY homebanking/build.gradle homebanking/settings.gradle /app/

COPY homebanking /app

RUN gradle build --no-daemon

# Verificar que el archivo JAR se generó correctamente
RUN ls -al /app/build/libs/

FROM openjdk:17-jdk-alpine

WORKDIR /app

# Copia el archivo JAR generado del paso de construcción
COPY --from=build /app/build/libs/homebanking-0.0.1-SNAPSHOT.jar app.jar

# Verificar que el archivo JAR está presente en la imagen final
RUN ls -al /app

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
