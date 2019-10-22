<template>
    <v-app id="inspire">
        <v-navigation-drawer
                v-model="drawer"
                :clipped="$vuetify.breakpoint.lgAndUp"
                fixed
                app
        >
            <v-list dense>
                <!--<v-list-tile avatar style="margin-top: 10px;" v-if="userInfo">-->
                <!--<v-list-tile-avatar>-->
                <!--<img :src=userInfo.thumbnail>-->
                <!--</v-list-tile-avatar>-->

                <!--<v-list-tile-content>-->
                <!--<v-list-tile-title>{{ userInfo.user_name}}</v-list-tile-title>-->
                <!--<v-list-tile-title>{{ userInfo.nickname}}</v-list-tile-title>-->

                <!--</v-list-tile-content>-->
                <!--</v-list-tile>-->
                <template v-for="item in items">
                    <v-list-item :key="item.text" ripple :to="item.route">
                        <v-list-item-action>
                            <v-icon>{{ item.icon }}</v-icon>
                        </v-list-item-action>
                        <v-list-item-content>
                            <v-list-item-title>
                                {{ item.text }}
                            </v-list-item-title>
                        </v-list-item-content>
                    </v-list-item>
                </template>
            </v-list>
        </v-navigation-drawer>
        <v-app-bar
                :clipped-left="$vuetify.breakpoint.lgAndUp"
                color="blue darken-3"
                dark
                app
                fixed
        >
            <v-toolbar-title style="width: 340px" class="ml-0 pl-3">
                <v-layout>
                    <v-app-bar-nav-icon style="margin-right: 20px;" @click.stop="drawer = !drawer"></v-app-bar-nav-icon>
                    <v-img max-height=45 max-width=220 src="../public/static/image/Logo_black_stroke7.png"></v-img>
                </v-layout>
            </v-toolbar-title>
            <v-spacer></v-spacer>
            <v-btn text color="white" @click="logout()" v-if="authorized">
                Logout
            </v-btn>
            <v-btn text color="white" @click="googleLogin()" v-if="!authorized">
                Login
            </v-btn>
            <!--<v-btn flat color="white" @click="callCurl()">-->
            <!--Button-->
            <!--</v-btn>-->
            <v-btn icon @click="dialog = true">
                <v-icon>settings</v-icon>
            </v-btn>
        </v-app-bar>
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
        <!-- Snackbar insert info -->
        <v-snackbar
                v-model="snackbar"
                color="error"
                :timeout=10000
        >
            Click Setting & Insert Infomation
            <v-btn
                    dark
                    flat
                    @click="snackbar = false"
            >
                Close
            </v-btn>
        </v-snackbar>
    </v-app>
</template>

<script>
    import axios from 'axios'
    import https from 'https'

    export default {
        data: () => ({
            dialog: false,
            drawer: false,
            isLogin: false,
            kubeHost: '',
            kubeToken: '',
            items: [
                {icon: 'fa-book', text: 'Home', route: '/'},
                {icon: 'fa-sticky-note', text: 'EventStorming', route: '/event'},
            ],
            api: [],
            snackbar: false,
            fab:false
        }),
        props: {
            source: String
        },
        components: {
            axios,
            https
        },
        beforeDestroy() {
            window.localStorage.removeItem("accessToken");
        },
        computed: {
            activeFab () {
                switch (this.tabs) {
                    case 'one': return { class: 'purple', icon: 'account_circle' }
                    case 'two': return { class: 'red', icon: 'edit' }
                    case 'three': return { class: 'green', icon: 'keyboard_arrow_up' }
                    default: return {}
                }
            },
            authorized() {
                if (window.localStorage.getItem("accessToken") == null) {
                    window.authorized = false;
                    return false
                } else if (window.localStorage.getItem("accessToken")) {
                    this.$http.defaults.headers.common['Authorization'] = `Bearer ${localStorage.accessToken}`;
                    window.authorized = true;
                    return true
                }
            },
            userInfo() {
                if (this.authorized == true) {
                    // console.log(this.$jwt.hasToken())
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

                    var tmpURL = window.location.protocol + "//" + window.location.host + "/";

                    window.location.href = tmpURL;
                } else {
                }
            } else {
                this.$http.defaults.headers.common['Authorization'] = `Bearer ${localStorage.accessToken}`;
            }
        },
        watch: {},
        mounted() {
            var me = this
            if (me.authorized == true) {
                // console.log(me.userInfo)
                this.$http.get(`${API_HOST}/kube/user/getUserDetail?username=` + me.userInfo.user_name).then((result) => {
                    me.kubeHost = result.data.host
                    me.kubeToken = result.data.token

                    let tmp = {
                        kubeHost: result.data.host,
                        kubeToken: result.data.token,
                        userName: me.userInfo.user_name
                    }

                    this.$store.dispatch('LOGIN', tmp)
                }).catch((e) => {
                    if (e.toString().includes('500')) {
                        me.snackbar = true
                    }
                })
            }
        },
        methods: {
            saveSetting() {
                var me = this;
                me.dialog = false;
                this.$http.put(`${API_HOST}/kube/user/saveUserDetail`, {
                    username: me.userInfo.user_name,
                    host: me.kubeHost,
                    token: me.kubeToken
                }).then((result) => {
                    let tmp = {kubeHost: me.kubeHost, kubeToken: me.kubeHost, userName: me.userInfo.user_name}
                    me.$store.dispatch('LOGIN', tmp)
                })


            },
            googleLogin() {
                var me = this;
                if (localStorage.accessToken) {
                    localStorage.removeItem('accessToken')
                }
                // window.location.href = "https://localhost:8082/login/google"
                window.location.href = "http://service-oauth-lhgws4pe7a-uc.a.run.app/login/google?redirect_uri=http://localhost:8081"
            },
            logout() {
                console.log('logout')
                window.localStorage.removeItem("accessToken");

                var newURL = window.location.protocol + "//" + window.location.host + "/";

                window.location.href = newURL;
                this.$store.commit('LOGOUT')

            }
        }
    }
</script>


