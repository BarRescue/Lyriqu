<template>
  <v-row>
    <v-col v-for="song in songs" :key="song.id"
      cols="12"
      sm="4"
      lg="2"
    >
      <div class="inner pa-3" style="background-color: #313131; border-radius: 10px; position: relative;">
        <v-img :src="song.thumbnail" style="border-radius: 10px;" height="200" :lazy-src="song.thumbnail"/>
        <v-btn
          class="mx-2"
          fab
          dark
          small
          color="primary"
          absolute
          style="bottom: 15px; left: 10px;"
          @click="streamSong(song.path)"
        >
          <v-icon dark medium>
            mdi-play
          </v-icon>
        </v-btn>
      </div>
    </v-col>
  </v-row>
</template>

<script>
export default {
  name: "Songs",
  props: {
    songs: {
      type: Array
    }
  },
  methods: {
    streamSong(path) {
      console.log(path);
      this.$store.commit('stream/newStream', {endpoint: path})
    }
  }
}
</script>

<style scoped>

</style>
