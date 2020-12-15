Plants of the World Online
===

Plants of the World Online Portal is a global, online, biodiversity information resource. This repository contains the code for the data model, harvester and web portal.

The POWO code powers:
* [**Plants of the World Online**](http://powo.science.kew.org)
* [**World Flora Online** (in development)](http://worldfloraonline.org/)

Developing
===

The easiest way to run the POWO app is using `docker-compose`. In order to do this you need to run the following to build images for the different modules:

```
mvn package
```

Once this is done you can run the following to start the application

```
docker-compose up
```

### Initial setup

Once you have the application up and running you will need to load some data to interact with.

### Making changes

After making changes to a specific module you can rebuild just that module using the following command (using the relevant Maven module e.g. `powo-portal`):

```
mvn package -pl powo-portal
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

> If you make changes to the frontend code e.g. JS / CSS you will need to rebuild both `powo-portal` and `powo-static`

Deployment
===

Deploy to UAT
---
 
* Update ‘powo-infrastructure/powo/values.yaml’ with your new portal image tag from google cloud container registry.
* Push the change to Github and Gitlab.
* Make sure google cloud context is `powo-dev`

`kubectl create job deploy-manual --from=cronjob/builder --namespace=builder-uat`

### Cancelling a job

To cancel a job we need to:

1. Stop the job and any pods it has created
2. Remove the Helm deployment created as part of the job
3. Remove the namespace created as part of the job.

**Cancel the job**

1. Get the job name using `kubectl get jobs -n builder-uat` - you will probably be looking for the youngest job
2. Delete the job using `kubectl delete jobs/<job_name> -n builder-uat`

**Remove Helm deployment**

1. Get the name of the Helm release using `helm ls` - you want the release which was created later
2. Delete the helm release using `helm delete --purge <release_name>`

The release name is also the name of the namespace - you will need it in the following step.

**Remove the namespace**

1. Get the namespace name based on the previous step, or run `kubectl get ns` and use the youngest namespace 
2. Delete the old namespace using `kubectl delete ns <namespace_name>`

This step is required since the builder raises an error if there would be more than 3 namespaces at one time.
 
Deploy to live
---
 
* Run deploy in root

* Update ‘powo-infrastructure/powo/prod.yaml’ with your new portal image tag from google cloud container registry.

* Push the change to Github and Gitlab.

* Switch your Google Cloud context to point to ‘powo-prod’ rather than ‘powo-dev’

* Run the same build command as you’ve been doing for UAT but change the namespace to ‘builder-prod’ e.g.

kubectl create job deploy-manual-live --from=cronjob/builder --namespace=builder-prod
 