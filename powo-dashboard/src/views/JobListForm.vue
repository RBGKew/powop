<template>
  <v-dialog v-model="editing" max-width="800px">
    <v-card>
      <v-card-title>
        <h1>{{ jobList.newInstance ? 'New' : 'Edit' }} Job List</h1>
      </v-card-title>
      <v-card-text>
        <v-container>
          <v-layout wrap>
            <v-flex xs2>
              <v-text-field v-model="jobList.identifier" label="Identifier" required></v-text-field>
            </v-flex>
            <v-flex xs10>
              <v-text-field v-model="jobList.description" label="Description" required></v-text-field>
            </v-flex>
          </v-layout>
        </v-container>

        <v-data-table
          :items="jobList.jobConfigurations"
          :headers="headers"
          :rows-per-page-items="perPage"
          :pagination.sync="pagination">
        <template slot="items" slot-scope="props">
          <tr>
            <td width="10%">{{ offset + props.index+1 }}</td>
            <td>{{ props.item.description }}</td>
            <td width="30%">
              <v-btn class="py-0" :disabled=!isAuthenticated icon @click.native.stop="moveUp(props.index)">
                <v-icon>keyboard_arrow_up</v-icon>
              </v-btn>
              <v-btn class="py-0" :disabled=!isAuthenticated icon @click.native.stop="moveDown(props.index)">
                <v-icon>keyboard_arrow_down</v-icon>
              </v-btn>
              <v-btn class="py-0" :disabled=!isAuthenticated icon @click.native.stop="deleteJob(props.index)">
                <v-icon color="red">delete</v-icon>
              </v-btn>
            </td>
          </tr>
        </template>
        <template slot="footer">
          <td colspan="100%">
            <v-container>
              <v-layout wrap>
                <v-flex xs9>
                  <v-autocomplete
                    v-model="model"
                    :items="unusedJobs"
                    :search-input.sync="search"
                    item-text="description"
                    item-value="identifier"
                    label="Available Jobs"
                    placeholder="Add new job to list"
                    hide-no-data
                    hide-selected
                    return-object
                    >
                  </v-autocomplete>
                </v-flex>
                <v-flex xs2>
                  <v-btn color="success" class="py-0" :disabled=!isAuthenticated @click="addJob()">
                    <v-icon left>add</v-icon>Add Job
                  </v-btn>
                </v-flex>
              </v-layout>
            </v-container>
          </td>
        </template>
        </v-data-table>
        <v-btn color="success" @click="update()">Save <v-icon right dark>save</v-icon></v-btn>
        <v-btn color="warning" @click="editing = false">Cancel <v-icon right dark>cancel</v-icon></v-btn>
      </v-card-text>
    </v-card>
  </v-dialog>
</template>

<script>
import _ from 'lodash'
import Api from '../lib/Api'
export default {
  name: 'JobListForm',
  data () {
    return {
      editing: false,
      model: null,
      search: null,
      isLoading: false,
      jobs: [],
      jobList: {
        newInstance: true
      },
      headers: [
        { text: 'Order', value: '', sortable: false },
        { text: 'Job', value: 'identifier', sortable: false },
        { text: 'actions', value: '', sortable: false },
      ],
      perPage: [
        8, 25, {text: 'All', value: -1}
      ],
      pagination: {}
    }
  },

  watch: {
    search () {
      if (this.jobs.length > 0) return
      if (this.isLoading) return

      this.isLoading = true

      Api.jobs()
        .then(res => { 
          this.jobs = res.data.results
        })
        .catch(function(err) {
          // eslint-disable-next-line
          console.log(err)
        })
        .finally(() => {
          this.isLoading = false
        })
    }
  },

  methods: {
    show (identifier) {
      this.jobList = _.cloneDeep(this.$store.getters.jobList(identifier))
        || { newInstance: true, jobConfigurations: [] }
      this.editing = true
    },

    addJob () {
      this.jobList.jobConfigurations.push(this.model)
      this.model = null
      this.search = null
      this.pagination.page = this.pages
    },

    moveDown (index) {
      index = this.offset + index
      var jobs = this.jobList.jobConfigurations
      if (index < jobs.length-1) {
        jobs.splice(index, 2, jobs[index+1], jobs[index])
        if (this.offset+this.pagination.rowsPerPage-1 === index) {
          this.pagination.page = this.pagination.page + 1
        }
      }
    },

    moveUp (index) { 
      index = this.offset + index
      var jobs = this.jobList.jobConfigurations
      if (index > 0) {
        this.jobList.jobConfigurations.splice(index-1, 2, jobs[index], jobs[index-1])
        if (this.offset > index-1) {
          this.pagination.page = this.pagination.page - 1
        }
      }
    },

    deleteJob (index) {
      index = this.offset + index
      this.jobList.jobConfigurations.splice(index, 1)
    },

    update () {
      if (this.jobList.newInstance) {
        this.$store.dispatch('createJobList', this.jobList)
      } else {
        this.$store.dispatch('updateJobList', this.jobList)
      }

      this.editing = false
    }
  },

  computed: {
    isAuthenticated () {
      return this.$store.getters.isAuthenticated
    },

    offset () {
      return this.pagination.rowsPerPage * (this.pagination.page-1)
    },

    pages () {
      if (this.pagination.rowsPerPage == null || this.jobList.jobConfigurations == null)
        return 0
      return Math.ceil(this.jobList.jobConfigurations.length / this.pagination.rowsPerPage)
    },

    unusedJobs () {
      return _.differenceBy(this.jobs, this.jobList.jobConfigurations, 'id')
    }
  }
}
</script>
