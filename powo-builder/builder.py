import datetime
import deployer
import dns
import harvester
import os
import sys

import logging as log
import google.cloud.logging

client = google.cloud.logging.Client(project=os.environ['G_PROJECT'])
client.setup_logging()

ENV = os.environ['ENVIRONMENT']
TAG = os.environ['DEPLOY_TAG']
DELETE_EXISTING = os.getenv('DELETE_EXISTING', 'true')

existing = deployer.current_namespace(ENV)
build = deployer.next_namespace(ENV)

if not deployer.get_chart(TAG):
    log.error("Error getting chart [%s] files. Exiting" % TAG)
    sys.exit(1)

if not deployer.deploy(build):
    log.error("Error deploying build %s. Exiting" % build)
    sys.exit(1)

if not deployer.wait_until_ready(build, timeout=datetime.timedelta(minutes=20)):
    log.error("Error while waiting for %s to be ready. Exiting" % build)
    sys.exit(1)

harvester.API_PREFIX = "http://apache.%s.svc.cluster.local" % build
if not harvester.load_data():
    log.error("Error loading data. Exiting")
    sys.exit(1)

if not dns.update(build):
    log.error("Error swapping dns. Exiting")
    sys.exit(1)

if DELETE_EXISTING == 'true':
    if not deployer.purge(existing):
        log.error("Error deleting old build [%s]. Exiting" % existing)
        sys.exit(1)
