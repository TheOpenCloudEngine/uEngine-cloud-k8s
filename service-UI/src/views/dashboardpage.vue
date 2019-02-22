<template>
    <div class="dashboardpage">
        <div class="md-layout-item">
            <md-field>
                <label for="namespace">NameSpace</label>
                <md-select v-model="namespace" name="movie" id="movie" >
                    <md-option value=null>All</md-option>
                    <md-option v-for="item in namespaceList" :value="item">{{ item }}</md-option>
                </md-select>
            </md-field>
        </div>
        <md-tabs md-sync-route>
            <md-tab id="tab-home" md-label="Pod">
                <DashBoard :namespace.sync="namespace" :namespaceList.sync="namespaceList" :types="'pod'" style="margin-top: 20px;"/>
            </md-tab>
            <md-tab id="tab-pages" md-label="service">
                <DashBoard :namespace.sync="namespace" :namespaceList.sync="namespaceList" :types="'service'" style="margin-top: 20px;"/>
            </md-tab>
            <md-tab id="tab-posts" md-label="deployments">
                <DashBoard :namespace.sync="namespace" :namespaceList.sync="namespaceList" :types="'deployment'" style="margin-top: 20px;" />
            </md-tab>
        </md-tabs>

    </div>
</template>

<script>
    // @ is an alias to /src
    import DashBoard from '@/components/DashBoard.vue'

    export default {
        name: 'home',
        data() {
            return {
                namespace: null,
                namespaceList: [],
            }
        },
        components: {
            DashBoard
        }
    }
</script>
