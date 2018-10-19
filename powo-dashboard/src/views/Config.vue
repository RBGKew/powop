<template>
  <v-toolbar-items>
    <v-menu :close-on-content-click="false" offset-y left id="configMenu" v-model="menu">
      <v-btn flat ripple slot="activator">
        <v-icon>settings</v-icon>
      </v-btn>
      <v-container>
        <div v-if="isAuthenticated">
          <v-subheader>Configuration</v-subheader>
          <input type="file" id="select-config-file" @change="importConfiguration" />
          <v-btn flat @click="exportConfiguration">Export</v-btn>
          <v-btn flat @click="showSelectDialog">Import</v-btn>
          <v-divider/>
        </div>
        <div v-if="!isAuthenticated">
          <v-text-field label="Username:" @keyup.enter="login" v-model="username"></v-text-field>
          <v-text-field label="Password:" @keyup.enter="login" v-model="password" type="password"></v-text-field>
        </div>
        <v-btn flat @click.native="login" v-if="!isAuthenticated">Log in</v-btn>
        <v-btn flat @click.native="logout" v-if="isAuthenticated">Log out</v-btn>
      </v-container>
    </v-menu>
  </v-toolbar-items>
</template>

<script>
import api from '@/lib/Api'

export default {
  name: 'Config',
  data () {
    return {
      username: '',
      password: '',
      menu: false
    }
  },

  methods: {
    login () {
      this.$store.dispatch('setCredentials', {
        'auth': {
          'username': this.username,
          'password': this.password
        }
      })
      this.menu = false
    },

    logout () {
      this.$store.dispatch('unsetCredentials')
      this.username = null
      this.password = null
    },

    exportConfiguration () {
      api.exportConfiguration()
    },

    showSelectDialog () {
      document.getElementById('select-config-file').click()
    },

    importConfiguration (e) {
      var file = e.target.files[0]
      var reader = new FileReader()
      reader.onload = () => {
        this.$store.dispatch('importConfiguration', reader.result)
      }
      reader.readAsText(file)
    }
  },

  computed: {
    isAuthenticated () {
      return this.$store.getters.isAuthenticated
    }
  }
}
</script>

<style>
#select-config-file {
  display: none;
}

.v-menu__content {
  background-color: white;
}
</style>
