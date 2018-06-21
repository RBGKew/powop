import Vue from 'vue'
import Router from 'vue-router'
import Home from './views/Home.vue'
import Organisation from './views/Organisation.vue'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'home',
      component: Home
    },
    {
      path: '/organisation/:identifier',
      name: 'organisation',
      component: Organisation
    }
  ]
})
