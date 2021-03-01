import Vue from 'vue'
import Router from 'vue-router'
import Home from './views/Home.vue'
import Organisation from './views/Organisation.vue'
import Jobs from './views/Jobs.vue'
import JobLists from './views/JobLists.vue'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'home',
      component: Home
    },
    {
      path: '/organisations',
      name: 'organisations',
      component: Home
    },
    {
      path: '/organisation/:identifier',
      name: 'organisation',
      component: Organisation
    },
    {
      path: '/jobs',
      name: 'jobs',
      component: Jobs
    },
    {
      path: '/lists',
      name: 'lists',
      component: JobLists
    }
  ]
})
