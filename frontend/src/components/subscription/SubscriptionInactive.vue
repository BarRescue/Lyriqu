<template>
  <v-row>
    <v-col cols="12">
        <h1>Billing details</h1>
    </v-col>
    <v-col
      cols="12"
      sm="6"
    >
      <v-card
        :flat="true"
        img="https://lyriqu.nl/images/background-subscription.png"
        min-height="300"
        class="text-center pa-4"
      >
        <v-card-title
          class="white--text text-center"
          style="font-size: 18px; width: 100%; justify-content: center;"
        >
          Your current subscription
        </v-card-title>
        <v-card-subtitle
          style="font-size: 32px"
          class="white--text font-weight-bold mt-0"
        >
          Not active
        </v-card-subtitle>
        <v-card-actions
          style="justify-content: center;"
        >
          <v-btn color="secondary" @click="createSubscription">Start subscription</v-btn>
        </v-card-actions>
      </v-card>
    </v-col>
    <v-col
        cols="12"
        sm="6"
      >
        <v-card
          :flat="true"
          img="https://lyriqu.nl/images/next-payment.png"
          min-height="300"
          class="text-center pa-4"
        >
          <v-card-title
            class="white--text text-center"
            style="font-size: 18px; width: 100%; justify-content: center;"
          >
            Why start a subscription?
          </v-card-title>
          <v-card-subtitle
            class="white--text font-weight-bold mt-0 why-card-subtitle"
          >
            Upload music and get recognized by people
          </v-card-subtitle>
          <v-card-text
            class="white--text"
            style="font-size: 16px;"
          >
          </v-card-text>
        </v-card>
      </v-col>
  </v-row>
</template>

<script>
import {paymentService} from "@/services/payment.service";

export default {
name: "SubscriptionInactive",
  methods: {
    createSubscription() {
      paymentService.create_mandate().then(res => {
        window.location.href = res.data.href;
      }).catch(err => {
        console.log(err.response);
      })
    }
  }
}
</script>

<style scoped>
  .why-card-subtitle {
    line-height: 36px;
    font-size: 26px;
  }

  @media (min-width: 768px) {
    .why-card-subtitle {
      line-height: 42px;
      font-size: 32px;
    }
  }
</style>
