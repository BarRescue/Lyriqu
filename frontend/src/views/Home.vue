<template>
  <div class="home">
    <v-row v-if="loading">
      <v-col v-for="n in 18" :key="n"
        cols="12"
        md="4"
        lg="2"
      >
        <v-skeleton-loader
          light
          type="card"
          elevation="8"
          :loading="true"
        ></v-skeleton-loader>
      </v-col>
    </v-row>
    <v-row v-if="!loading">
      <v-col v-for="category in categories" :key="category.id"
        cols="12"
        sm="4"
        lg="2"
      >
        <div class="inner pa-3" style="background-color: #313131; border-radius: 10px;">
          <router-link :to="`/category/` + category.id" style="text-decoration: none;">
            <v-img :src="category.logoPath" style="border-radius: 10px;" :lazy-src="category.logoPath"/>
            <h3 class="text-center mt-3" style="color: #FFF; text-decoration: none;">{{category.name}}</h3>
            <p class="text-center ma-0" style="color: #FFF; text-decoration: none;">{{category.description}}</p>
          </router-link>
        </div>
      </v-col>
    </v-row>
  </div>
</template>

<script>
import {musicService} from "@/services/music.service";

export default {
  name: 'Home',
  data() {
    return {
      categories: null,
      loading: true,
      audio: null
    }
  },
  mounted() {
    let _this = this;

    setTimeout(function() {
        _this.getCategories();
    }, 500);
  },
  methods: {
    getCategories() {
      musicService.get_categories().then(res => {
        this.categories = res.data;
      }).catch(err => {
        console.log(err.response)
      }).finally(() => {
        this.loading = false;
      })
    }
  }
}
</script>
