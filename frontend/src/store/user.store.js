import jwt_decode from "jwt-decode";

const state = {
    user: {
        token: null,
        role: null,
        loggedIn: false
    }
};

const getters = {
    getToken() {
        return state.user.token;
    },
    isLoggedIn() {
        return state.user.loggedIn;
    },
    getRole() {
        return state.user.role;
    }
};

const mutations = {
    loginSuccess(state, payload) {
        if(payload.role === undefined) {
            payload.role = "User";
        }

        state.user = {
            token: payload.token,
            role: payload.role,
            loggedIn: true
        };
    },
    loginFailed(state) {
        state.user = {
            token: null,
            role: null,
            loggedIn: false
        };
    }
};

export const user = {
    namespaced: true,
    state,
    getters,
    mutations
};
