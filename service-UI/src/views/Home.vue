<template>
    <div class="home">
        Welcome
    </div>
</template>

<script>
    // @ is an alias to /src
    // import DashBoard from '@/components/dashboard_back.vue'

    export default {
        name: 'home',
        data() {
            return {
                user: [{provider: 'EC2', name:'1'}]
            }
        },
        components: {

        }
    }
</script>
