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

    </v-app>
</template>

<script>
    export default {
        data: () => ({
            dialog: false,
            drawer: null,
            items: [
                {icon: 'home', text: 'Home', route: '/'},
                {icon: 'dashboard', text: 'Dashboard', route: '/dashboard/pod'},
            ]
        }),
        props: {
            source: String
        }
    }
</script>
