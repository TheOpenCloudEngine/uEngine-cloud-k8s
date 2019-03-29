<template>
    <v-app id="inspire">
        <v-navigation-drawer
                v-model="drawer"
                :clipped="$vuetify.breakpoint.lgAndUp"
                fixed
                app
        >
            <v-list dense>
                <template v-for="item in items">
                    <v-list-tile :key="item.text" ripple :to="item.route">
                        <v-list-tile-action>
                            <v-icon>{{ item.icon }}</v-icon>
                        </v-list-tile-action>
                        <v-list-tile-content>
                            <v-list-tile-title>
                                {{ item.text }}
                            </v-list-tile-title>
                        </v-list-tile-content>
                    </v-list-tile>
                </template>
            </v-list>
        </v-navigation-drawer>
        <v-toolbar
                :clipped-left="$vuetify.breakpoint.lgAndUp"
                color="blue darken-3"
                dark
                app
                fixed
        >
            <v-toolbar-title style="width: 300px" class="ml-0 pl-3">
                <v-toolbar-side-icon @click.stop="drawer = !drawer"></v-toolbar-side-icon>
                <span class="hidden-sm-and-down">uEngine</span>
            </v-toolbar-title>
            <v-spacer></v-spacer>
            <v-btn flat color="white" @click="googleLogin()" v-if="!authorized">
                Login
            </v-btn>
            <v-btn flat color="white" @click="callCurl()">
                Call
            </v-btn>
            <v-btn flat color="white" @click="logout()" v-if="authorized">
                Logout
            </v-btn>
            <v-btn icon @click="dialog = true">
                <v-icon>settings</v-icon>
            </v-btn>
        </v-toolbar>
        <v-content>
            <v-container fluid fill-height>
                <v-layout row wrap>
                    <v-flex xs12>
                        <router-view/>
                    </v-flex>
                </v-layout>
            </v-container>
        </v-content>

        <!-- Setting Dialog -->
        <v-dialog v-model="dialog" fullscreen hide-overlay transition="dialog-bottom-transition">
            <v-card>
                <v-toolbar dark color="primary">
                    <v-btn icon dark @click="dialog = false; kubeToken=''; kubeHost='';">
                        <v-icon>close</v-icon>
                    </v-btn>
                    <v-toolbar-title>Settings</v-toolbar-title>
                    <v-spacer></v-spacer>
                    <v-toolbar-items>
                        <v-btn dark flat @click="saveSetting()">Save</v-btn>
                    </v-toolbar-items>
                </v-toolbar>
                <v-list three-line subheader>
                    <v-subheader>Connection Setting</v-subheader>
                    <v-list-tile avatar>
                        <v-list-tile-content>
                            <v-list-tile-sub-title>
                                <v-text-field
                                        label="Kube Host"
                                        v-model="kubeHost"
                                        hint="Ex) https://api.k8s.bzdvops.com"
                                        outline
                                ></v-text-field>
                            </v-list-tile-sub-title>
                        </v-list-tile-content>
                    </v-list-tile>
                    <v-list-tile avatar>
                        <v-list-tile-content>
                            <v-list-tile-sub-title>
                                <v-text-field
                                        label="Kube Token"
                                        v-model="kubeToken"
                                        outline
                                ></v-text-field>
                            </v-list-tile-sub-title>
                        </v-list-tile-content>
                    </v-list-tile>
                </v-list>
            </v-card>
        </v-dialog>
    </v-app>
</template>

<script>
    import axios from 'axios'
    import https from 'https'

    export default {
        data: () => ({
            dialog: false,
            drawer: null,
            isLogin: false,
            kubeHost: 'http://localhost:8080',
            kubeToken: 'eyJhbGciOiJSUzI1NiIsImtpZCI6IiJ9.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJkZWZhdWx0Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZWNyZXQubmFtZSI6ImNsb3VkdXNlci10b2tlbi16cmtqaiIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VydmljZS1hY2NvdW50Lm5hbWUiOiJjbG91ZHVzZXIiLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlcnZpY2UtYWNjb3VudC51aWQiOiI0ZmJmNzk0YS0zNTgwLTExZTktYTU2OC0wMjkxMGMyMWIzOTgiLCJzdWIiOiJzeXN0ZW06c2VydmljZWFjY291bnQ6ZGVmYXVsdDpjbG91ZHVzZXIifQ.APncfC7biCYEre4LZ3S-TVcf641qpQlo7r_BN0khN8ovnT7rR3DWGTaUDLP2eFQBLUvEVSAgTT1g0wF1OFsqEx-Sn3fHByyf1r8t15wvN_XJFM2_V_ZZBosUeZCciklcky0jwF6AWcSpUo9nKa23yBtylJ9d6EPjAq8KtURdX7IVb5i8j0InSExyOQZv5xJ-yv55GB_yRrI9rQ6cwxt_PdFaQiFLjSjnp6SvZj3nPACw_qb98w2I4p_O8DZ5SE-b4OetZj0xmZM7ELXBbceMDepT0UHrU1ZcIc54aWNnhyGFdxspxwrGWSDtNL4T6KKbTqU6HVXkiJTKCw1w9E9hHg',
            items: [
                {icon: 'home', text: 'Home', route: '/'},
            ],
            api: []
        }),
        props: {
            source: String
        },
        components: {
            axios,
            https
        },
        computed: {
            authorized() {
                if (window.localStorage.getItem("accessToken") == null) {
                    return false
                } else if (window.localStorage.getItem("accessToken")) {
                    return true
                }
            },
            userInfo() {
                if(this.authorized == true) {
                    console.log(this.$jwt.hasToken())
                    return this.$jwt.decode()
                } else {
                    return null
                }
            }
        },
        created: function () {
            var me = this
            // localStorage.removeItem("access_token")
            if (window.localStorage.getItem("accessToken") == null) {
                if (this.$route.query.access_token) {
                    localStorage.setItem("accessToken", this.$route.query.access_token)

                    var tmpURL = window.location.href;
                    var deleteURL = window.location.search;

                    tmpURL = tmpURL.replace(deleteURL, '')
                    window.location.href = tmpURL;
                } else {
                }
            } else {
                this.$http.defaults.headers.common['Authorization'] = `Bearer ${localStorage.accessToken}`;
            }
        },
        watch: {

        },
        methods: {
            callCurl() {
                var me = this
                var token = localStorage.getItem('accessToken');
                console.log("Bearer " + token)

                var url = 'http://localhost:8080/kube/v1/pods/namespaces/default'
                $.ajax({
                    url: url,
                    type: "get",
                    headers: {
                        "Authorization": 'Bearer' + token
                    },
                    success: function (data) {
                        console.log('111');
                        console.log(data);
                    },
                    error: function () {
                        console.log('Failed to get env');
                    }
                });

            },
            saveSetting() {
                var me = this;
                me.dialog = false;
            },
            googleLogin() {
                var me = this;
                if (localStorage.accessToken) {
                    localStorage.removeItem('accessToken')
                }
                window.location.href = "http://localhost:8082/login/google"
            },
            logout() {
                window.localStorage.removeItem("accessToken");

                var tmpURL = window.location.href;
                var deleteURL = window.location.search;

                tmpURL = tmpURL.replace(deleteURL, '')

                window.location.href = tmpURL;

                // this.$http.defaults.headers.common['Authorization'] = null;
            }
        }
    }
</script>

