<image src="https://rawgit.com/RBGKew/powop/master/powo-portal/src/main/frontend/src/img/svg/powo-logo.svg" width="350px"/>

[![Build Status](https://travis-ci.org/RBGKew/powop.svg?branch=master)](https://travis-ci.org/RBGKew/powop)

Plants of the World Online Portal is a global, online, biodiversity information
resource.  This repository contains the code for the data model, harvester 
and web portal.

The POWO code powers:
* [**Plants of the World Online**](http://powo.science.kew.org)
* [**World Flora Online** (in development)](http://worldfloraonline.org/)

Deploy to UAT
---
 
* Update ‘powo-infrastructure/powo/values.yaml’ with your new portal image tag from google cloud container registry.
* Push the change to Github and Gitlab.
* Make sure google cloud context is `powo-dev`

`kubectl create job deploy-manual --from=cronjob/builder --namespace=builder-uat`

### Cancelling a job

If you want to stop a build job that has started (for example if it is not using the correct images) you can do the following:

1. Get the job name using `kubectl get jobs -n builder-uat` - you will probably be looking for the youngest job
2. Delete the job using `kubectl delete jobs/<job_name> -n builder-uat`

This will delete the job and any pods it has started.
 
Deploy to live
---
 
* Run deploy in root

* Update ‘powo-infrastructure/powo/prod.yaml’ with your new portal image tag from google cloud container registry.

* Push the change to Github and Gitlab.

* Switch your Google Cloud context to point to ‘powo-prod’ rather than ‘powo-dev’

* Run the same build command as you’ve been doing for UAT but change the namespace to ‘builder-prod’ e.g.

kubectl create job deploy-manual-live --from=cronjob/builder --namespace=builder-prod
 