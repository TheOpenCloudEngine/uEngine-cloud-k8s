import Vue from 'vue'
import './plugins/vuetify'
import 'vuetify/dist/vuetify.min.css'
import App from './App.vue'
import router from './router'
import store from './store'
import './registerServiceWorker'
import VueJWT from 'vuejs-jwt'

import VModal from 'vue-js-modal'
import EditYaml from './components/edityamlpage.vue'
import textReader from './components/yaml.vue'
import Opengraph from './components/opengraph'
import ClassModeling from './components/designer/class-modeling'
import Modeling from './components/designer/modeling'

Vue.use(Opengraph);
Vue.use(ClassModeling);
Vue.use(Modeling);

import Metaworks4 from '../node_modules/metaworks4'

Vue.use(Metaworks4);

Vue.use(VModal)
Vue.component('EditYaml', EditYaml)
Vue.component('text-reader', textReader)

var options = {'keyName' : 'accessToken'};

Vue.use(VueJWT, options)

process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";

Vue.prototype.$EventBus = new Vue()
Vue.prototype.$ModelingBus = new Vue()

if( process.env.NODE_ENV == "development" ){
    window.API_HOST = "http://a3957b136544e11e9a56802910c21b39-1662590085.ap-northeast-2.elb.amazonaws.com:8080";
    // window.API_HOST = "http://a3957b136544e11e9a56802910c21b39-1662590085.ap-northeast-2.elb.amazonaws.com:8080";
}else{
    window.API_HOST = process.env.VUE_APP_API_HOST
}

Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: function (h) { return h(App) }
}).$mount('#app')
