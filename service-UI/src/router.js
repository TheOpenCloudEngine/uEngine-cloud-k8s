import Vue from 'vue'
import Router from 'vue-router'
import DashBoardPage from './views/dashboardpage.vue'
import EditYaml from './views/edityamlpage.vue'
import Home from './views/Home.vue'


Vue.use(Router);

var backend;

// console.log(process.env);


export default new Router({
    routes: [
        {
            path: '/',
            name: 'Home',
            component: Home,
        },

        {
            path: '/dashboard',
            name: 'Dashboard',
            component: DashBoardPage,
        },
        {
            path: '/edityaml',
            name: 'Edit YAML',
            component: EditYaml,
        },
    ]
})
