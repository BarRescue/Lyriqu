<template>
  <div>
    <v-data-table
      :headers="headers"
      :items="categories"
      class="elevation-1"
    >
      <template v-slot:top>
        <v-toolbar
          flat
        >
          <v-toolbar-title>All Categories</v-toolbar-title>
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
                New Category
              </v-btn>
            </template>
            <v-card>
              <v-card-text>
                <v-container>
                  <v-row>
                    <v-col cols="12">
                      <v-text-field
                        v-model="category.name"
                        label="Category Name"
                      ></v-text-field>
                    </v-col>
                    <v-col cols="12">
                      <v-text-field
                        v-model="category.description"
                        label="Category Description"
                      ></v-text-field>
                    </v-col>
                    <v-col cols="12">
                      <v-file-input
                        show-size
                        truncate-length="15"
                        v-model="category.file"
                        label="Category image"
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

      <template v-slot:item.logoPath="{ item }">
        <v-img :src="item.logoPath" :lazy-src="item.logoPath" width="50"/>
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
  name: "Categories",
  data() {
    return {
      headers: [
        { text: 'Name', value: 'name' },
        { text: 'Description', value: 'description' },
        { text: 'Image', value: 'logoPath' }
      ],
      categories: [],
      snackbar: {
        status: false,
        text: ""
      },
      dialog: false,
      editedIndex: -1,
      category: {
        name: null,
        description: null,
        file: null
      },
      defaultItem: {
        name: null,
        description: null,
        file: null
      }
    }
  },
  mounted() {
    this.fetchCategories();
  },
  methods: {
    fetchCategories() {
      musicService.get_categories().then(res => {
        this.categories = res.data;
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
      musicService.create_category(this.category).then(res => {
        this.categories.push(this.category)
      }).catch(err => {
        this.snackbar.status = true;
        this.snackbar.text = err.response.data;
      }).finally(() => {
        this.close();
      });
    },
  }
}
</script>
