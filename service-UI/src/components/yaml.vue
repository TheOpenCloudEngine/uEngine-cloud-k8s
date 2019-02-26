<template>
    <div class="yaml" style="text-align:center; margin: 10px">
        <input type="file" @change="loadTextFromFile"/>
    </div>
</template>

<script>
    export default {

        name: 'yaml',
        props: {
            plainText: String
        },
        components: {

        },
        data() {
            return {
                fileName: '',
                single: ''
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

                this.$emit('update:plainText', '')

                const file = ev.target.files[0];
                const reader = new FileReader();

                this.fileName = file.name;
                this.$emit('update:fileName', this.fileName)
                reader.onload = e => this.$emit("load", e.target.result);
                reader.readAsText(file);
            }
        }
    }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">

</style>
