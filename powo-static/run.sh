#!/bin/sh
sed -i "s/{portal}/$PORTAL/" /etc/nginx/conf.d/default.conf

exec nginx
