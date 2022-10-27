<template>
  <div class="category">
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
    <Songs :songs="category.songs" v-if="!loading" />
  </div>
</template>

<script>
import {musicService} from "@/services/music.service";
import Songs from "@/components/music/Songs";

export default {
  name: "Category",
  components: {
    Songs
  },
  data() {
    return {
      category: null,
      loading: true
    }
  },
  mounted() {
    this.fetchCategory();
  },
  methods: {
    fetchCategory() {
      musicService.get_category(this.$route.params.id).then(res => {
        console.log(res);
        this.category = res.data;
      }).catch(err => {
        console.log(err);
      }).finally(() => {
        this.loading = false;
      });
    }
  }
}
</script>

<style scoped>

</style>
