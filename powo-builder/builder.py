import datetime
import deployer
import functools
import harvester
import os
import sys

print = functools.partial(print, flush=True)

ENV = os.environ['ENVIRONMENT']
TAG = os.environ['DEPLOY_TAG']

existing = deployer.current_namespace(ENV)
build = deployer.next_namespace(ENV)

if not deployer.get_chart(TAG):
    print("Error getting chart [%s] files. Exiting" % TAG)
    sys.exit(1)

if not deployer.deploy(build):
    print("Error deploying build %s. Exiting" % build)
    sys.exit(1)

if not deployer.wait_until_ready(build, timeout=datetime.timedelta(minutes=20)):
    print("Error while waiting for %s to be ready. Exiting" % build)
    sys.exit(1)

harvester.API_PREFIX = "http://apache.%s.svc.cluster.local" % build
if not harvester.load_data():
    print("Error loading data. Exiting")
    sys.exit(1)

if not deployer.purge(existing):
    print("Error deleting old build [%s]. Exiting" % existing)
    sys.exit(1)
