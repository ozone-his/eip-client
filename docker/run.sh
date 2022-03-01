#!/bin/sh
# envsubst < "./config/application.properties" > "./config/application.properties.tmp"
# mv "./config/application.properties.tmp" "./config/application.properties"

echo "wait for $EIP_DB_HOST:$EIP_DB_PORT"
/bin/sh /wait-for.sh -t 3600 "$EIP_DB_HOST":"$EIP_DB_PORT"
java -jar app.jar
