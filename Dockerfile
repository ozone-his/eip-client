FROM openjdk:8-jdk-alpine
EXPOSE 8083
ARG JAR_FILE=./target/eip-app-1.0.0-SNAPSHOT.jar
RUN mkdir /config
RUN mkdir /routes
ADD ./properties/application.properties /application.properties
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]