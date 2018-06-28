<template>
  <v-dialog v-model="editing" max-width="800px">
    <v-card>
      <v-card-title><h1>{{ resource.newInstance ? 'New' : 'Edit' }} Resource</h1></v-card-title>
      <v-card-text>
        <v-container grid-list-md>
          <v-layout wrap>
            <v-flex xs12>
              <v-text-field label="Title" v-model="resource.title"></v-text-field>
            </v-flex>
            <v-flex xs12>
              <v-text-field label="URL" v-model="url"></v-text-field>
            </v-flex>
            <v-flex xs12>
              <v-text-field v-model="prefix" label="Image prefix"></v-text-field>
            </v-flex>
            <v-flex xs4>
              <v-checkbox v-model="skipIndex" label="Skip indexing"></v-checkbox>
            </v-flex>
            <v-flex xs8>
              <v-radio-group v-model="processingMode" row class="pt-0">
                <v-radio label="Harvest names" value="IMPORT_NAMES"></v-radio>
                <v-radio label="Harvest taxonomy" value="IMPORT_TAXONOMY"></v-radio>
              </v-radio-group>
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
  name: 'ResourceForm',
  data () {
    return {
      editing: false,
      resource: {
        newInstance: true,
        jobConfiguration: {
          parameters: {
            'prefix': null
          }
        }
      },
      resourceTypes: [
        { text: 'Standard', value: 'Harvest' },
        { text: 'Harvest Names', value: 'HarvestNames' },
        { text: 'Harvest Taxonomy', value: 'HarvestTaxonomy' }
      ]
    }
  },

  computed: {

    prefix: {
      get: function() {
        return this.resource.jobConfiguration.parameters.prefix
      },
      set: function(prefix) {
        this.resource.jobConfiguration.parameters.prefix = prefix
      }
    },

    url: {
      get: function() {
        return this.resource.uri
      },
      set: function(url) {
        this.resource.uri = url
        this.resource.jobConfiguration.parameters['authority.uri'] = url
      }
    },

    skipIndex: {
      get: function() {
        return this.resource.jobConfiguration.parameters['skip.indexing']
      },
      set: function(val) {
        this.resource.jobConfiguration.parameters['skip.indexing'] = val
      }
    },

    processingMode: {
      get: function() {
        return this.resource.jobConfiguration.parameters['taxon.processing.mode']
      },
      set: function (val) {
        this.resource.jobConfiguration.parameters['taxon.processing.mode'] = val
      }
    },
  },

  methods: {
    update () {
      var action = this.resource.newInstance ? 'createResource' : 'updateResource'
      this.$store.dispatch(action, this.resource)
        .then(this.editing = false)
    },

    show (resource) {
      this.resource = _.cloneDeep(resource)
      this.editing = true
    },
  },

  watch: {
    'resource.title': function(title) {
      if (title) {
        this.resource.identifier = title.replace(/\W+/g, '-')
        this.resource.jobConfiguration.description = 'Harvest ' + title
        this.resource.jobConfiguration.identifier = 'Harvest-' + this.resource.identifier
        this.resource.jobConfiguration.parameters['resource.identifier'] = this.resource.identifier
      }
    }
  }
}
</script>

