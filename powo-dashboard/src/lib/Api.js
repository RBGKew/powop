import axios from 'axios'

const url = 'http://localhost:10080/harvester/api/1/'
const api = axios.create({
  baseURL: url
})

const organisations = function() {
  return api.get('organisation')
}

const organisation = function(identifier) {
  return api.get('organisation/' + identifier)
}

const createOrganisation = function(organisation) {
  return api.post('organisation', organisation)
}

const updateOrganisation = function(organisation) {
  return api.post('organisation/' + organisation.identifier, organisation)
}

const deleteOrganisation = function(identifier) {
  return api.delete('organisation/' + identifier)
}

const resources = function () {
  return api.get('resources')
}

const resource = function(identifier) {
  return api.get('resources/' + identifier)
}

const createResource = function(resource) {
  return api.post('resource/', resource)
}

const updateResource = function(resource) {
  return api.post('resource/' + resource.identifier, resource)
}

const deleteResource = function(identifier) {
  return api.delete('resource/' + identifier)
}

const jobs = function() {
  return api.get('job/configuration')
}

const job = function(identifier) {
  return api.get('job/configuration/' + identifier)
}

const updateJob = function(job) {
  return api.post('job/configuration/' + job.identifier, job)
}

const deleteJob = function(identifier) {
  return api.delete('job/configuration/' + identifier)
}

const run = function(identifier) {
  return api.post('job/configuration/' + identifier + '/run')
}

export default {
  organisations,
  organisation,
  createOrganisation,
  updateOrganisation,
  deleteOrganisation,
  resources,
  resource,
  createResource,
  updateResource,
  deleteResource,
  jobs,
  job,
  updateJob,
  deleteJob,
  run
}
