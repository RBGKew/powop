import datetime
import requests
import time
import os
import os.path

import logging as log

API_PREFIX = os.getenv('API_PREFIX', 'http://localhost:10080')
API_URL = os.getenv('API_URL', '/harvester/api/1')
API_USERNAME = os.getenv('API_USERNAME', 'admin')
API_PASSWORD = os.getenv('API_PASSWORD', 'password')
ENV = os.getenv('ENVIRONMENT', 'test')

def _api(method):
    return API_PREFIX + API_URL + method

def _data_file():
    path = 'data-%s.json' % ENV
    if os.path.isfile(path):
        return path
    else:
        log.info('Could not find %s. Defaulting to data-test.json' % path)
        return 'data-test.json'

def load_data_config():
    config = open(_data_file(), 'br')
    headers = {'Content-Type': 'application/json; charset=utf-8'}
    r = requests.post(_api('/data'), data=config, headers=headers, auth=(API_USERNAME, API_PASSWORD))
    r.raise_for_status()
    log.info("Data configuration loaded")
    return r.json()

def get_data_config():
    r = requests.get(_api('/data'))
    r.raise_for_status()
    return r.json()

def run_joblist(identifier):
    r = requests.post(_api('/job/list/' + identifier + '/run'), auth=(API_USERNAME, API_PASSWORD))
    log.info("Running job list with identifier %s" % identifier)
    r.raise_for_status()
    return r.json()

def joblist_running(identifier):
    r = requests.get(_api('/job/list/' + identifier))
    r.raise_for_status()
    status = r.json().get('status')
    return status == None or status != 'Completed'

def load_data():
    load_data_config()
    # assume the job list that loads everything has identifier 1
    run_joblist("1")
    return wait_until_data_loded("1", timeout=datetime.timedelta(hours=23))

def wait_until_data_loded(joblist, timeout=None):
    wait = 60
    start = datetime.datetime.now()
    while joblist_running(joblist):
        log.info("Waiting for job list %s to complete" % joblist)
        time.sleep(wait)

        delta = datetime.datetime.now() - start
        if timeout is not None and delta > timeout:
            log.error("Not ready after %ds. Timed out" % delta.seconds)
            return False

    log.warn("Job list %s complete" % joblist)
    return True
