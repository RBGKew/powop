FROM tomcat:8.0-jre8-alpine

COPY logback-core-1.2.2.jar lib/
COPY logback-access-1.2.2.jar lib/
COPY bin/ bin/
COPY conf/ conf/

RUN rm -rf webapps/*

ADD emonocot-portal.war webapps/ROOT.war
