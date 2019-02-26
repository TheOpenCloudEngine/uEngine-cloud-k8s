import Vue from 'vue'
import './plugins/vuetify'
import App from './App.vue'
import router from './router'
import store from './store'
import axios from 'axios'
import './registerServiceWorker'

import VModal from 'vue-js-modal'

Vue.use(VModal)

Vue.prototype.$http = axios

if( process.env.NODE_ENV == "development" ){
    window.API_HOST = "http://localhost:8086";
}else{
    window.API_HOST = process.env.VUE_APP_API_HOST
}

Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: function (h) { return h(App) }
}).$mount('#app')
