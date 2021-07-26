FROM openjdk:8-jdk-alpine
EXPOSE 8083
RUN mkdir /config
RUN mkdir /routes
RUN mkdir /eip-home
ADD application.properties /application.properties
ADD *jar app.jar
EXPOSE 8083
ENTRYPOINT ["java","-jar","/app.jar"]
