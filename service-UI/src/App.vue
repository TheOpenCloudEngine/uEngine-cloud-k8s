<template>
    <div id="app" style="height: 100%">
        <el-container style="height: 100%">
            <el-header>
                <el-menu class="el-menu-demo" mode="horizontal">
                    <el-menu-item index="1">
                    </el-menu-item>
                </el-menu>
            </el-header>
            <el-container>
                <el-aside width="200px">
                    <el-menu
                            default-active="1"
                            class="el-menu-vertical-demo"
                            @open="handleOpen"
                            @close="handleClose"
                            background-color="#545c64"
                            text-color="#fff"
                            active-text-color="#ffd04b"
                            style="height: 100%; width: 100%;"
                            router = true >
                        <el-menu-item route="/" index="1">
                            <i class="el-icon-menu"></i>
                            <span>Home</span>
                        </el-menu-item>
                        <el-menu-item route="dashboard" index="dashboard">
                            <i class="el-icon-document"></i>
                            <span>Dashboard</span>
                        </el-menu-item>
                        <el-menu-item route="edityaml" index="edityaml">
                            <i class="el-icon-setting"></i>
                            <span>EditYaml</span>
                        </el-menu-item>
                    </el-menu>
                </el-aside>
                <el-main>
                    <router-view />
                </el-main>
            </el-container>
        </el-container>


    </div>
</template>
<script>
    export default {
        created() {
            // console.log(window.innerHeight)
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
    .el-menu-vertical-demo:not(.el-menu--collapse) {
        width: 220px;
    }

    #app {
        font-family: 'Avenir', Helvetica, Arial, sans-serif;
        -webkit-font-smoothing: antialiased;
        -moz-osx-font-smoothing: grayscale;
        text-align: center;
        color: #2c3e50;
    }

    #nav {
        padding: 30px;
        a {
            font-weight: bold;
            color: #2c3e50;
            &.router-link-exact-active {
                color: #42b983;
            }
        }
    }
</style>
