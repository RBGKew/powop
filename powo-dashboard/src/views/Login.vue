<template>
  <v-toolbar-items>
    <v-menu :close-on-content-click="false" offset-y offset-x id="loginMenu" v-model="menu">
    <v-btn flat ripple slot="activator" v-show=!isAuthenticated>Login</v-btn>
      <v-text-field label="Username:" v-model="username"></v-text-field>
      <v-text-field label="Password:" v-model="password" :type="false ? 'text' : 'password'"></v-text-field>
      <v-btn flat ripple @click.native="login">Log in</v-btn>
    </v-menu>
    <v-btn flat ripple @click.native="logout" v-show=isAuthenticated>Logout</v-btn>
  </v-toolbar-items>
</template>
<style>
.v-menu__content {
  background: white !important;
  padding: 10px !important;
}
</style>
<script>
import { mapGetters } from 'vuex'
  export default {
    name: "Login",
    data() {
      return {
        username: "",
        password: "",
        menu: false
      };
    },
    methods: {
      login () {
        this.$store.dispatch("setCredentials", {
          "auth": {
            "username": this.username,
            "password": this.password
          }
       })
       this.menu = false
     },
      logout() {
        this.$store.dispatch("unsetCredentials")
        this.username = ""
        this.password = ""
      }
    },
    computed: {
      ...mapGetters(['isAuthenticated',])
    }
  }
</script>
