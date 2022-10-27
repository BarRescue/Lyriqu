import Vue from 'vue'
import App from './App.vue'
import router from './router'
import vuetify from "@/plugins/vuetify";
import { store } from './store'
import Amplify, { Auth } from 'aws-amplify';
import awsmobile from './aws-exports';
import moment from 'moment';

Amplify.configure(awsmobile);
Vue.config.productionTip = false

import Default from "./layouts/default";
import NoSidebar from "./layouts/no-sidebar";

Vue.component('default-layout', Default);
Vue.component('no-sidebar-layout', NoSidebar);

Vue.filter('formatDate', function(value) {
    if (value) {
        return moment(String(value)).format('DD MMMM, YYYY')
    }
});

new Vue({
  router,
  store,
  vuetify,
  render: h => h(App)
}).$mount('#app')
