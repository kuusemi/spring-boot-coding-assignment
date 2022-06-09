FROM gradle:7.4.2-jdk11 AS build

MAINTAINER Mika Kuusela <mika.kuusela@digitalistgroup.com>

COPY settings.gradle build.gradle gradlew /code/
COPY .gradle/ /code/.gradle/
COPY gradle/ /code/gradle/
COPY src/ /code/src/

WORKDIR /code

RUN ./gradlew test bootJar

FROM azul/zulu-openjdk-alpine:11

RUN addgroup -S appgroup && adduser -S appuser -G appgroup

VOLUME /tmp
WORKDIR /home/appuser

COPY --from=build /code/build/libs/spring-boot-coding-assignment-0.0.1-SNAPSHOT.jar CodingAssignmentServer.jar

RUN chown appuser:appgroup CodingAssignmentServer.jar

USER appuser

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","CodingAssignmentServer.jar"]
