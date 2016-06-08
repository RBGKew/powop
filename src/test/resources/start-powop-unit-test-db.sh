#!/bin/bash
#Parameters:i
#c-containername
#u-username
#p-password
#P-port
#v-volume
while getopts ":c:p:u:P:v:d:" opt; do
  case $opt in
    c) container="$OPTARG"
    ;;
    p) password="$OPTARG"
    ;;
    u) username="$OPTARG"
    ;;
    P) port="$OPTARG"
    ;;
    v) volume="$OPTARG"
    ;;
    d) database="$OPTARG"
    ;;
    \?) echo "Invalid option -$OPTARG" >&2
    ;;
  esac
done


docker stop $container
docker rm $container
docker run -d --name $container -v $volume:/etc/mysql/conf.d -e MYSQL_ROOT_PASSWORD=mysql -e MYSQL_USER=$username -e MYSQL_PASSWORD=$password -e MYSQL_DATABASE=$database -p $port:3306 mysql:5.5

while ! curl http://localhost:$port/ > /dev/null 2>&1
do
  echo "$(date) - still trying to connect to " "$container"
  sleep 1
done
echo "$(date) - connected successfully to " "$container"

