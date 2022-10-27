<template>
    <v-row>
      <v-col
        cols="12"
      >
        <h1>Billing details</h1>
      </v-col>
      <v-col
        cols="12"
        md="6"
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
            €10<span style="font-size: 16px;" class="font-weight-regular">.00</span>
          </v-card-subtitle>
          <v-card-text
            class="white--text"
            style="font-size: 16px;"
          >
            Premium subscription
          </v-card-text>
          <v-card-actions
            style="justify-content: center;"
          >
            <v-btn v-if="!subscription.options.endDate" color="secondary" @click="cancelSubscription">Cancel</v-btn>
          </v-card-actions>
        </v-card>
      </v-col>
      <v-col
        cols="12"
        md="6"
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
            v-if="!subscription.options.nextPayment"
          >
            You subscription will cancel on
          </v-card-title>
          <v-card-title
            class="white--text text-center"
            style="font-size: 18px; width: 100%; justify-content: center;"
            v-if="subscription.options.nextPayment"
          >
            Next payment
          </v-card-title>
          <v-card-subtitle
            style="font-size: 32px"
            class="white--text font-weight-bold mt-0"
            v-if="subscription.options.nextPayment"
          >
            €10<span style="font-size: 16px;" class="font-weight-regular">.00</span>
          </v-card-subtitle>
          <v-card-subtitle
            style="font-size: 32px"
            class="white--text font-weight-bold mt-0"
            v-if="!subscription.options.nextPayment"
          >
            {{ subscription.options.endDate | formatDate }}
          </v-card-subtitle>
          <v-card-text
            class="white--text"
            style="font-size: 16px;"
            v-if="subscription.options.nextPayment"
          >
            On {{ subscription.options.nextPayment | formatDate }}
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
</template>

<script>
import { subscriptionService } from "@/services/subscription.service";

export default {
  name: "SubscriptionActive",
  props: {
    subscription: {
      type: Object
    }
  },
  methods: {
    cancelSubscription() {
      subscriptionService.cancel_subscription().then(() => {
        window.location.reload();
      }).catch(err => {
        console.log(err.response);
      });
    }
  }
}
</script>

<style scoped>

</style>
