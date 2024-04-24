FROM gradle:8.7-jdk17-alpine AS build
WORKDIR /gradle
COPY . ./
RUN gradle build

FROM amazoncorretto:17.0.11-alpine
EXPOSE 8080
COPY --from=build /gradle/build/libs/*.jar /app/backend.jar
ENTRYPOINT ["java", "-jar", "/app/backend.jar"]
