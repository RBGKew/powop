from tomcat:8.0-jre8-alpine

ENV VERSION 2.12.2
ENV DATA_DIR webapps/ROOT/data

RUN apk add --no-cache openssl ca-certificates wget \
  && wget -q -O /tmp/geoserver.zip https://downloads.sourceforge.net/project/geoserver/GeoServer/$VERSION/geoserver-$VERSION-war.zip
RUN rm -rf webapps/* \
  && mkdir webapps/ROOT \
  && mkdir $DATA_DIR \
  && mkdir $DATA_DIR/logs \
  && unzip -q /tmp/geoserver.zip geoserver.war \
  && unzip -q geoserver.war -d webapps/ROOT \
  && rm -rf ${DATA_DIR}/styles \
  && rm -rf ${DATA_DIR}/workspaces \
  && rm -rf ${DATA_DIR}/demo \
  && rm /tmp/geoserver.zip && rm geoserver.war

COPY styles/* ${DATA_DIR}/styles/
COPY workspaces ${DATA_DIR}/workspaces
COPY lib/* webapps/ROOT/WEB-INF/lib/
COPY logging.xml ${DATA_DIR}/
COPY users.properties ${DATA_DIR}/security/
COPY PRODUCTION_LOGGING.properties ${DATA_DIR}/logs
