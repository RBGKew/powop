import axios from 'axios'
import _ from 'lodash'

const url = process.env.VUE_APP_HARVESTER_URL
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

const joblists = function() {
  return api.get('job/list')
}

const joblist = function(identifier) {
  return api.get('job/list/' + identifier)
}

const createJobList = function(job, credentials) {
  return api.post('job/list', job, credentials)
}

const updateJobList = function(jobList, credentials) {
  return api.post('job/list/' + jobList.identifier, jobList, credentials)
}

const deleteJobList = function(identifier, credentials) {
  return api.delete('job/list/' + identifier, credentials)
}

const runJobList = function(identifier, credentials) {
  return api.post('job/list/' + identifier + '/run', credentials)
}

const checkCredentials = function(cred) {
  return api.post('login', {}, cred)
}

const exportConfiguration = function() {
  window.location = url + 'data'
}

const importConfiguration = function(file, credentials) {
  var conf = _.merge({headers: {'Content-Type': 'application/json'}}, credentials)
  return api.post('data', file, conf)
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
  joblists,
  joblist,
  createJobList,
  updateJobList,
  deleteJobList,
  runJobList,
  checkCredentials,
  exportConfiguration,
  importConfiguration
}
