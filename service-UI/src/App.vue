<template>
    <div class="page-container">
        <md-app md-mode="fixed" style="height: 100% !important;">
            <md-app-toolbar class="md-primary">
                <md-button class="md-icon-button" @click="menuVisible = !menuVisible">
                    <md-icon>menu</md-icon>
                </md-button>
                <span class="md-title">My Title</span>
            </md-app-toolbar>

            <md-app-drawer :md-active.sync="menuVisible">
                <md-toolbar class="md-transparent" md-elevation="0">Menu</md-toolbar>
                <md-list>
                    <md-list-item to="/home">
                        <md-icon>home</md-icon>
                        <span class="md-list-item-text">Home</span>
                    </md-list-item>

                    <md-list-item to="/dashboard">
                        <md-icon>send</md-icon>
                        <span class="md-list-item-text">Dashboard</span>
                    </md-list-item>

                    <md-list-item to="/edityaml">
                        <md-icon>description</md-icon>
                        <span class="md-list-item-text">Edit YAML</span>
                    </md-list-item>
                </md-list>
            </md-app-drawer>

            <md-app-content>
                <router-view/>
            </md-app-content>
        </md-app>
    </div>
    <!--<div id="app" style="width: 100%;  height: 100%;">-->
        <!--<md-app md-waterfall md-mode="flexible">-->

            <!--<md-app-content>-->
            <!--</md-app-content>-->
        <!--</md-app>-->
    <!--</div>-->
</template>
<script>
    export default {
        created() {
            // console.log(window.innerHeight)
        },
        data() {
            return {
                menuVisible: false
            }
        },
        methods: {
            handleOpen(key, keyPath) {
                console.log(key, keyPath);
            },
            handleClose(key, keyPath) {
                console.log(key, keyPath);
            }
        }
    }
</script>
<style lang="scss">
    .page-container {
        height: 100%;
    }
    .md-app {
        height: 100%;
        border: 1px solid rgba(#000, .12);
    }
    .md-overlay {
        height: 100%;
    }
    .md-drawer {
        width: 230px;
    }
</style>
