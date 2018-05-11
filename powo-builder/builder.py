import deployer
import datetime
import os
import sys

ENV = os.environ['ENVIRONMENT']
TAG = os.environ['DEPLOY_TAG']

existing = deployer.current_namespace(ENV)
build = deployer.next_namespace(ENV)

if not deployer.get_chart(TAG):
    print("Exiting")
    sys.exit(1)

if not deployer.deploy(build):
    print("Exiting")
    sys.exit(1)

if not deployer.wait_until_ready(build, timeout=datetime.timedelta(minutes=20)):
    print("Exiting")
    sys.exit(1)

# Load data

deployer.purge(existing)
