import Vue from 'vue'
import './plugins/vuetify'
import App from './App.vue'
import router from './router'
import store from './store'
import './registerServiceWorker'
import VueJWT from 'vuejs-jwt'

import VModal from 'vue-js-modal'
import EditYaml from './components/edityamlpage.vue'
import textReader from './components/yaml.vue'

Vue.use(VModal)
Vue.component('EditYaml', EditYaml)
Vue.component('text-reader', textReader)

var options = {'keyName' : 'accessToken'};

Vue.use(VueJWT, options)



process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";

Vue.prototype.$EventBus = new Vue()

if( process.env.NODE_ENV == "development" ){
    // window.API_HOST = "http://localhost:8080";
    window.API_HOST = "http://a83d5e3453ee611e9a56802910c21b39-157320908.ap-northeast-2.elb.amazonaws.com";
}else{
    window.API_HOST = process.env.VUE_APP_API_HOST
}

Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: function (h) { return h(App) }
}).$mount('#app')
