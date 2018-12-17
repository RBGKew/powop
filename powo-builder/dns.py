from kubernetes import client, config
from google.cloud import dns
import os
import time
import yaml
import logging as log

def update(namespace):
    with open('/config/dns-mappings.yaml', 'r') as yamlfile:
        cfg = yaml.load(yamlfile)
    for mapping in cfg['dns']:
        if not Swapper(mapping, namespace).update_dns():
            return False
    return True

class Swapper:

    def __init__(self, mapping, namespace):
        self.project = os.environ['G_PROJECT']
        self.mapping = mapping
        self.dns_client = dns.Client(project=self.project)
        self.zone = self.dns_client.zone(mapping['zone'], mapping['name'])
        self.namespace = namespace

    def _kube_client(self):
        config.load_kube_config()
        return client.CoreV1Api()

    def current_dns_record(self):
        records = self.zone.list_resource_record_sets()
        for record in records:
            if record.record_type == 'A' and record.name == self.mapping['name']:
                return record

    def new_dns_record(self, current):
        selector = 'name=%s' % self.mapping['service']
        service = self._kube_client().list_namespaced_service(self.namespace, label_selector=selector).items
        new_ip = service[0].status.load_balancer.ingress[0].ip
        return self.zone.resource_record_set(current.name, current.record_type, current.ttl, [new_ip,])

    def update_dns(self):
        current = self.current_dns_record()
        updated = self.new_dns_record(current)

        log.info('Current dns: %s' % [current.name, current.record_type, current.ttl, current.rrdatas[0]])
        log.info('Updating to: %s' % [updated.name, updated.record_type, updated.ttl, updated.rrdatas[0]])

        changes = self.zone.changes()
        changes.add_record_set(updated)
        changes.delete_record_set(current)
        changes.create()

        while changes.status != 'done':
            log.info('Waiting for dns changes to complete')
            time.sleep(10)
            changes.reload()

        log.info('Changes complete, waiting for TTL (x 2, just to be safe)')
        time.sleep(updated.ttl * 2)

        return True
