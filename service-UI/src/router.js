import Vue from 'vue'
import Router from 'vue-router'
import Home from './views/Home.vue'

Vue.use(Router);

var backend;

// console.log(process.env);


export default new Router({
    routes: [
        {
            path: '/',
            name: 'User one',
            component: Home,
        }
    ]
})
