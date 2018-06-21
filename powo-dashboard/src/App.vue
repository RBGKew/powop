<template>
  <v-app>
    <v-toolbar>
      <v-toolbar-title>Plants of the World Online</v-toolbar-title>
      <v-spacer />
      <v-toolbar-items>
        <v-btn flat ripple>Organisations</v-btn>
        <v-btn flat ripple>Jobs</v-btn>
        <v-btn flat ripple>Login</v-btn>
      </v-toolbar-items>
    </v-toolbar>
    <v-content>
      <router-view/>
    </v-content>
  </v-app>
</template>

<script>
export default {
  name: 'App',
  beforeMount: function() {
    this.$store.dispatch('initialize')
  }
}
</script>
