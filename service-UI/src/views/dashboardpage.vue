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
                <v-btn-toggle v-model="toggle_exclusive" mandatory flat>
                    <v-btn flat color="primary"  @click="types='pod'" style="margin: 2px;">
                        pods
                    </v-btn>
                    <v-btn flat color="primary" @click="types='deployment'" style="margin: 2px;">
                        deployment
                    </v-btn>
                    <v-btn flat color="primary" @click="types='service'" style="margin: 2px; ">
                        service
                    </v-btn>
                </v-btn-toggle>
                <v-btn fab flat color="primary" @click="types='service'" style="margin: 2px; ">
                    <v-icon>add</v-icon>
                </v-btn>
                <!--<v-btn @click="types='pod'">-->
                    <!--Pods-->
                <!--</v-btn>-->

                <!--<v-btn @click="types='deployment'">-->
                    <!--Deployments-->
                <!--</v-btn>-->

                <!--<v-btn @click="types='service'">-->
                    <!--Serivce-->
                <!--</v-btn>-->

                <DashBoard v-if="show"
                           :namespace.sync="namespace" :namespaceList.sync="namespaceList" :types="types"
                           style="margin-top: 20px;"/>
            </v-card-text>
        </v-card>
    </div>
</template>

<script>
    // @ is an alias to /src
    import DashBoard from '@/components/DashBoard.vue'

    export default {
        name: 'home',
        data() {
            return {
                namespace: 'All',
                namespaceList: ['All'],
                types: 'pod',
                show: true,
                toggle_exclusive: 0
            }
        },
        components: {
            DashBoard
        },
        watch: {
            types: function () {
                var me = this;

                me.show = false;

                me.$nextTick(function () {
                    console.log("re-render");
                    me.show = true;
                })
            }
        },
        created() {
            // this.types = 'pod'
        }
    }
</script>
