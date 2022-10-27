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
          height="550"
          class="pl-10 pr-10 pt-13 pb-13"
          style="box-shadow: 5px 10px 15px 5px rgba(0,0,0,0.10); display: flex; flex-wrap: wrap; align-items: center; align-content: center; justify-content: center;"
        >
          <h2 class="primary--text mb-5">Sign Up</h2>
          <form style="width: 100%">
            <v-text-field
                v-model="username"
                label="Email"
            ></v-text-field>
            <v-text-field
                v-model="name"
                label="Name"
                ></v-text-field>
            <v-text-field
                v-model="password"
                :type='"password"'
                label="Password"
                ></v-text-field>
            <v-text-field
                v-model="repeat"
                :type='"password"'
                label="Repeat password"
                class="mb-5"
                ></v-text-field>
            <v-btn
              @click="signUp()"
              color="primary"
              width="100%"
            >
              Sign up
            </v-btn>
            <p class="text-center mt-3 mb-0">Already registered? <router-link to="/auth/login">Sign in</router-link></p>
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
  name: 'Register',
  data() {
    return {
      username: null,
      password: null,
      repeat: null,
      name: null,
      error: null
    }
  },
  methods: {
    signUp() {

      this.error = null;

      if(!this.password || !this.repeat || !this.username) {
        this.error = "All fields are required.";
        return;
      }

      if(this.password !== this.repeat) {
        this.error = "Password does not match.";
        return;
      }

      if(!this.validEmail(this.username)) {
        this.error = "Valid e-mail required.";
        return;
      }


      Auth.signUp({
        username: this.username,
        password: this.password,
        attributes: {
          name: this.name
        }
      })
      .then((res) => {
        this.$router.push('/auth/confirm')
      })
      .catch(err => {
        this.error = err.message;
      });
    },
    validEmail: function (email) {
      var re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
      return re.test(email);
    }
  }
}
</script>
