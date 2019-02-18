<template>
    <div class="dashboardpage">
        <!--<el-button @click="user=[{provider: 'All' , name: 'All'}]">Master</el-button>-->
        <!--<el-button @click="user=[{provider: 'EC2' , name: 'nodes.jjy.k8s.local'}]">User 1</el-button>-->
        <!--<el-button @click="user=[{provider: 'K8S' , name: 'test'}]">User 2</el-button>-->
        <!--<el-button @click="user=[{provider: 'K8S' , name: 'default'}, {provider: 'EC2' , name: 'nodes.jjy.k8s.local'}]">User 3</el-button>-->
        <el-tabs :tab-position="tabPosition" style="height: 200px;">
            <el-tab-pane label="PODS">
                <DashBoard :types="'pod'" style="margin-top: 20px;"/>


            </el-tab-pane>
            <el-tab-pane label="SERVICE">

                <DashBoard :types="'service'"style="margin-top: 20px;" />

            </el-tab-pane>
            <el-tab-pane label="DEPELOYMENT">
                <DashBoard :types="'deployment'" style="margin-top: 20px;" />


            </el-tab-pane>

        </el-tabs>

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
