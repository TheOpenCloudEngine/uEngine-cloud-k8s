import Vue from 'vue'
import Vuex from 'vuex'
import axios from 'axios'

Vue.prototype.$http = axios

Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        accessToken: null
    },
    getters: {

    },
    mutations: {
        LOGIN (state, {accessToken,baseURL}) {
            // state.accessToken = accessToken
            // localStorage.accessToken = accessToken
            // console.log(baseURL)
        },
        LOGOUT (state) {
            state.accessToken = null
        }
    },
    actions: {
        LOGIN ({commit}, accessToken) {
            console.log(accessToken)
            // commit('LOGIN', {accessToken,baseURL})
            Vue.prototype.$http.defaults.baseURL = 'http://localhost:8080';
            Vue.prototype.$http.defaults.headers.common['Authorization'] = `Bearer ${accessToken}`;
        },
        LOGOUT ({commit}) {
            commit('LOGOUT')
        },
    }
})
