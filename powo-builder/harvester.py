import datetime
import functools
import requests
import time
import os

print = functools.partial(print, flush=True)

API_PREFIX = os.getenv('API_PREFIX', 'http://localhost:10080')
API_URL = os.getenv('API_URL', '/harvester/api/1')

def _api(method):
    return API_PREFIX + API_URL + method

def load_data_config():
    config = open('data.json', 'br')
    headers = {'Content-Type': 'application/json; charset=utf-8'}
    r = requests.post(_api('/data'), data=config, headers=headers)
    r.raise_for_status()
    print("Data configuration loaded")
    return r.json()

def get_data_config():
    r = requests.get(_api('/data'))
    r.raise_for_status()
    return r.json()

def run_joblist(identifier):
    r = requests.post(_api('/job/list/' + identifier + '/run'))
    print("Running job list with identifier %s" % identifier)
    r.raise_for_status()
    return r.json()

def joblist_running(identifier):
    r = requests.get(_api('/job/list/' + identifier))
    r.raise_for_status()
    return r.json()['hasNextJob']

def load_data():
    load_data_config()
    # assume the job list that loads everything has identifier 1
    run_joblist("1")
    return wait_until_data_loded("1", timeout=datetime.timedelta(hours=16))

def wait_until_data_loded(joblist, timeout=None):
    wait = 60
    start = datetime.datetime.now()
    while joblist_running(joblist):
        print("Waiting for job list %s to complete" % joblist)
        time.sleep(wait)

        delta = datetime.datetime.now() - start
        if timeout is not None and delta > timeout:
            print("Not ready after %ds. Timed out" % delta.seconds)
            return False

    print("Job list %s complete" % joblist)
    return True
