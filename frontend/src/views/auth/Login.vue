<template>
  <v-container
    style="max-width: 720px;"
    fill-height
    fluid
  >
    <v-row>
      <v-col
        :cols="12"
        :sm="6"
        :md="7"
        align-self="center"
        class="pr-0"
      >
        <v-img style="box-shadow: 15px 7px 15px 8px rgba(0,0,0,0.15);" height="300" src="https://images.pexels.com/photos/3761020/pexels-photo-3761020.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940" />
      </v-col>
      <v-col
        :cols="12"
        :sm="6"
        :md="5"
        class="pl-0"

      >
        <v-card
          height="400"
          class="pl-10 pr-10 pt-13 pb-13"
          style="box-shadow: 5px 10px 15px 5px rgba(0,0,0,0.10); display: flex; flex-wrap: wrap; align-items: center; align-content: center; justify-content: center;"
        >
          <h2 class="primary--text mb-5">Sign In</h2>
          <form style="width: 100%">
            <v-text-field
                v-model="email"
              label="Email"
            ></v-text-field>
            <v-text-field
                v-model="password"
                  :type='"password"'
                  label="Password"
                class="mb-5"
                ></v-text-field>
            <v-btn
              @click="signIn()"
              color="primary"
              width="100%"
            >
              Login
            </v-btn>
            <p class="text-center mt-3 mb-0">No account? <router-link to="/auth/register">Sign up</router-link></p>
            <div class="error" v-if="error != null">{{error}}</div>
          </form>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import { Auth } from 'aws-amplify';

export default {
  name: 'Login',
  data() {
    return {
      email: null,
      password: null,
      error: null,
      isLoading: false
    }
  },
  methods: {
    signIn() {
      Auth.signIn(this.email, this.password)
        .then(res => {
          this.$store.commit("user/loginSuccess", {
            token: res.signInUserSession.accessToken.jwtToken,
            role: res.signInUserSession.accessToken.payload["cognito:groups"]
          });

          this.$router.push("/");
        }).catch(err => {
          this.error = err.message;
      });
    }
  }
}
</script>
