FROM eclipse-temurin:17-jre-jammy
LABEL maintainer="ozone-his.com"
# Copy the application zip file
COPY app/target/*.zip eip-client.zip
RUN apt-get update && apt-get install -y unzip
RUN unzip eip-client.zip -d . && rm eip-client.zip
## Create the following directories:
## /config - for configuration files
## /eip-home - for the EIP home directory
## /routes - for the routes directory (both XML and Java DSL)
ARG EIP_CLIENT_DIR=/eip-client
ARG EIP_CONFIG_DIR=${EIP_CLIENT_DIR}/config
ARG EIP_HOME_DIR=${EIP_CLIENT_DIR}/eip-home
ARG EIP_ROUTES_DIR=${EIP_CLIENT_DIR}/routes

RUN mkdir -p ${EIP_CONFIG_DIR} ${EIP_HOME_DIR} ${EIP_ROUTES_DIR}

WORKDIR ${EIP_CLIENT_DIR}

ENV EIP_PROFILE=dev

RUN chmod +x ./start.sh && chmod +x ./version.txt

EXPOSE 8080

CMD ["./start.sh"]
