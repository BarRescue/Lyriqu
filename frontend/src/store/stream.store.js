import { endpoints } from "@/helpers/endpoint.helper"

const state = {
    data: {
        endpoint: null,
        cancelToken: null
    }
};

const getters = {
    getEndpoint() {
        return state.data.endpoint;
    },
    getToken() {
        return state.data.cancelToken;
    }
};

const mutations = {
    newStream(state, payload) {
        state.data.endpoint = payload.endpoint;
    },
    addToken(state, payload) {
        state.data.cancelToken = payload.token
    }
};

export const stream = {
    namespaced: true,
    state,
    getters,
    mutations,
};
