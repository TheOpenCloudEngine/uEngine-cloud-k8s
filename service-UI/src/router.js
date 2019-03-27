import Vue from 'vue'
import Router from 'vue-router'
import Dashboard from './views/dashboardpage.vue'
import Home from './components/HelloWorld'

Vue.use(Router)

export default new Router({
    mode: 'history',
    base: process.env.BASE_URL,
    routes: [
        {
            path: '/',
            name: 'Dashboard',
            component: Home
        },
        // {
        //     path: '/yaml',
        //     name: 'EditYaml',
        //     component: EditYaml
        // },
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
