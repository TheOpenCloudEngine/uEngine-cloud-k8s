<template>
    <div class="yaml" style="text-align:center; margin: 10px">
        <label class="text-reader">
            Read File
            <input type="file" @change="loadTextFromFile">
        </label>
    </div>
</template>

<script>
    export default {

        name: 'yaml',
        props: {

        },
        components: {

        },
        data() {
            return {

            }
        },

        beforeDestroy: function () {

        },
        mounted() {

        },

        watch: {

        },
        methods: {
            loadTextFromFile(ev) {
                const file = ev.target.files[0];
                const reader = new FileReader();

                reader.onload = e => this.$emit("load", e.target.result);
                reader.readAsText(file);
            }
        }
    }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">

</style>
