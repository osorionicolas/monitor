FROM gradle:7.3.0-jdk11-alpine AS build

WORKDIR /usr/app/
COPY . .
RUN gradle build --no-daemon

FROM openjdk:11-jre-slim

ENV APP_HOME=/usr/app/

RUN mkdir /app

COPY --from=build /usr/app/build/libs/monitor.jar /app/spring-boot-application.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/spring-boot-application.jar"]