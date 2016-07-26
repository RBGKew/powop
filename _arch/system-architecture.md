---
layout: page
title: "System Architecture"
---

Docker is used to build and run the various system components, and Kubernetes is used
for orchestration. The components of the system are:

* Tomcat
* Apache
* MySql
* Solr
* Geoserver
* ActiveMQ

The images can be built and used localy, but are also built by
[Travis](https://travis-ci.org/RBGKew/powop).

# Docker

The web application is split into two components, the portal and harvester. Each is
packaged in it's own tomcat container and run independently. Apache sits in front of the
tomcat applications as a proxy and to serve static assets.

Docker images are built via maven and the spotify docker-maven-plugin. Docker
configuration lives in the `docker/` directory of the relevant submodule.

# Kubernetes

We are targeting Google Container Engine (gce) as our deployment environment. Images are
named appropriately for pushing to the Google Container Registry. The specific repo to use
is specified with the `docker.registry` property.

Configuration is available on [Github](https://github.com/RBGKew/powop-infrastructure)
