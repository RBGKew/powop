Plants of the World Online
===

Plants of the World Online Portal is a global, online, biodiversity information resource. This repository contains the code for the data model, harvester and web portal.

The POWO code powers:
* [**Plants of the World Online**](http://powo.science.kew.org)
* [**World Flora Online** (in development)](http://worldfloraonline.org/)

Developing
---

The easiest way to run the POWO app is using `docker-compose`. In order to do this you need to run the following to build images for the different modules:

```
mvn install
```

Once this is done you can run the following to start the application

```
docker-compose up
```

### Initial setup

Once you have the application up and running you will need to load some data to interact with. To do this:

1. Go to `http://localhost:10080/admin/#/organisations`
2. Click the cog in the top right and login with username `admin` and password `password`
3. Click the cog in the top right and click import - select the `powo-harvest/local-development-data-configuration.json` as the file
4. Go to `http://localhost:10080/admin/#/lists` and click the play icon next to the `Load everything` job [Note: if you want images in search results, first follow the Images instructions below]

This loads a subset of the full data onto your development machine. It can take several hours to complete.

### Making changes

If any module dependencies have been updated (e.g. after running `git pull`) rebuild your local modules using:

```
mvn install
```

After making changes to a specific module you can rebuild just that module using the following commands (using the relevant Maven module e.g. `powo-portal`):

```
mvn prepare-package -pl powo-portal
mvn package -pl powo-static
```

Then you can restart that service using the following command (using the relevant service name defined in `docker-compose.yaml` e.g. `portal`):

```
docker-compose up portal
```

You can run services that change infrequently in the background to simplify this process and make startup easier:

```
docker-compose up -d db harvester solr geoserver geodb
```

Then restarting only the services you need:

```
docker-compose up portal ingress
```


#### Making changes to the frontend

First, start all the docker services with `docker-compose up -d`.

If you make changes to the frontend handlebars templates you will need to rebuild `powo-portal`:

```
mvn package -pl powo-portal -Ddockerfile.skip
```

If you are working mainly on the frontend JS or CSS, you can use the following command to start automatic asset recompilation and browser reload:

```
# If yarn not installed globally
npm i -g yarn

# Then start automatic asset compilation
cd powo-portal/src/main/frontend
yarn dev

# Connect to a different backend, for developing an alternative POWO site
yarn dev --backend-port=20080
```

### Issues with services hanging

If the `portal` and `harvester` services are hanging and failing to startup, it can be an issue with an unreleased lock on a Liquibase managed table. This can happen when services are stopped and don't properly release the locks. To fix this you can run:

```
docker exec <your_db_container_id> mysql --user=powo --password=powo powo -e "UPDATE DATABASECHANGELOGLOCK SET LOCKED=0, LOCKGRANTED=null, LOCKEDBY=null where ID=1;"
```

This will reset the lock on the db, after that you can stop and restart your containers and they should be OK.


### Images

In order for the app to load images correctly, it needs a CDN key. The steps to do this locally are:

1. Get the CDN key from the POWO team
2. Create a file called `.env` in the root of the project
3. Add the following line, replacing `your_cdn_key` with the actual key:

```
CDN_KEY=your_cdn_key
```

Deployment
===

Deployment for POWO is managed via the [powo-infrastructure](https://gitlab.ad.kew.org/development/powop-infrastructure) repository. Please refer to that for all deployment documentation.
