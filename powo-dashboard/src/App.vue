<template>
  <v-app>
    <v-snackbar
      v-for="message in messages"
      :timeout="message.timeout"
      :top="message.top"
      :color="message.color"
      :key="message.index"
      v-model="message.visible">
      {{ message.text }}
    </v-snackbar>
    <v-toolbar>
      <v-toolbar-title>Plants of the World Online</v-toolbar-title>
      <v-spacer />
      <v-toolbar-items>
        <v-btn flat ripple @click="$router.push('/organisations')">Organisations</v-btn>
        <v-btn flat ripple @click="$router.push('/jobs')">Jobs</v-btn>
        <v-btn flat ripple @click="$router.push('/lists')">Job Lists</v-btn>
        <config></config>
      </v-toolbar-items>
    </v-toolbar>
    <v-content>
      <router-view/>
    </v-content>
  </v-app>
</template>

<script>
import Config from '@/views/Config'
export default {
  name: 'App',
  components:  { Config },

  beforeMount: function() {
    this.$store.dispatch('initialize')
  },

  computed: {
    messages() {
      return this.$store.getters.visibleMessages
    }
  }
}
</script>
