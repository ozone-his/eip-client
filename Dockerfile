#
# Copyright © 2021, Ozone HIS <info@ozone-his.com>
#
# This Source Code Form is subject to the terms of the Mozilla Public
# License, v. 2.0. If a copy of the MPL was not distributed with this
# file, You can obtain one at http://mozilla.org/MPL/2.0/.
#

FROM amazoncorretto:17-alpine AS corretto-jdk

RUN apk add --no-cache binutils && \
    rm -rf /var/cache/apk/*
RUN jlink \
         --add-modules ALL-MODULE-PATH \
         --strip-debug \
         --no-man-pages \
         --no-header-files \
         --compress=2 \
         --output /jre

FROM alpine:3.21
LABEL maintainer="ozone-his.com"
ENV JAVA_HOME=/jre
ENV PATH="${JAVA_HOME}/bin:${PATH}"
RUN apk add -U unzip && rm -rf /var/cache/apk/*
COPY --from=corretto-jdk /jre $JAVA_HOME

# Copy the application zip file
COPY app/target/*.zip eip-client.zip
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

RUN chmod +x ./start.sh

EXPOSE 8080

CMD ["./start.sh"]
