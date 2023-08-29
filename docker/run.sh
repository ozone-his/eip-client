#!/bin/sh
JAVA_OPTS=${JAVA_OPTS:-""}
echo "wait for $EIP_DB_HOST:$EIP_DB_PORT"
/bin/sh /wait-for.sh -t 3600 "$EIP_DB_HOST":"$EIP_DB_PORT"
exec java $JAVA_OPTS -jar app.jar 
