# Multi-stage build: the Gradle distribution exists only in the build stage.
FROM gradle:8.14.3-jdk17 AS build
WORKDIR /home/gradle/project
COPY --chown=gradle:gradle build.gradle settings.gradle gradle.properties ./
COPY --chown=gradle:gradle src ./src
RUN gradle --no-daemon clean bootJar

FROM eclipse-temurin:17-jre
WORKDIR /app
RUN groupadd --system appgroup && useradd --system --gid appgroup appuser
COPY --from=build /home/gradle/project/build/libs/student-api.jar /app/student-api.jar
USER appuser
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/student-api.jar"]
