FROM gradle:8.7-jdk17-alpine

WORKDIR /app

# Copia los archivos de configuración de Gradle primero
COPY  homebanking/build.gradle homebanking/settings.gradle /app/

COPY homebanking /app

RUN gradle build --no-daemon

RUN ls -al /app/build/libs/

EXPOSE 8080

RUN gradle build --no-daemon || return 0

ENTRYPOINT ["java", "-jar", "build/libs/homebanking-0.0.1-SNAPSHOT.jar"]