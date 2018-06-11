from functools import reduce, partial
from kubernetes import client, config
from kubernetes.client.rest import ApiException
import datetime
import operator
import os
import random
import string
import subprocess
import time

print = partial(print, flush=True)

GIT_URL = 'https://github.com/RBGKew/powop-infrastructure'
CHART_FILES = '/tmp/helm'
SECRETS_FILE = '/secrets/secrets.yaml'

def api():
    config.load_kube_config()
    return client.CoreV1Api()

def wait_until_ready(namespace, timeout=None):
    wait = 5
    start = datetime.datetime.now()
    while True:
        try:
            pods = api().list_namespaced_pod(namespace)
            if not pods.items:
                continue
            if any([pod.status.phase != 'Running' for pod in pods.items]):
                continue
            statuses = [[status.ready for status in pod.status.container_statuses] for pod in pods.items]
            statuses = reduce(operator.concat, statuses)
            if all(statuses):
                print("All %d pods in %s ready" % (len(statuses), namespace))
                return True
        except ApiException as e:
            print("Exception listing pods: %s\n" % e)

        print("Waiting for all pods to be ready")
        time.sleep(wait)
        wait *= 2

        delta = datetime.datetime.now() - start
        if timeout is not None and delta > timeout:
            print("Not ready after %ds. Timed out" % delta.seconds)
            return False

def current_namespace(namespace):
    namespaces = [ns.metadata.name for ns in api().list_namespace().items if ns.metadata.name.startswith(namespace)]
    if len(namespaces) == 1:
        return namespaces[0]
    if len(namespaces) > 1:
        raise RuntimeError("More than one %s prefixed namespace: %s" % (namespace, namespaces))
    return None

def next_namespace(namespace):
    current_namespaces = set([ns.metadata.name for ns in api().list_namespace().items])
    while True:
        suffix = ''.join(random.choice(string.ascii_lowercase + string.digits) for _ in range(5))
        potential = "%s-%s" % (namespace, suffix)
        if potential in current_namespaces:
            continue
        print("Next namespace is: %s" % potential)
        return potential

def delete_namespace(namespace):
    api().delete_namespace(namespace, client.V1DeleteOptions())

def get_chart(tag='master'):
    if os.path.exists(CHART_FILES):
        cmd = ['git', 'pull', 'origin', tag]
        print("running (%s)" % " ".join(cmd))
        result = subprocess.run(cmd, stdout=subprocess.PIPE, cwd=CHART_FILES)
        if(result.returncode != 0):
            print("Error updating %s to %s" % (GIT_URL, tag))
            return False
    else:
        result = subprocess.run(['git', 'clone', '--branch', tag, GIT_URL, CHART_FILES], stdout=subprocess.PIPE)
        if(result.returncode != 0):
            print("Error cloning %s:%s to %s" % (GIT_URL, tag, CHART_FILES))
            return False
    return True

def deploy(name):
    powo_chart = os.path.join(CHART_FILES, 'powo')
    cmd = ['helm', 'install', '--namespace', name, '--name', name]
    values_file = os.path.join(powo_chart, "%s.yaml" % name.rsplit('-')[0])
    if os.path.exists(values_file):
        cmd += ['-f', values_file]
    if os.path.exists(SECRETS_FILE):
        cmd += ['-f', SECRETS_FILE]
    else:
        raise RuntimeError("Secrets file is requried and could not be found at %s" % SECRETS_FILE)
    cmd += [powo_chart]

    print("running (%s)" % " ".join(cmd))
    result = subprocess.run(cmd, stdout=subprocess.PIPE, cwd=CHART_FILES)
    if(result.returncode != 0):
        print("Error deploying %s" % name)
        return False

    print("%s deployment kicked off" % name)
    return True

def purge(name):
    if(not name):
        return True

    cmd = ['helm', 'delete', '--purge', name]
    print("running (%s)" % " ".join(cmd))
    result = subprocess.run(cmd, stdout=subprocess.PIPE, cwd=CHART_FILES)
    if(result.returncode != 0):
        print("Error deleting %s" % name)
        return False

    delete_namespace(name)
    print("%s deleted" % name)
    return True
