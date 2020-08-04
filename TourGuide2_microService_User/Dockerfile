FROM maven:3.5.2-jdk-8-alpine AS MAVEN_BUILD

MAINTAINER JB ROUSSAT

COPY pom.xml /build/

COPY src /build/src/

WORKDIR /build/

RUN mvn clean package spring-boot:repackage  -Dmaven.test.skip=true

FROM openjdk:8-jre-alpine


WORKDIR /app

COPY --from=MAVEN_BUILD /build/target/TourGuide2_microService_User-0.0.1-SNAPSHOT.jar /app/

ENTRYPOINT ["java", "-jar", "TourGuide2_microService_Zuul-0.0.1-SNAPSHOT.jar"]