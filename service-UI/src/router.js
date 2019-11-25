import Vue from 'vue'
import Router from 'vue-router'
// import Dashboard from './views/dashboardpage.vue'
import EventStormingListPage from './components/EventStormingList/EventStormingListPage.vue'
import Introduce from './components/EventStormingList/Introduce.vue'



Vue.use(Router)

import ModelerRouter from './components/designer/ModelerRouter'
import ProcessDesigner from './components/designer/process/ProcessDesigner'
import EventModeler from './components/designer/class-modeling/EventModeler'
import ClassModeler from './components/designer/class-modeling/ClassModeler'
import ModelerImageGenerator from './components/designer/ModelerImageGenerator'

Vue.component('modeler-router', ModelerRouter);
Vue.component('modeler-image-generator', ModelerImageGenerator);
Vue.component('process-designer', ProcessDesigner);
Vue.component('event-modeler', EventModeler)
Vue.component('class-modeler', ClassModeler)


export default new Router({
    base: process.env.BASE_URL,
    routes: [
        // {
        //     path: '/',
        //     name: 'Introduce',
        //     component: Introduce
        // },
        // {
        //     path: '/event',
        //     name: 'EventStorming',
        //     component: EventStormingListPage
        // },
        {
            path: '/',
            name: 'EventStorming',
            component: EventModeler
        },
        // {
        //   path: '/about',
        //   name: 'about',
        //   // route level code-splitting
        //   // this generates a separate chunk (about.[hash].js) for this route
        //   // which is lazy-loaded when the route is visited.
        //   component: function () {
        //     return import(/* webpackChunkName: "about" */ './views/About.vue')
        //   }
        // }
    ]
})
