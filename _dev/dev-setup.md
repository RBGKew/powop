---
layout: page
title: "Development Setup"
---

## Build
The project is built using maven.

| `mvn install`                  | build the full project |
| `mvn test -Punit`              | run unit tests         |
| `mvn verify -P integration`    | run integration tests  |

## Run

To run, you need docker and docker-compose available.

* [Install Docker](https://docs.docker.com/engine/installation/)
* [Install Docker Compose](https://docs.docker.com/compose/install/)

Once you have built the project with `mvn install`, you should have all the docker
images needed to run the system.

```shell
$ docker-compose up
```
