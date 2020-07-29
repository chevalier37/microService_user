FROM adoptopenjdk/openjdk11:alpine-jre

# Refer to Maven build -> finalName
# Faire d'abord : mvn package pour créer le jar, puis mettre le nom du jar ici
ARG JAR_FILE=target/TourGuide2_microService_User-0.0.1-SNAPSHOT.jar

#ENV PORT 9002
#EXPOSE 9002

# cd /opt/app
WORKDIR /opt/app

# cp target/spring-boot-web.jar /opt/app/app.jar
COPY ${JAR_FILE} app.jar

# java -jar /opt/app/app.jar
ENTRYPOINT ["java","-jar","app.jar"]