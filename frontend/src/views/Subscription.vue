<template>
  <v-container>
    <SubscriptionActive v-if="subscription && !isLoading" :subscription="subscription" />
    <SubscriptionInactive v-if="!subscription && !isLoading" />
    <v-row>
      <v-col
        cols="12"
      >
        <h2 class="mb-5">Payment history</h2>
        <template>
          <v-simple-table v-if="payments">
            <template v-slot:default>
              <thead>
                <tr>
                  <th class="text-left">
                    Amount
                  </th>
                  <th class="text-left">
                    Date
                  </th>
                  <th class="text-left">
                    Description
                  </th>
                  <th class="text-left">
                    Method
                  </th>
                  <th class="text-left">
                    Status
                  </th>
                </tr>
              </thead>
              <tbody>
                <tr
                  v-for="payment in payments"
                  :key="payment.id"
                >
                  <td>&euro;{{ formatPrice(payment.amount.value) }}</td>
                  <td>{{ payment.createdAt | formatDate }}</td>
                  <td>{{ payment.description }}</td>
                  <td>{{ payment.method }}</td>
                  <td>
                    <div class="success text-center white--text" style="border-radius: 5px;" v-if="payment.status === 'paid'">{{ payment.status }}</div>
                    <div class="orange text-center white--text" style="border-radius: 5px;" v-if="payment.status === 'pending'">{{ payment.status }}</div>
                    <div class="orange text-center white--text" style="border-radius: 5px;" v-if="payment.status === 'open'">{{ payment.status }}</div>
                    <div class="error text-center white--text" style="border-radius: 5px;" v-if="payment.status === 'canceled'">{{ payment.status }}</div>
                  </td>
                </tr>
              </tbody>
            </template>
          </v-simple-table>
        </template>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import { paymentService } from '@/services/payment.service';
import { subscriptionService } from '@/services/subscription.service';

import SubscriptionActive from '@/components/subscription/SubscriptionActive';
import SubscriptionInactive from "@/components/subscription/SubscriptionInactive";

export default {
  name: "Subscription",
  data() {
    return {
      subscription: null,
      isLoading: true,
      payments: null
    }
  },
  components: {
    SubscriptionInactive,
    SubscriptionActive
  },
  created() {
    this.fetchSubscription();
    this.fetchPayments();
  },
  methods: {
    formatPrice(value) {
        let val = (value/1).toFixed(2).replace('.', ',')
        return val.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".")
    },
    fetchPayments() {
      paymentService.get_payments().then(res => {
        this.payments = res.data;
        console.log(res.data);
      }).catch(err => {
        console.log(err.response);
      })
    },
    fetchSubscription() {
      subscriptionService.get_subscription().then(res => {
        this.subscription = res.data;
      }).catch(err => {
        console.log(err.response);
      }).finally(() => {
        this.isLoading = false;
      });
    }
  }
}
</script>

<style scoped>

</style>
