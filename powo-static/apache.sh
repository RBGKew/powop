#!/bin/sh
sed -e "s/{portal}/$PORTAL/" conf.d/tomcat.conf > conf.d/tomcat.conf.tmp && mv conf.d/tomcat.conf.tmp conf.d/tomcat.conf
httpd-foreground
