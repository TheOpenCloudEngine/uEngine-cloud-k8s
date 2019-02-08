import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import axios from 'axios'
import Element from 'element-ui'
import locale from 'element-ui/lib/locale/lang/ko'


Vue.config.productionTip = false
Vue.prototype.$http = axios
Vue.use(Element, { locale })


// window.API_HOST = "http://serviceapi:8086"
if( process.env.NODE_ENV == "development" ){
  window.API_HOST = process.env.VUE_APP_API_HOST
}else{
  window.API_HOST = process.env.VUE_APP_API_HOST
}

console.log(process.env);

new Vue({
  router,
  store,

  render: function (h) { return h(App) }
}).$mount('#app')
