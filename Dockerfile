FROM gradle:8.7-jdk17-alpine

WORKDIR /app

COPY . .

EXPOSE 8080

RUN gradle build --no-daemon || return 0

ENTRYPOINT ["java", "-jar", "build/libs/homebanking-0.0.1-SNAPSHOT.jar"]