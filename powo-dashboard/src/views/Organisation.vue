<template>
  <v-container>
    <organisation-form ref="organisationForm"></organisation-form>
    <resource-form ref="resourceForm"></resource-form>
    <v-card>
      <v-toolbar card>
        <v-toolbar-title>{{ organisation.abbreviation }}: {{ organisation.title }}</v-toolbar-title>
        <v-spacer></v-spacer>
        <v-btn icon :disabled=!isAuthenticated @click.native.stop="editOrganisation()">
          <v-icon color="blue">edit</v-icon>
        </v-btn>
        <v-btn :disabled=!isAuthenticated icon><v-icon color="red">delete</v-icon></v-btn>
      </v-toolbar>
      <v-card-text>
        <p>{{ organisation.description }}</p>
        <h3 v-if="notEmpty(organisation.bibliographicCitation)">Cite as:</h3>
        <cite>{{ organisation.bibliographicCitation }}</cite>
        <v-spacer class="py-3"></v-spacer>
        <v-chip v-for="subject in subjects" :key="subject" outline color="primary">{{ subject }}</v-chip>
        <v-divider class="my-3"></v-divider>
        <h3>Resources</h3>
        <v-data-table
          :items="resources"
          :headers="headers"
          hide-actions
          disable-initial-sort>
          <template slot="items" slot-scope="props">
            <td><h4>{{ props.item.title }}</h4></td>
            <td>{{ props.item.uri }}</td>
            <td class="justify-center layout">
              <v-btn class="py-0" :disabled=!isAuthenticated icon @click.native.stop="editResource(props.item)">
                <v-icon color="blue">edit</v-icon>
              </v-btn>
              <v-btn class="py-0" :disabled=!isAuthenticated icon @click.native.stop="confirmDelete(props.item)">
                <v-icon color="red">delete</v-icon>
              </v-btn>
            </td>
          </template>
          <template slot="footer">
            <v-btn color="success" :disabled=!isAuthenticated @click.native.stop="newResource(organisation)">
              <v-icon left>add</v-icon>Add Resource
            </v-btn>
          </template>
        </v-data-table>
      </v-card-text>
      <v-dialog v-model="dialog" max-width="350">
        <v-card>
          <v-card-title class="headline">Really delete {{ active.title }}?</v-card-title>
          <v-card-text>This is irriversible and will delete all associated jobs, and data</v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="info" @click.native="dialog = false">Cancel</v-btn>
            <v-btn color="error" @click.native="deleteResource(active)">Delete</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-card>
  </v-container>
</template>

<script>
import _ from 'lodash'
import OrganisationForm from '@/views/OrganisationForm'
import ResourceForm from '@/views/ResourceForm'
import { mapGetters } from 'vuex'
export default {
  name: 'Organisation',
  components: { OrganisationForm, ResourceForm },

  data() {
    return {
      dialog: false,
      active: {},
      headers: [
        { text: 'Title', value: 'title', sortable: false },
        { text: 'URL', value: 'uri', sortable: false },
        { text: 'Actions', value: '', sortable: false }
      ]
    }
  },

  beforeRouteEnter (to, from, next) {
    next(vm => vm.$store.dispatch('getOrganisation', to.params.identifier))
  },

  beforeRouteUpdate (to, from, next) {
    this.$store
      .dispatch('getOrganisation', to.params.identifier)
      .then(next())
  },

  computed: {
    organisation () {
      return this.$store.getters.organisation(this.$route.params.identifier) || {}
    },

    subjects () {
      var subject = this.organisation.subject
      return subject ? subject.split(';') : []
    },

    resources () {
      return this.organisation.resources
    },
    ...mapGetters(['isAuthenticated',])
  },

  methods: {
    notEmpty (str) {
      return !_.isEmpty(str)
    },

    editResource (resource) {
      this.$refs.resourceForm.show(resource)
    },

    editOrganisation () {
      this.$refs.organisationForm.show(this.$route.params.identifier)
    },

    newResource (organisation) {
      var template = {
        newInstance: true,
        organisation: organisation.identifier,
        resourceType: 'DwC_Archive',
        jobConfiguration: {
          jobName: 'DarwinCoreArchiveHarvesting',
          parameters: {
            'authority.name': organisation.identifier,
            'skip.indexing': false,
          }
        }
      }

      this.$refs.resourceForm.show(template);
    },

    confirmDelete (resource) {
      this.active = resource
      this.dialog = true
    },

    deleteResource (resource) {
      this.$store
        .dispatch('deleteResource', resource)
        .then(this.dialog = false)
    }
  }
}
</script>
