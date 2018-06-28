<template>
  <v-container>
    <v-card>
      <v-card-title>
        <h2>Organisations</h2>
        <v-spacer/>
        <v-text-field v-model="search" append-icon="search" label="Search" hide-details></v-text-field>
      </v-card-title>
      <v-data-table
        :headers="headers"
        :items="organisations"
        :search="search"
        :rows-per-page-items="pagination"
        disable-initial-sort>
        <template slot="items" slot-scope="props">
          <td class="py-3 org">{{ props.item.title }}</td>
          <td class="py-3">{{ props.item.abbreviation }}</td>
          <td class="py-3">{{ props.item.description }}</td>
          <td class="justify-center layout">
            <v-btn class="py-0" icon @click="go(props.item.identifier)">
              <v-icon color="blue">edit</v-icon>
            </v-btn>
            <v-btn class="py-0" icon @click="confirmDelete(props.item)">
              <v-icon color="red">delete</v-icon>
            </v-btn>
          </td>
        </template>
        <v-alert slot="no-results" :value="true" color="error" icon="warning">
          No organisations called "{{ search }}"
        </v-alert>
        <template slot="footer">
          <v-btn color="success" @click.native.stop="newOrganisation()">
            <v-icon left>add</v-icon>Add Organisation
          </v-btn>
        </template>
      </v-data-table>
      <organisation-form ref="organisationForm"></organisation-form>
      <v-dialog v-model="dialog" max-width="350">
        <v-card>
          <v-card-title class="headline">Really delete {{ active.title }}?</v-card-title>
          <v-card-text>This is irriversible and will delete all associated resources, jobs, and data</v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="info" @click.native="dialog = false">Cancel</v-btn>
            <v-btn color="error" @click.native="deleteOrganisation(active)">Delete</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-card>
  </v-container>
</template>

<script>
import OrganisationForm from '@/views/OrganisationForm'
export default {
  name: 'Home',
  components: { OrganisationForm },
  data () {
    return {
      search: '',
      dialog: false,
      active: '',
      message: {},
      headers: [
        { text: 'Organisation', value: 'title' },
        { text: 'Abbreviation', value: 'abbreviation' },
        { text: 'Description', value: 'description', sortable: false },
        { text: 'Action', value: 'name', sortable: false },
      ],
      pagination: [
        10, 25, 50, {text: 'All', value: -1}
      ]
    }
  },

  computed: {
    organisations () {
      return this.$store.state.organisations
    }
  },

  methods: {
    go (identifier) {
      this.$router.push({ path: `organisation/${identifier}` })
    },

    confirmDelete (organisation) {
      this.active = organisation
      this.dialog = true
    },

    deleteOrganisation (organisation) {
      this.$store.dispatch('deleteOrganisation', organisation)
        .then(function(response) {
          this.message.text = response.title + ' successfully deleted'
          this.message.color = 'success'
        })
        .catch(function(error) {
          this.message.text = 'Error deleting ' + organisation.title + '. ' + error.messsage
          this.message.color = 'error'
        })
    },

    newOrganisation () {
      this.$refs.organisationForm.show()
    }
  }
}
</script>

<style scoped>
.org {
  width: 25%;
}

td {
  vertical-align: top;
}
</style>
