import Vue from 'vue';
import Vuetify from 'vuetify'
import "vuetify/dist/vuetify.min.css"
import '@mdi/font/css/materialdesignicons.css'

Vue.use(Vuetify);

export default new Vuetify({
    icons: {
        iconfont: 'mdi',
    },
    theme: {
        dark: true,
        themes: {
            light: {
                primary: '#10ac84',
                secondary: '#2f3640'
            },
            dark: {
                primary: '#10ac84',
                secondary: '#2f3640'
            }
        }
    }
});
