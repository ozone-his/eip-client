FROM openjdk:8-jdk-alpine
RUN mkdir /config
RUN mkdir /routes
RUN mkdir /eip-home
RUN apk add gettext --no-cache
COPY run.sh run.sh
COPY wait-for.sh wait-for.sh
RUN chmod +x run.sh wait-for.sh
ADD application.properties /application.properties
ADD *jar app.jar
EXPOSE 8083
ENTRYPOINT ["./run.sh"]
