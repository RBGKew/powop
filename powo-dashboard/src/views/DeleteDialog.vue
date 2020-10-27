<template>
  <v-dialog v-model="visible" max-width="350">
    <v-card>
      <v-card-title class="headline">{{ title }}</v-card-title>
      <v-card-text>{{ text }}</v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="info" @click.native="visible = false">Cancel</v-btn>
        <v-btn color="error" @click.native="confirm">Delete</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
export default {
  name: 'DeleteDialog',

  props: {
    title: { type: String },
    text: { type: String, required: true },
    fn: { type: Function, required: true },
    obj: { type: Function }
  },

  data () {
    return {
      visible: false
    }
  },

  methods: {
    show (title, obj) {
      this.title = title
      this.obj = obj
      this.visible = true
    },

    confirm () {
      this.fn(this.obj)
      this.visible = false
    }
  }
}
</script>
