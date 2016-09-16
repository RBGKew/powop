from tomcat:8.0-jre8-alpine

ENV VERSION 2.9.1
ENV DATA_DIR webapps/ROOT/data

RUN wget -q -O /tmp/geoserver.zip http://downloads.sourceforge.net/project/geoserver/GeoServer/$VERSION/geoserver-$VERSION-war.zip
RUN rm -rf webapps/* \
  && mkdir webapps/ROOT \
  && mkdir $DATA_DIR \
  && unzip -q /tmp/geoserver.zip geoserver.war \
  && unzip -q geoserver.war -d webapps/ROOT \
  && rm -rf ${DATA_DIR}/styles \
  && rm -rf ${DATA_DIR}/workspaces \
  && rm -rf ${DATA_DIR}/demo \
  && rm /tmp/geoserver.zip && rm geoserver.war

COPY styles/* ${DATA_DIR}/styles/
COPY workspaces ${DATA_DIR}/workspaces
COPY lib/* webapps/ROOT/WEB-INF/lib/
COPY users.properties ${DATA_DIR}/security/
