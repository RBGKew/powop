<template>
  <v-container>
    <v-card>
      <v-card-title>
        Organisations
        <v-spacer/>
        <v-text-field v-model="search" append-icon="search" label="Search" hide-details></v-text-field>
      </v-card-title>
      <v-data-table
        :headers="headers"
        :items="organisations"
        :search="search"
        :rows-per-page-items="pagination">
        <template slot="items" slot-scope="props">
          <td class="py-3 org">{{ props.item.title }}</td>
          <td class="py-3">{{ props.item.abbreviation }}</td>
          <td class="py-3">{{ props.item.description }}</td>
          <td class="justify-center layout">
            <v-btn class="py-0" icon @click="go(props.item.identifier)">
              <v-icon color="blue">edit</v-icon>
            </v-btn>
            <v-btn class="py-0" icon>
              <v-icon color="red">delete</v-icon>
            </v-btn>
          </td>
        </template>
        <v-alert slot="no-results" :value="true" color="error" icon="warning">
          No organisations called "{{ search }}"
        </v-alert>
      </v-data-table>
    </v-card>
  </v-container>
</template>

<script>
export default {
  name: 'Home',
  data () {
    return {
      search: '',
      headers: [
        { text: 'Organisation', value: 'title' },
        { text: 'Abbreviation', value: 'abbreviation' },
        { text: 'Description', value: 'description', sortable: false },
        { text: 'Action', value: 'name', sortable: false },
      ],
      pagination: [
        25, 50, {text: 'All', value: -1}
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
      // eslint-disable-next-line
      console.log("navigating to organisation/" + identifier)
      this.$router.push({ path: `organisation/${identifier}` })
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
