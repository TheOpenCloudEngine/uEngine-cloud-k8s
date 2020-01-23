import Vue from 'vue'
import './plugins/vuetify'
import App from './App.vue'
import router from './router'
import store from './store'
import axios from 'axios'

import VueCodemirror from 'vue-codemirror'
import 'codemirror/lib/codemirror.css'
import 'codemirror/theme/darcula.css'
Vue.use(VueCodemirror);

import Vuetify from 'vuetify/lib'
import 'vuetify/src/stylus/app.styl'
Vue.use(Vuetify)


Vue.config.productionTip = false
Vue.prototype.$http = axios

Vue.component('router-link', Vue.options.components.RouterLink);
Vue.component('router-view', Vue.options.components.RouterView);

if( process.env.NODE_ENV == "development" ){
  window.API_HOST = "http://localhost:8086";
}else{
  window.API_HOST = process.env.VUE_APP_API_HOST
}

new Vue({
  router,
  store,

  render: function (h) { return h(App) }
}).$mount('#app')
