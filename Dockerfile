FROM eclipse-temurin:17-jre-jammy as builder
WORKDIR application
ARG JAR_FILE=app/target/*.jar
COPY ${JAR_FILE} eip-client.jar
## Extract the layers from the jar file
RUN java -Djarmode=layertools -jar eip-client.jar extract

FROM eclipse-temurin:17-jre-jammy
LABEL maintainer="ozone-his.com"
WORKDIR ozone-eip-client

## Create the following directories:
## /config - for configuration files
## /eip-home - for the EIP home directory
## /routes - for the routes
## /camel/xml-routes - for the routes written in XML
## /camel/java-routes - for the routes written in Java(Jars)
ARG EIP_CONFIG_DIR=/config
ARG EIP_HOME_DIR=/eip-home
ARG EIP_ROUTES_DIR=/routes
ARG EIP_XML_ROUTES_DIR=/camel/xml-routes
ARG EIP_JAVA_ROUTES_DIR=/camel/java-routes

RUN mkdir -p ${EIP_CONFIG_DIR} ${EIP_HOME_DIR} ${EIP_ROUTES_DIR} ${EIP_XML_ROUTES_DIR} ${EIP_JAVA_ROUTES_DIR}
## Copy files from the builder stage
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./
COPY --from=builder application/ozone-dependencies/ ./

## Set configurable Java options and other useful settings
ENV EXTRA_JAVA_OPTS=""
ENV JAVA_OPTS="-Djava.security.egd=file:/dev/./urandom -Xmx512m -Xms256m "${EXTRA_JAVA_OPTS}""
#ENV SPRING_PROFILES_ACTIVE=prod


## A comma-separated list of directories (containing file resources and/or nested archives in *.jar or *.zip or archives)
## or archives to append to the classpath. BOOT-INF/classes,BOOT-INF/lib in the application archive are always used.
ENV LOADER_PATH=${EIP_CONFIG_DIR},${EIP_HOME_DIR},${EIP_ROUTES_DIR},${EIP_XML_ROUTES_DIR},${EIP_JAVA_ROUTES_DIR}

## The name of the main class. If not specified, the main class will be searched in the manifest file.
#ENV LOADER_MAIN=com.ozonehis.eip.Application

EXPOSE 8080

ENTRYPOINT ["java", "org.springframework.boot.loader.PropertiesLauncher"]
