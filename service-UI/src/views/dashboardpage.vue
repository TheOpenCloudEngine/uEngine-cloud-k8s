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
                    <v-btn @click="types='pod'">
                        Pods
                    </v-btn>

                    <v-btn @click="types='deployment'">
                        Deployments
                    </v-btn>

                    <v-btn @click="types='service'">
                        Serivce
                    </v-btn>

                    <DashBoard v-if="show"
                                :namespace.sync="namespace" :namespaceList.sync="namespaceList" :types="types"
                               style="margin-top: 20px;"/>
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
                types : '',
                show: true
            }
        },
        components: {
            DashBoard
        },
        watch: {
            types: function () {
              var me = this;

              me.show = false;

              me.$nextTick(function (){
                  console.log("re-render");
                  me.show = true;
              })
          }
        },
        created() {
            this.types = 'pod'
        }
    }
</script>
