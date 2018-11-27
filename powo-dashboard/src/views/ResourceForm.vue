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
            <v-flex xs12>
              <v-autocomplete label="Skip Resources" chips deletable-chips multiple :items="resourceTypes" v-model="skipped">
              </v-autocomplete>
            </v-flex>
            <v-flex>
              <v-btn color="success" @click="update()">Save <v-icon right dark>save</v-icon></v-btn>
              <v-btn color="warning" @click="editing = false">Cancel <v-icon right dark>cancel</v-icon></v-btn>
            </v-flex>
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
          parameters: { }
        }
      },

      resourceTypes: [
        'Common Names',
        'Descriptions',
        'Distributions',
        'Images',
        'References',
      ],

      skipParameters: {
        'Common Names': ['vernacularName.processing.mode', 'SKIP_VERNACULAR_NAME'],
        'Descriptions': ['description.processing.mode', 'SKIP_DESCRIPTIONS'],
        'Distributions': ['distribution.processing.mode', 'SKIP_DISTRIBUTION'],
        'Images': ['image.processing.mode', 'SKIP_IMAGES'],
        'References': ['reference.processing.mode', 'SKIP_REFERENCES'],
      },
    }
  },

  computed: {
    skipped: {
      // should only show the resourceType associated with any SKIP_* parameters set
      get: function() {
        var vm = this
        return _.keys(
          _.pickBy(vm.skipParameters, function(param) {
            return param[0] in vm.resource.jobConfiguration.parameters
          })
        )
      },

      // sets the processing mode job configuration param associated with a given resourceType
      set: function (skip) {
        var vm = this
        _.map(this.skipParameters, function(param, key) {
          if (_.find(skip, function(item) { return item == key })) {
            vm.$set(vm.resource.jobConfiguration.parameters, param[0], param[1])
          } else {
            vm.$delete(vm.resource.jobConfiguration.parameters, param[0])
          }
        })
      }
    },

    prefix: {
      get: function() {
        return this.resource.jobConfiguration.parameters.prefix
      },
      set: function(prefix) {
        this.$set(this.resource.jobConfiguration.parameters, 'prefix', prefix)
      }
    },

    url: {
      get: function() {
        return this.resource.jobConfiguration.parameters['authority.uri']
      },
      set: function(url) {
        this.$set(this.resource, 'uri', url)
        this.$set(this.resource.jobConfiguration.parameters, 'authority.uri', url)
      }
    },

    skipIndex: {
      get: function() {
        return this.resource.jobConfiguration.parameters['skip.indexing'] == 'true'
      },
      set: function(val) {
        this.$set(this.resource.jobConfiguration.parameters, 'skip.indexing', val)
      }
    },

    processingMode: {
      get: function() {
        return this.resource.jobConfiguration.parameters['taxon.processing.mode']
      },
      set: function (val) {
        this.$set(this.resource.jobConfiguration.parameters, 'taxon.processing.mode', val)
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

