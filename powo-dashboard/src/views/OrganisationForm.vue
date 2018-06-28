<template>
  <v-dialog v-model="editing" max-width="800px">
    <v-card>
      <v-card-title><h1>{{ organisation.newInstance ? 'New' : 'Edit' }} Organisation</h1></v-card-title>
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
            <v-btn color="success" @click="update()">Save <v-icon right dark>save</v-icon></v-btn>
            <v-btn color="warning" @click="editing = false">Cancel <v-icon right dark>cancel</v-icon></v-btn>
          </v-layout>
        </v-container>
      </v-card-text>
    </v-card>
  </v-dialog>
</template>

<script>
import _ from 'lodash'
export default {
  name: 'OrganisationForm',
  data () {
    return {
      editing: false,
      organisation: {
        newInstance: true
      }
    }
  },

  computed: {
    subjects: {
      get: function() {
        var subject = this.organisation.subject
        return subject ? subject.split(';') : []
      },
      set: function(subjects) {
        this.organisation.subject = subjects.join(';')
      }
    },
  },

  methods: {
    update () {
      var action = this.organisation.newInstance ? 'createOrganisation' : 'updateOrganisation'
      this.$store.dispatch(action, this.organisation)
        .then(this.editing = false)
    },

    show (identifier) {
      this.organisation = _.cloneDeep(this.$store.getters.organisation(identifier))
        || { newInstance: true }
      this.editing = true
    },
  },

  watch: {
    'organisation.title': function(title) {
      this.organisation.identifier = title.replace(/\W+/g, '-')
    }
  }
}
</script>
