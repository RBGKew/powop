import Vue from 'vue'
import Vuex from 'vuex'
import _ from 'lodash'
import api from './lib/Api'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    organisations: [],
    jobs: [],
    lists: [],
    messages: [],
    credentials: {}
  },

  mutations: {
    initialize (state, initial) {
      state.organisations = initial
    },

    createOrganisation (state, organisation) {
      state.organisations.push(organisation)
    },

    updateOrganisation (state, organisation) {
      var index = _.findIndex(state.organisations, {'identifier': organisation.identifier})
      if (index > -1) {
        state.organisations.splice(index, 1, organisation)
      }
    },

    deleteOrganisation (state, organisation) {
      var index = _.findIndex(state.organisations, {'identifier': organisation.identifier})
      if (index > -1) {
        state.organisations.splice(index, 1)
        _.each(organisation.resources, function(resource) {
          this.deleteResource(resource)
        })
      }
    },

    createResource (state, resource) {
      var organisation = this.getters.organisation(resource.organisation)
      organisation.resources.push(resource)
    },

    updateResource (state, resource) {
      var organisation = this.getters.organisation(resource.organisation)
      var index = _.findIndex(organisation.resources, {'identifier': resource.identifier})
      if (index > -1) {
        organisation.resources.splice(index, 1, resource)
      }
    },

    deleteResource (state, resource) {
      var organisation = this.getters.organisation(resource.organisation)
      var index = _.findIndex(organisation.resources, {'identifier': resource.identifier})
      if (index > -1) {
        organisation.resources.splice(index, 1)
      }
    },

    loadJobs (state, jobs) {
      state.jobs = jobs
    },

    updateJob (state, job) {
      var index = _.findIndex(state.jobs, {'identifier': job.identifier})
      if (index > -1) {
        state.jobs.splice(index, 1, job)
      }
    },

    deleteJob (state, job) {
      var index = _.findIndex(state.jobs, {'identifier': job.identifier})
      if (index > -1) {
        state.jobs.splice(index, 1)
      }
    },

    loadJobLists (state, lists) {
      state.lists = lists
    },

    updateJobList (state, list) {
      var index = _.findIndex(state.lists, {'identifier': list.identifier})
      if(index > -1) {
        state.lists.splice(index, 1, list)
      }
    },

    deleteJobList (state, list) {
      var index = _.findIndex(state.lists, {'identifier': list.identifier})
      if(index > -1) {
        state.lists.splice(index, 1)
      }
    },

    addMessage (state, message) {
      state.messages.push(_.merge(message, {
        visible: true,
        timeout: 5000,
        top: true
      }))
    },

    successMessage (state, message) {
      state.messages.push({
        visible: true,
        timeout: 5000,
        top: true,
        text: message,
        color: 'success'
      })
    },

    errorMessage (state, message) {
      state.messages.push({
        visible: true,
        timeout: 5000,
        top: true,
        text: message,
        color: 'error'
      })
    },

    setCredentials (state, credentials) {
      state.credentials = credentials
    }
  },

  actions: {
    initialize (context) {
      return api.organisations()
        .then(function(result) {
          context.commit('initialize', result.data)
          return result
        })
    },

    getOrganisation (context, identifier) {
      return api.organisation(identifier)
        .then(function(result) {
          context.commit('updateOrganisation', result.data)
          return result
        })
    },

    createOrganisation (context, organisation) {
      return api.createOrganisation(organisation, context.state.credentials)
        .then(function(result) {
          context.commit('createOrganisation', result.data)
          context.commit('successMessage', 'Created ' + organisation.title)
          return result
        })
        .catch(function(error) {
          if(error.status === 401) {
            context.commit('errorMessage', 'Unauthorised. Please check your username and password.')
          } else {
            context.commit('errorMessage', 'Error creating ' + organisation.title + '. ' + error.message)
            throw error
          }
        })
    },

    updateOrganisation (context, organisation) {
      return api.updateOrganisation(organisation, context.state.credentials)
        .then(function(result) {
          context.commit('updateOrganisation', result.data)
          context.commit('successMessage', 'Updated ' + organisation.title)
          return result
        })
        .catch(function(error) {
          if(error.status === 401) {
            context.commit('errorMessage', 'Unauthorised. Please check your username and password.')
          } else {
          context.commit('errorMessage', 'Error updating ' + organisation.title + '. ' + error.message)
          throw error
          }
        })
    },

    deleteOrganisation (context, organisation) {
      return api.deleteOrganisation(organisation.identifier, context.state.credentials)
        .then(function(result) {
          context.commit('deleteOrganisation', organisation)
          context.commit('successMessage', 'Deleted ' + organisation.title)
          return result
        })
        .catch(function(error) {
            if(error.status === 401) {
              context.commit('errorMessage', 'Unauthorised. Please check your username and password.')
            }
        })
    },

    createResource (context, resource) {
      return api.createResource(resource, context.state.credentials)
        .then(function(result) {
          context.commit('createResource', result.data)
          context.commit('successMessage', 'Created ' + resource.title)
          return result
        })
        .catch(function(error) {
            if(error.status === 401) {
              context.commit('errorMessage', 'Unauthorised. Please check your username and password.')
            }
        })
    },

    updateResource (context, resource) {
      return api.updateResource(resource, context.state.credentials)
        .then(function(result) {
          context.commit('updateResource', result.data)
          context.commit('successMessage', 'Updated ' + resource.title)
          return result
        })
        .catch(function(error) {
            if(error.status === 401) {
              context.commit('errorMessage', 'Unauthorised. Please check your username and password.')
            }
        })
    },

    deleteResource (context, resource) {
      return api.deleteResource(resource.identifier, context.state.credentials)
        .then(function(result) {
          context.commit('deleteResource', resource)
          context.commit('successMessage', 'Deleted ' + resource.title)
          return result
        })
        .catch(function(error) {
          if(error.status === 401) {
            context.commit('errorMessage', 'Unauthorised. Please check your username and password.')
          } else {
            context.commit('errorMessage', 'Error deleting ' + resource.title + '. ' + error.message)
          throw error
          }
        })
    },

    loadJobs (context) {
      return api.jobs()
        .then(function(result) {
          context.commit('loadJobs', result.data.results)
          return result
        })
    },

    updateJob (context, job) {
      return api.updateJob(job, context.state.credentials)
        .then(function(result) {
          context.commit('updateJob', result.data)
          context.commit('successMessage', 'Updated job ' + job.title)
          return result
        })
        .catch(function(error) {
          if(error.status === 401) {
            context.commit('errorMessage', 'Unauthorised. Please check your username and password.')
          }
        })
    },

    loadJobLists (context) {
      return api.joblists()
        .then(function(result) {
          context.commit('loadJobLists', result.data.results)
          return result
        })
    },

    updateJobList (context, list) {
      return api.updateJobList(list, context.state.credentials)
        .then(function(result) {
          context.commit('updateJobList', result.data)
          context.commit('successMessage', 'Updated job list ' + list.description)
          return result
        })
        .catch(function(error) {
            if(error.status === 401) {
              context.commit('errorMessage', 'Unauthorised. Please check your username and password.')
            } else {
              context.commit('errorMessage', 'Error updating ' + list.description + '. ' + error.message)
              throw error
            }
        })
    },

    deleteJobList (context, list) {
      return api.deleteJobList(list.identifier, context.state.credentials)
        .then(function(result) {
          context.commit('deleteJobList', list)
          context.commit('successMessage', 'Deleted ' + list.description)
          return result
        })
        .catch(function(error) {
          if(error.status === 401) {
            context.commit('errorMessage', 'Unauthorised. Please check your username and password.')
          } else {
            context.commit('errorMessage', 'Error deleting ' + list.description + '. ' + error.message)
            throw error
          }
        })
    },

    setCredentials (context, cred) {
      return api.checkCredentials(cred)
      .then(function() {
        context.commit('setCredentials', cred)
        context.commit('successMessage', 'Login Successful')
      })
      .catch(function(error) {
        if(error.status === 401) {
          context.commit('errorMessage', 'Login unsuccessful. Please check your username and password.')
        } else {
          context.commit('errorMessage', 'Something went wrong with your login attempt. Please try again, or contact powop_support@kew.org if this error persists')
        }
      })
    },

    unsetCredentials(context) {
      context.commit('setCredentials', {})
      context.commit('successMessage', 'You are now logged out')
    },

    importConfiguration(context, file) {
      return api.importConfiguration(file, context.state.credentials)
      .then(function(result) {
        if(result.data.error) {
          context.commit('errorMessage', result.data.error)
        } else {
          context.commit('initialize', result.data.organisations)
        }
      })
      .catch(function(error) {
        if(error.status === 401) {
          context.commit('errorMessage', 'Login unsuccessful. Please check your username and password.')
        } else {
          context.commit('errorMessage', 'Error loading configuration: ' + error)
        }
      })
    }
  },

  getters: {
    job: (state) => (identifier) => {
      return state.jobs.find(job => job.identifier === identifier)
    },

    jobList: (state) => (identifier) => {
      return _.find(state.lists, list => list.identifier == identifier)
    },

    organisation: (state) => (identifier) => {
      return _.find(state.organisations, org => org.identifier === identifier)
    },

    running: (state) => {
      return _.filter(state.jobs, job => job.jobStatus && job.jobStatus != 'COMPLETED')
    },

    visibleMessages: (state) => {
      return _.filter(state.messages, 'visible')
    },

    isAuthenticated: (state) => {
      return !_.isEmpty(state.credentials)
    }

  }
})
