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
    running: []
  },

  mutations: {
    initialize(state, initial) {
      state.organisations = initial
    },

    updateOrganisation(state, organisation) {
      var index = _.findIndex(state.organisations, {'identifier': organisation.identifier})
      state.organisations[index] = organisation
    }
  },

  actions: {
    initialize (context) {
      return api.organisations()
      .then(function(result) {
        context.commit('initialize', result.data.results) 
      })
    },

    getOrganisation (context, identifier) {
      return api.organisation(identifier)
      .then(function(result) {
        context.commit('updateOrganisation', result.data)
      })
    }
  },

  getters: {
    organisation: (state) => (identifier) => {
      return state.organisations.find(org => org.identifier === identifier)
    }
  }
})
