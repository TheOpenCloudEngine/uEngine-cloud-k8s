<template>
    <div class="dashboardpage">
        <v-card>
            <v-card-title>
                <v-select
                        v-model="namespace"
                        :items="namespaceList"
                        label="Namespace"
                ></v-select>
            </v-card-title>
            <v-card-text>
                <v-tabs fixed-tabs>
                    <v-tab :key="'pod'">
                        Pods
                    </v-tab>

                    <v-tab :key="'deployment'">
                        Deployments
                    </v-tab>

                    <v-tab :key="'service'">
                        Serivce
                    </v-tab>

                    <v-tab-item :key="'service'">
                        <DashBoard :namespace.sync="namespace" :namespaceList.sync="namespaceList" :types="'service'"
                                   style="margin-top: 20px;"/>
                    </v-tab-item>

                    <v-tab-item :key="'deployment'">
                        <DashBoard :namespace.sync="namespace" :namespaceList.sync="namespaceList" :types="'deployment'"
                                   style="margin-top: 20px;"/>
                    </v-tab-item>

                    <v-tab-item :key="'pod'">
                        <DashBoard :namespace.sync="namespace" :namespaceList.sync="namespaceList" :types="'pod'"
                                   style="margin-top: 20px;"/>
                    </v-tab-item>
                </v-tabs>
            </v-card-text>
        </v-card>
        <!--<md-tabs md-sync-route>-->
        <!--<md-tab id="tab-home" md-label="Pod">-->
        <!--</md-tab>-->
        <!--<md-tab id="tab-pages" md-label="service">-->
        <!--</md-tab>-->
        <!--<md-tab id="tab-posts" md-label="deployments">-->
        <!--</md-tab>-->
        <!--</md-tabs>-->
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
