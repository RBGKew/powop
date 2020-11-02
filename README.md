<image src="https://rawgit.com/RBGKew/powop/master/powo-portal/src/main/frontend/src/img/svg/powo-logo.svg" width="350px"/>

[![Build Status](https://travis-ci.org/RBGKew/powop.svg?branch=master)](https://travis-ci.org/RBGKew/powop)

Plants of the World Online Portal is a global, online, biodiversity information
resource.  This repository contains the code for the data model, harvester 
and web portal.

The POWO code powers:
* [**Plants of the World Online**](http://powo.science.kew.org)
* [**World Flora Online** (in development)](http://worldfloraonline.org/)

Deploy to UAT
 
* Update ‘powo-infrastructure/powo/values.yaml’ with your new portal image tag from google cloud container registry.

* Push the change to Github and Gitlab.

* Make sure google cloud context is 'powo-dev'

* Run this build command in root

kubectl create job deploy-manual --from=cronjob/builder --namespace=builder-uat
 
Deploy to live
 
* Run deploy in root

* Update ‘powo-infrastructure/powo/prod.yaml’ with your new portal image tag from google cloud container registry.

* Push the change to Github and Gitlab.

* Switch your Google Cloud context to point to ‘powo-prod’ rather than ‘powo-dev’

* Run the same build command as you’ve been doing for UAT but change the namespace to ‘builder-prod’ e.g.

kubectl create job deploy-manual-live --from=cronjob/builder --namespace=builder-prod
 