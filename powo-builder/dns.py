from kubernetes import client, config
from google.cloud import dns
import time

class Swapper:

    def __init__(self, zone, name, namespace, project = 'powop-1349'):
        self.project = project
        self.dns_client = dns.Client(project=project)
        self.zone = self.dns_client.zone(zone, name)
        self.namespace = namespace

    def _kube_client(self):
        config.load_kube_config()
        return client.CoreV1Api()

    def current_dns_record(self):
        records = self.zone.list_resource_record_sets()
        return [r for r in records if r.record_type == 'A'][0]

    def new_dns_record(self, current):
        service = self._kube_client().list_namespaced_service(self.namespace, label_selector='name=apache').items
        new_ip = service[0].status.load_balancer.ingress[0].ip
        return self.zone.resource_record_set(current.name, current.record_type, current.ttl, [new_ip,])

    def update_dns(self):
        current = self.current_dns_record()
        updated = self.new_dns_record(current)

        print('Current dns: %s' % [current.name, current.record_type, current.ttl, current.rrdatas[0]])
        print('Updating to: %s' % [updated.name, updated.record_type, updated.ttl, updated.rrdatas[0]])

        changes = self.zone.changes()
        changes.add_record_set(updated)
        changes.delete_record_set(current)
        changes.create()

        while changes.status != 'done':
            print('Waiting for dns changes to complete')
            time.sleep(10)
            changes.reload()

        print('Changes complete, waiting for TTL (x 2, just to be safe)')
        time.sleep(updated.ttl * 2)

        return True
