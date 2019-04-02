import Vue from 'vue'
import Vuex from 'vuex'
import axios from 'axios'

Vue.prototype.$http = axios

Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        accessToken: null,
        kubeHost: '',
        kubeToken: '',
        storeAuthorized: false
    },
    getters: {
        getAuth(state) {
            return state.storeAuthorized
        }
    },
    mutations: {
        LOGIN (state, data) {
            console.log(data)
            state.kubeHost = data.kubeHost
            state.kubeToken = data.kubeToken
            state.storeAuthorized = true;
        },
        LOGOUT (state) {
            state.kubeHost = ''
            state.kubeToken = ''
            state.storeAuthorized = false;
        }
    },
    actions: {
        LOGIN ({ commit }, data) {
            // console.log(data)
            if(data.kubeToken.length < 1 && data.kubeHost.length <1)
                commit('LOGIN', data)
            // commit('LOGIN', {accessToken, host})
            // Vue.prototype.$http.defaults.baseURL = 'http://localhost:8080';
            // Vue.prototype.$http.defaults.headers.common['Authorization'] = `Bearer ${accessToken}`;
        },
        LOGOUT ({commit}) {
            commit('LOGOUT')
        },
    }
})
