<template>
  <div>
    <v-data-table
      :headers="headers"
      :items="songs"
      class="elevation-1"
    >
      <template v-slot:top>
        <v-toolbar
          flat
        >
          <v-toolbar-title>My music</v-toolbar-title>
          <v-spacer></v-spacer>
          <v-dialog
            v-model="dialog"
            max-width="500px"
          >
            <template v-slot:activator="{ on, attrs }">
              <v-btn
                color="primary"
                dark
                class="mb-2"
                v-bind="attrs"
                v-on="on"
              >
                Upload new song
              </v-btn>
            </template>
            <v-card>
              <v-card-text>
                <v-container>
                  <v-row>
                    <v-col cols="12">
                      <v-text-field
                        v-model="song.name"
                        label="Song Name"
                      ></v-text-field>
                    </v-col>
                    <v-col cols="12">
                      <v-select :items="categories" v-model="song.categories" multiple label="Select one or more Categories"></v-select>
                    </v-col>
                    <v-col cols="12">
                      <v-file-input
                        show-size
                        truncate-length="15"
                        v-model="song.file"
                        accept="audio/mp3"
                        label="Song File"
                      ></v-file-input>
                    </v-col>
                    <v-col cols="12">
                      <v-file-input
                        show-size
                        truncate-length="15"
                        accept="image/png, image/jpeg"
                        v-model="song.image"
                        label="Song Image"
                      ></v-file-input>
                    </v-col>
                  </v-row>
                </v-container>
              </v-card-text>

              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn
                  color="blue darken-1"
                  text
                  @click="close"
                >
                  Cancel
                </v-btn>
                <v-btn
                  color="blue darken-1"
                  text
                  @click="create"
                >
                  Save
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>
        </v-toolbar>
      </template>

      <template v-slot:item.thumbnail="{ item }">
        <v-img :src="item.thumbnail" :lazy-src="item.thumbnail" width="50"/>
      </template>
      <template v-slot:item.category="{ item }">
        <span v-for="i in item.categories" :key="i.id">
          {{i.name}}
        </span>
      </template>
    </v-data-table>
    <v-snackbar
      v-model="snackbar.status"
    >
      {{ snackbar.text }}

      <template v-slot:action="{ attrs }">
        <v-btn
          color="secondary"
          text
          v-bind="attrs"
          @click="snackbar.status = false"
        >
          Close
        </v-btn>
      </template>
    </v-snackbar>
  </div>
</template>

<script>
import { musicService } from "@/services/music.service";

export default {
  name: "Music",
  data() {
    return {
      headers: [
        { text: 'Name', value: 'name' },
        { text: 'Status', value: 'status' },
        { text: 'Reason', value: 'reason' },
        { text: 'Categories', value: 'category' },
        { text: 'Thumbnail', value: 'thumbnail' }
      ],
      songs: [],
      categories: [],
      snackbar: {
        status: false,
        text: ""
      },
      dialog: false,
      editedIndex: -1,
      song: {
        name: null,
        categories: null,
        file: null,
        image: null
      },
      defaultItem: {
        name: null,
        categories: null,
        file: null,
        image: null
      }
    }
  },
  mounted() {
    this.fetchSongs();
    this.fetchCategories();
  },
  methods: {
    fetchSongs() {
      musicService.get_songs_by_user().then(res => {
        console.log(res);
        this.songs = res.data;
      }).catch(err => {
        this.snackbar.status = true;
        this.snackbar.text = err.response.data;
      })
    },
    fetchCategories() {
      musicService.get_categories().then(res => {
        res.data.forEach((item) => {
          this.categories.push({
            value: item.id,
            text: item.name
          })
        })
      }).catch(err => {
        this.snackbar.status = true;
        this.snackbar.text = err.response.data;
      })
    },
    close () {
      this.dialog = false
      this.$nextTick(() => {
        this.category = Object.assign({}, this.defaultItem)
        this.editedIndex = -1
      })
    },
    create () {
      musicService.publish_song(this.song).then(res => {
        this.fetchSongs();
      }).catch(err => {
        this.snackbar.status = true;
        this.snackbar.text = err.response.data;
      }).finally(() => {
        this.dialog = false;
      })
    },
  }
}
</script>

<style scoped>

</style>
