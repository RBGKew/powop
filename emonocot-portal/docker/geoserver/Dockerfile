from tomcat:7.0-jre7

ENV GEOSERVER_WAR https://storage.googleapis.com/kew-dev-backup/geoserver.war
ENV DATA_DIR /var/lib/geoserver

RUN rm -rf webapps/* \
  && wget -nv --output-document=/usr/local/tomcat/webapps/ROOT.war $GEOSERVER_WAR \
  && mkdir -p $DATA_DIR/data \
  && unzip -d $DATA_DIR webapps/ROOT.war 'data/*'

COPY styles/* ${DATA_DIR}/styles/
COPY workspaces ${DATA_DIR}/workspaces
COPY users.properties ${DATA_DIR}/security/
