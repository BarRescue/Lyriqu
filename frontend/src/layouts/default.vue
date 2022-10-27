<template>
    <div id="app">
        <v-app>
            <v-app-bar app color="primary" dark elevation="0">
              <v-spacer></v-spacer>
              <v-app-bar-nav-icon @click.stop="drawer = !drawer"></v-app-bar-nav-icon>
            </v-app-bar>
            <v-navigation-drawer
            v-model="drawer"
            width="200px"
            app
            style="background-color: #2f3640;"
            >

              <v-list dense>
                <v-list-item
                :ripple="false">
                  <v-list-item-title
                    style="color: #FFF; text-align: center; font-size: 20px;"
                  >
                    R
                  </v-list-item-title>
                </v-list-item>
              </v-list>

              <v-divider color="white"></v-divider>

              <v-list dense>
                <v-list-item
                link
                to="/subscription"
                :ripple="false">
                  <v-list-item-icon>
                    <v-icon color="white">mdi-account-cash</v-icon>
                  </v-list-item-icon>
                  <v-list-item-title>
                    Subscription
                  </v-list-item-title>
                </v-list-item>
              </v-list>

              <v-divider color="white"></v-divider>

              <v-list dense>
                <v-list-item
                link
                to="/"
                :ripple="false">
                  <v-list-item-icon>
                    <v-icon color="white">mdi-music-note</v-icon>
                  </v-list-item-icon>
                  <v-list-item-title>
                    Music
                  </v-list-item-title>
                </v-list-item>
              </v-list>

              <v-divider color="white"></v-divider>

              <v-list dense>
                <v-list-item
                link
                to="/my-music"
                :ripple="false">
                  <v-list-item-icon>
                    <v-icon color="white">mdi-music-box</v-icon>
                  </v-list-item-icon>
                  <v-list-item-title>
                    My music
                  </v-list-item-title>
                </v-list-item>
              </v-list>

              <v-divider color="white"></v-divider>

              <v-list dense>
                <v-list-item
                link
                to="/admin/monitoring"
                :ripple="false">
                  <v-list-item-icon>
                    <v-icon color="white">mdi-monitor-eye</v-icon>
                  </v-list-item-icon>
                  <v-list-item-title>
                    Monitoring
                  </v-list-item-title>
                </v-list-item>
              </v-list>

              <v-divider color="white"></v-divider>

              <v-list dense>
                <v-list-item
                link
                to="/admin/categories"
                :ripple="false">
                  <v-list-item-icon>
                    <v-icon color="white">mdi-shape</v-icon>
                  </v-list-item-icon>
                  <v-list-item-title>
                    Manage categories
                  </v-list-item-title>
                </v-list-item>
              </v-list>

              <v-divider color="white"></v-divider>

              <v-list dense>
                <v-list-item
                link
                :ripple="false"
                @click="signOut"
                >
                  <v-list-item-icon>
                    <v-icon color="white">mdi-logout</v-icon>
                  </v-list-item-icon>
                  <v-list-item-title>
                    Logout
                  </v-list-item-title>
                </v-list-item>
              </v-list>

            </v-navigation-drawer>
            <v-content>
                <v-container fluid>
                    <v-row class="fill-height">
                        <v-col>
                            <transition name="fade">
                                <router-view></router-view>
                            </transition>
                        </v-col>
                    </v-row>
                </v-container>
            </v-content>
            <audio controls v-if="endpoint" :key="endpoint" autoplay v-on:play="stopStream(this)">
              <source v-bind:src="endpoint" type="audio/mp3" />
            </audio>
        </v-app>
    </div>
</template>

<script>
import {Auth} from "aws-amplify";
import {mapState} from "vuex";

export default {
name: "default",
  components: {
    //VAudioPlayer: () => import("@woodydark/vuetify-audio-player"),
  },
  data() {
    return {
      drawer: true,
      mini: true
    }
  },
  computed: mapState('stream', {
    endpoint: state => state.data.endpoint
  }),
  watch: {
    endpoint(newValue, oldValue) {
      console.log(`Changing from ${oldValue} to ${newValue}`);
    }
  },
  methods: {
    stopStream(audioElem) {
      console.log(audioElem);
    },
    signOut() {
      Auth.signOut().then(() => {
        this.$store.commit("user/loginFailed");
        this.$router.push("/auth/login");
      });
    }
  }
}
</script>

<style lang="scss" scoped>
  span.d-block {
    color: #FFF;
  }
  .v-application--is-ltr .v-list-item__action:first-child, .v-application--is-ltr .v-list-item__icon:first-child {
    margin-right: 15px;
  }
</style>
