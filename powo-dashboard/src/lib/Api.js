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

const createOrganisation = function(organisation, credentials) {
  return api.post('organisation', organisation, credentials)
}

const updateOrganisation = function(organisation, credentials) {
  return api.post('organisation/' + organisation.identifier, organisation, credentials)
}

const deleteOrganisation = function(identifier, credentials) {
  return api.delete('organisation/' + identifier, {}, credentials)
}

const resources = function () {
  return api.get('resources')
}

const resource = function(identifier) {
  return api.get('resources/' + identifier)
}

const createResource = function(resource, credentials) {
  return api.post('resource/', resource, credentials)
}

const updateResource = function(resource, credentials) {
  return api.post('resource/' + resource.identifier, resource, credentials)
}

const deleteResource = function(identifier, credentials) {
  return api.delete('resource/' + identifier, {}, credentials)
}

const jobs = function() {
  return api.get('job/configuration')
}

const job = function(identifier) {
  return api.get('job/configuration/' + identifier)
}

const updateJob = function(job, credentials) {
  return api.post('job/configuration/' + job.identifier, job, credentials)
}

const deleteJob = function(identifier, credentials) {
  return api.delete('job/configuration/' + identifier, credentials)
}

const run = function(identifier, credentials) {
  return api.post('job/configuration/' + identifier + '/run', {}, credentials)
}

const checkCredentials = function(cred) {
  return api.post('login', {}, cred)
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
  run,
  checkCredentials
}
