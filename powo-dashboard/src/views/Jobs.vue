<template>
  <v-container>
    <v-card>
      <v-card-title>
        <h2>Jobs</h2>
        <v-spacer/>
        <v-text-field v-model="search" append-icon="search" label="Search" hide-details></v-text-field>
        <v-tooltip bottom>
          <v-btn class="mt-2" slot="activator" icon @click="toggleWatchJobs()">
            <v-icon :color="watchingColor">refresh</v-icon>
          </v-btn>
          <span>Watch jobs</span>
        </v-tooltip>
      </v-card-title>
      <v-data-table
        :headers="headers"
        :items="jobs"
        :search="search"
        :rows-per-page-items="pagination"
        disable-initial-sort>
        <template slot="items" slot-scope="props">
          <tr :class="jobColor(props.item)">
            <td class="py-3 org">
              {{ props.item.description }}
            </td>
            <td class="py-3">{{ props.item.jobStatus }}</td>
            <td class="py-3">{{ props.item.jobExitCode }}</td>
            <td>
              <v-progress-circular class="py-2" :indeterminate="isRunning(props.item)" color="primary" :width="progress.width" :size="progress.size">
              <v-btn class="py-0" icon :disabled=!isAuthenticated @click="run(props.item.id)">
                <v-icon color="green">play_circle_outline</v-icon>
              </v-btn>
              </v-progress-circular>
            </td>
          </tr>
        </template>
        <v-alert slot="no-results" :value="true" color="error" icon="warning">
          No Job called "{{ search }}"
        </v-alert>
      </v-data-table>
    </v-card>
  </v-container>
</template>

<script>
import api from '../lib/Api'
import { mapGetters } from 'vuex'
export default {
  name: 'Jobs',

  beforeRouteEnter (to, from, next) {
    next(vm => vm.updateJobs())
  },

  data () {
    return {
      search: '',
      headers: [
        { text: 'Description', value: 'description' },
        { text: 'Status', value: 'jobStatus' },
        { text: 'Exit Code', value: 'jobExitCode' },
        { text: 'Run', value: '', sortable: false },
      ],
      pagination: [
        25, 50, {text: 'All', value: -1}
      ],
      poller: null,
      watching: false,
      interval: 10000,
      progress: {
        width: 2,
        size: 40
      }
    }
  },

  computed: {
    jobs () {
      return this.$store.state.jobs
    },

    running () {
      return this.$store.getters.running
    },

    watchingColor () {
      return this.watching ? 'green' : 'grey'
    },
    ...mapGetters(['isAuthenticated',])
  },

  methods: {
    run (identifier) {
      api.run(identifier)
        .then(function(result) {
          this.$store.commit('updateJob', result.data)
          this.watchRunningJobs()
        }.bind(this))
    },

    isRunning (job) {
      return job.jobStatus &&
        !( job.jobStatus === 'COMPLETED' || job.jobStatus == 'FAILED')
    },

    hasRunning () {
      return this.running.length > 0
    },

    updateJobs () {
      this.$store.dispatch('loadJobs')
    },

    jobColor (job) {
      if(!job.jobStatus) return;
      if(job.jobExitCode === 'FAILED') return 'red--text'
      if(job.jobStatus == 'STARTING' || job.jobStatus == 'STARTED') return 'green--text'
      if(job.jobStatus == 'COMPLETED') return 'teal--text darken-4'
    },

    toggleWatchJobs () {
      if(this.watching) {
        this.unwatchJobs()
      } else {
        this.watchJobs()
      }
    },

    watchJobs () {
      this.watching = true
      this.poller = setInterval(function() {
        this.updateJobs()
      }.bind(this), this.interval)
    },

    watchRunningJobs () {
      this.unwatchJobs()
      this.poller = setInterval(function() {
        this.updateJobs()
        if(!this.hasRunning()) {
          this.unwatchJobs()
        }
      }.bind(this), this.interval)
    },

    unwatchJobs () {
      this.watching = false
      clearInterval(this.poller)
    }
  },

  created: function () {
    this.watchRunningJobs()
  }
}
</script>
