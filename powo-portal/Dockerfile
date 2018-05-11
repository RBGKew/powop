FROM tomcat:8.0-jre8-alpine

COPY docker/bin/ bin/
COPY docker/conf/ conf/

RUN rm -rf webapps/*

ADD target/powo-portal.war webapps/ROOT.war
