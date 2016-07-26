FROM tomcat:8.0-jre7

COPY conf/setenv.sh bin/

RUN rm -rf webapps/*

ADD emonocot-portal.war webapps/ROOT.war
