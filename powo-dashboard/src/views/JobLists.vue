<template>
  <v-container>
    <job-list-form ref="jobListForm"></job-list-form>
    <v-card>
      <v-card-title>
        <h2>Job Lists</h2>
      </v-card-title>
      <v-data-table
        :headers="headers"
        :items="jobLists">
        <template slot="items" slot-scope="props">
          <tr>
            <td width="5%">{{ props.item.identifier }}</td>
            <td>{{ props.item.description }}</td>
            <td>{{ props.item.jobConfigurations[props.item.currentJob].description }}</td>
            <td>{{ props.item.status }}</td>
            <td width="20%">
              <v-btn class="py-0" icon :disabled=!isAuthenticated @click="run(props.item)">
                <v-icon color="green">play_circle_outline</v-icon>
              </v-btn>
              <v-btn class="py-0" icon :disabled=!isAuthenticated @click.native.stop="edit(props.item.identifier)">
                <v-icon color="green">edit</v-icon>
              </v-btn>
              <v-btn class="py-0" :disabled=!isAuthenticated icon @click.native.stop="confirmDelete(props.item)">
                <v-icon color="red">delete</v-icon>
              </v-btn>
            </td>
          </tr>
        </template>
        <template slot="footer">
          <td colspan="100%">
          <v-btn color="success" :disabled=!isAuthenticated @click.native.stop="newJobList()">
            <v-icon left>add</v-icon>Add Job List
          </v-btn>
          </td>
        </template>
      </v-data-table>
      <delete-dialog
        text="Are you sure you want to delete this job list?"
        :fn="deleteJobList"
        ref="deleteDialog"></delete-dialog>
    </v-card>
  </v-container>
</template>

<script>
import JobListForm from '@/views/JobListForm'
import DeleteDialog from '@/views/DeleteDialog'
import api from '@/lib/Api'

export default {
  name: 'JobLists',
  components: {
    DeleteDialog,
    JobListForm
  },

  data () {
    return {
      headers: [
        { text: 'Identifier', value: 'identifier', sortable: false },
        { text: 'Description', value: 'description' },
        { text: 'Current Job', value: 'currentJob' },
        { text: 'Status', value: 'status' },
        { text: 'Actions', value: '', sortable: false }
      ],
      dialog: false
    }
  },

  beforeRouteEnter (to, from, next) {
    next(vm => vm.$store.dispatch('loadJobLists'))
  },

  computed: {
    jobLists () {
      return this.$store.state.lists
    },

    isAuthenticated () {
      return this.$store.getters.isAuthenticated
    }
  },

  methods: {
    run (jobList) {
      api.runJobList(jobList.identifier, this.$store.state.credentials)
        .then(() => {
          this.$store.commit('successMessage', 'Started running "' + jobList.description + '"')
        })
        .catch((error) => {
          if (error.status === 401) {
            this.$store.commit('errorMessage', 'Unauthorised. Please check your username and password.')
          } else {
            this.$store.commit('errorMessage', 'Error running ' + jobList.description + '. ' + error.message)
            throw error
          }
        })
    },

    edit (jobList) {
      this.$refs.jobListForm.show(jobList)
    },

    confirmDelete (jobList) {
      this.$refs.deleteDialog.show("Delete \"" + jobList.description + "\"?", jobList)
    },

    deleteJobList (jobList) {
      this.$store.dispatch('deleteJobList', jobList)
    },

    newJobList () {
      this.$refs.jobListForm.show()
    }
  }
}
</script>
