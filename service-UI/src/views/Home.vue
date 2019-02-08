<template>
    <div class="home">
        <el-button @click="user=null">Master</el-button>
        <el-button @click="user=[{provider: 'EC2' , name: 'nodes.jjy.k8s.local'}]">User 1</el-button>
        <el-button @click="user=[{provider: 'K8S' , name: 'default'}]">User 2</el-button>
        <el-button @click="user=[{provider: 'K8S' , name: 'default'}, {provider: 'EC2' , name: 'nodes.jjy.k8s.local'}]">User 3</el-button>
        <DashBoard :user="user"/>
    </div>
</template>

<script>
    // @ is an alias to /src
    import DashBoard from '@/components/DashBoard.vue'

    export default {
        name: 'home',
        data() {
            return {
                user: [{provider: 'EC2', name:'1'}]
            }
        },
        components: {
            DashBoard
        }
    }
</script>
