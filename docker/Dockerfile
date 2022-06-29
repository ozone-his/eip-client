FROM eclipse-temurin:8-jdk-focal
RUN mkdir /config
RUN mkdir /routes
RUN mkdir /eip-home
RUN apt-get update && apt install -y gettext netcat
COPY run.sh run.sh
COPY wait-for.sh wait-for.sh
RUN chmod +x run.sh wait-for.sh
ADD application.properties /application.properties
ADD *jar app.jar
EXPOSE 8083
ENTRYPOINT ["./run.sh"]
