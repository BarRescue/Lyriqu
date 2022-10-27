import Vue from 'vue'
import Vuex from 'vuex'

import { user } from '@/store/user.store'
import { stream } from "@/store/stream.store";

Vue.use(Vuex)

export const store = new Vuex.Store({
    modules: {
        user: user,
        stream: stream
    }
});
