<template>
    <div class="dashboardpage">
        <!--<el-button @click="user=[{provider: 'All' , name: 'All'}]">Master</el-button>-->
        <!--<el-button @click="user=[{provider: 'EC2' , name: 'nodes.jjy.k8s.local'}]">User 1</el-button>-->
        <!--<el-button @click="user=[{provider: 'K8S' , name: 'test'}]">User 2</el-button>-->
        <!--<el-button @click="user=[{provider: 'K8S' , name: 'default'}, {provider: 'EC2' , name: 'nodes.jjy.k8s.local'}]">User 3</el-button>-->
        <DashBoard :types="'pod'" style="margin-top: 20px;"/>
        <DashBoard :types="'service'"style="margin-top: 20px;" />
        <DashBoard :types="'deployment'" style="margin-top: 20px;" />


    </div>
</template>

<script>
    // @ is an alias to /src
    import DashBoard from '@/components/DashBoard.vue'

    export default {
        name: 'home',
        data() {
            return {

            }
        },
        components: {
            DashBoard
        }
    }
</script>
