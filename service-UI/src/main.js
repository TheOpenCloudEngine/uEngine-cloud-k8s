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
import Mustache from 'mustache'
import CodeMirror from 'vue-codemirror'
import VueYouTubeEmbed from 'vue-youtube-embed'

Vue.use(Mustache)
Vue.use(CodeMirror)
Vue.use(Opengraph);
Vue.use(ClassModeling);
Vue.use(Modeling);
// import Metaworks4 from '../node_modules/metaworks4'
import vuetify from './plugins/vuetify';
//
// Vue.use(Metaworks4);

Vue.use(VModal)
Vue.component('EditYaml', EditYaml)
Vue.component('text-reader', textReader)

var options = {'keyName' : 'accessToken'};

Vue.use(VueJWT, options)
Vue.use(VueYouTubeEmbed, { global: true, componentId: "youtube-media" })
process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";

Vue.prototype.$EventBus = new Vue()
Vue.prototype.$ModelingBus = new Vue()

if( process.env.NODE_ENV == "development" ){
    window.API_HOST = "localhost:8080";
}else{
    window.API_HOST = process.env.VUE_APP_API_HOST
}

// const TRANSLATE_KEY =`${process.env.VUE_APP_TRANSLATE_KEY}`
// console.log(TRANSLATE_KEY)

Vue.config.productionTip = false

new Vue({
    router,
    store,
    vuetify,
    render: function (h) { return h(App) }
}).$mount('#app')
