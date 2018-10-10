import datetime
import deployer
import dns
import functools
import harvester
import os
import sys

log = functools.partial(print, flush=True)

ENV = os.environ['ENVIRONMENT']
TAG = os.environ['DEPLOY_TAG']
DELETE_EXISTING = os.getenv('DELETE_EXISTING', 'true')

existing = deployer.current_namespace(ENV)
build = deployer.next_namespace(ENV)

if not deployer.get_chart(TAG):
    log("Error getting chart [%s] files. Exiting" % TAG)
    sys.exit(1)

if not deployer.deploy(build):
    log("Error deploying build %s. Exiting" % build)
    sys.exit(1)

if not deployer.wait_until_ready(build, timeout=datetime.timedelta(minutes=20)):
    log("Error while waiting for %s to be ready. Exiting" % build)
    sys.exit(1)

harvester.API_PREFIX = "http://apache.%s.svc.cluster.local" % build
if not harvester.load_data():
    log("Error loading data. Exiting")
    sys.exit(1)

if not dns.update(build):
    log("Error swapping dns. Exiting")
    sys.exit(1)

if DELETE_EXISTING == 'true':
    if not deployer.purge(existing):
        log("Error deleting old build [%s]. Exiting" % existing)
        sys.exit(1)
