FROM tomcat:8.0-jre8-alpine

COPY conf/setenv.sh bin/

RUN rm -rf webapps/* \
  && mkdir work/powop

ADD emonocot-harvest.war webapps/harvester.war
