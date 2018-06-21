<template>
  <div>
    <v-container>
      <v-dialog v-model="editing" max-width="800px">
        <v-card>
          <v-card-title><h1>Edit Organisation</h1></v-card-title>
          <v-card-text>
            <v-container grid-list-md>
              <v-layout wrap>
                <v-flex xs10>
                  <v-text-field label="Title" v-model="organisation.title"></v-text-field>
                </v-flex>
                <v-flex xs2>
                  <v-text-field label="Abbreviation" v-model="organisation.abbreviation"></v-text-field>
                </v-flex>
                <v-flex xs12>
                  <v-text-field label="Description" v-model="organisation.description" multi-line></v-text-field>
                </v-flex>
                <v-flex xs12>
                  <v-text-field label="Bibliographic Citation" v-model="organisation.bibliographicCitation" multi-line></v-text-field>
                </v-flex>
                <v-flex xs12>
                  <v-select label="Subjects" v-model="subjects" chips deletable-chips tags flat append-icon=""></v-select>
                </v-flex>
                <v-btn color="success">Save <v-icon right dark>save</v-icon></v-btn>
                <v-btn color="warning" @click="editing = false">Cancel <v-icon right dark>cancel</v-icon></v-btn>
              </v-layout>
            </v-container>
          </v-card-text>
        </v-card>
      </v-dialog>
      <v-card>
        <v-toolbar card prominent>
          <v-toolbar-title>{{ organisation.abbreviation }}: {{ organisation.title }}</v-toolbar-title>
          <v-spacer></v-spacer>
          <v-btn icon @click.native.stop="editing = true">
            <v-icon color="blue">edit</v-icon>
          </v-btn>
          <v-btn icon><v-icon color="red">delete</v-icon></v-btn>
        </v-toolbar>
        <v-card-text>
          <p>{{ organisation.description }}</p>
          <h3 v-if="notEmpty(organisation.bibliographicCitation)">Cite as:</h3>
          <cite>{{ organisation.bibliographicCitation }}</cite>
          <v-spacer class="py-3"></v-spacer>
          <v-chip v-for="subject in subjects" :key="subject" outline color="primary">{{ subject }}</v-chip>
          <v-divider class="my-3"></v-divider>
          <h3>Resources</h3>
          <v-data-table :items="resources" :headers="headers" hide-actions>
            <template slot="items" slot-scope="props">
              <td><h4>{{ props.item.title }}</h4></td>
              <td>{{ props.item.uri }}</td>
              <td class="justify-center layout">
                <v-btn class="py-0" icon>
                  <v-icon color="blue">edit</v-icon>
                </v-btn>
                <v-btn class="py-0" icon>
                  <v-icon color="red">delete</v-icon>
                </v-btn>
              </td>
            </template>
            <template slot="footer">
              <v-btn color="success"><v-icon left>add</v-icon>Add Resource</v-btn>
            </template>
          </v-data-table>
        </v-card-text>
      </v-card>
    </v-container>
  </div>
</template>

<script>
import _ from 'lodash'

export default {
  name: 'Organisation',

  data() {
    return {
      editing: false,
      headers: [
        { text: 'Title', value: 'title' },
        { text: 'URL', value: 'uri' },
        { text: 'Actions', value: '' }
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
      return this.$store.getters.organisation(this.$route.params.identifier)
    },

    subjects () {
      var subject = this.organisation.subject
      return subject ? subject.split(';') : []
    },

    resources () {
      return this.organisation.resources
    }
  },

  methods: {
    notEmpty (str) {
      return !_.isEmpty(str)
    },
  }
}
</script>
