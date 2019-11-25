<template>
    <v-container
    >
<<<<<<< HEAD
        <v-card v-if="value[0]">
            <v-card-title v-if="value[0]">
                {{value[0].name}}
            </v-card-title>
            <v-card-text
                    id="scroll-target"
                    style="max-height: 600px;"
                    class="overflow-auto"
                    v-if="value[0].name.includes('.png')"
            >
                <v-img
                        :src='cmOption'
                        aspect-ratio="1"
                        class="grey lighten-2"
                        max-width="500"
                        max-height="300"
                ></v-img>
            </v-card-text>
            <v-card-text
                    id="scroll-target"
                    style="max-height: 600px;"
                    class="overflow-auto"
                    v-else-if="value[0].name.includes('.jpg')"
            >
                <v-img
                        :src='cmOption'
                        aspect-ratio="1"
                        class="grey lighten-2"
                        max-width="500"
                        max-height="300"
                ></v-img>
            </v-card-text>
            <v-card-text
                    id="scroll-target"
                    style="max-height: 600px;"
                    class="overflow-auto"
                    v-else
            >
                <codemirror
                        v-if="value[0]"
                        ref="codemirror"
                        :value="value[0].code"
=======

        <v-card>
            <v-card-title v-if="value[0][0]">
                {{value[0][0].name}}
            </v-card-title>
            <v-card-text
                    id="scroll-target"
                    style="max-height: 700px"
                    class="overflow-y-auto"
            >
                <codemirror
                        ref="codemirror"
                        :value="code"
>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8
                        :options="cmOption"
                        @ready="onCmReady"
                        @Focus="onCmFocus"
                        @input="onCmCodeChange"
<<<<<<< HEAD
                        height="700"
=======
                        :height="700"
>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8

                >
                </codemirror>
            </v-card-text>
        </v-card>

    </v-container>
</template>
<script>
    import 'codemirror/mode/yaml/yaml'
    import 'codemirror/mode/dockerfile/dockerfile'
    import 'codemirror/mode/markdown/markdown'
    import 'codemirror/mode/properties/properties'
    import 'codemirror/mode/shell/shell'
    import 'codemirror/mode/xml/xml'

    import 'codemirror/lib/codemirror.css'
    import 'codemirror/theme/darcula.css'
    import 'codemirror/mode/clike/clike'
    import 'codemirror/lib/codemirror.css'
    import 'codemirror/theme/darcula.css'

    export default {
        props: {
            value: Array
        },
        data() {
            return {
<<<<<<< HEAD
                // code: '',
                // offsetTop: 0,
=======
                code: '',
                offsetTop: 0,
                definitionList: []
>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8
            }
        },
        computed: {
            cmOption() {
<<<<<<< HEAD
                if (this.value.length > 0) {
                    if (this.value[0].name.includes('.java')) {
=======
                if (this.value[0].length > 0) {
                    if (this.value[0][0].name.includes('.java')) {
>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8
                        var type = {
                            autoCloseBrackets: true,
                            tabSize: 4,
                            lineNumbers: true,
                            line: true,
                            mode: 'text/x-java',
                            theme: 'darcula',
                            lineWrapping: true,
                            matchBrackets: true,
                            scroll: true,
                            readOnly: 'nocursor',
<<<<<<< HEAD
                            scrollbarStyle: null
                        }
                    } else if (this.value[0].name.includes('.yml') || this.value[0].name.includes('.yaml')) {
=======
                            scrollbarStyle : null
                        }
                    } else if (this.value[0][0].name.includes('.yml') || this.value[0][0].name.includes('.yaml')) {
>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8
                        var type = {
                            autoCloseBrackets: true,
                            tabSize: 4,
                            lineNumbers: true,
                            line: true,
                            mode: 'text/x-yaml',
                            theme: 'darcula',
                            lineWrapping: true,
                            matchBrackets: true,
                            scroll: true,
                            readOnly: 'nocursor'
                        }
<<<<<<< HEAD
                    } else if (this.value[0].name.includes('.md')) {
=======
                    } else if (this.value[0][0].name.includes('.md')) {
>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8
                        var type = {
                            autoCloseBrackets: true,
                            tabSize: 4,
                            lineNumbers: true,
                            line: true,
                            mode: 'text/x-markdown',
                            theme: 'darcula',
                            lineWrapping: true,
                            matchBrackets: true,
                            scroll: true,
                            readOnly: 'nocursor'
                        }
<<<<<<< HEAD
                    } else if (this.value[0].name.includes('.properties')) {
=======
                    } else if (this.value[0][0].name.includes('.properties')) {
>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8
                        var type = {
                            autoCloseBrackets: true,
                            tabSize: 4,
                            lineNumbers: true,
                            line: true,
                            mode: 'text/x-properties',
                            theme: 'darcula',
                            lineWrapping: true,
                            matchBrackets: true,
                            scroll: true,
                            readOnly: 'nocursor'
                        }
<<<<<<< HEAD
                    } else if (this.value[0].name.includes('mvnw')) {
=======
                    } else if (this.value[0][0].name.includes('mvnw')) {
>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8
                        var type = {
                            autoCloseBrackets: true,
                            tabSize: 4,
                            lineNumbers: true,
                            line: true,
                            mode: 'text/x-sh',
                            theme: 'darcula',
                            lineWrapping: true,
                            matchBrackets: true,
                            scroll: true,
                            readOnly: 'nocursor'
                        }
<<<<<<< HEAD
                    } else if (this.value[0].name.includes('.xml')) {
=======
                    } else if (this.value[0][0].name.includes('.xml')) {
>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8
                        var type = {
                            autoCloseBrackets: true,
                            tabSize: 4,
                            lineNumbers: true,
                            line: true,
                            mode: 'application/xml',
                            theme: 'darcula',
                            lineWrapping: true,
                            matchBrackets: true,
                            scroll: true,
                            readOnly: 'nocursor'
                        }
<<<<<<< HEAD
                    } else if (this.value[0].name.includes('Dockerfile')) {
                        var type = {
                            autoCloseBrackets: true,
                            tabSize: 4,
                            lineNumbers: true,
                            line: true,
                            mode: 'text/x-dockerfile',
                            theme: 'darcula',
                            lineWrapping: true,
                            matchBrackets: true,
                            scroll: true,
                            readOnly: 'nocursor'
                        }
                    } else if (this.value[0].name.includes('.json')) {
                        var type = {
                            autoCloseBrackets: true,
                            tabSize: 4,
                            lineNumbers: true,
                            line: true,
                            mode: 'application/json',
                            theme: 'darcula',
                            lineWrapping: true,
                            matchBrackets: true,
                            scroll: true,
                            readOnly: 'nocursor'
                        }
                    } else if (this.value[0].name.includes('.png') || this.value[0].name.includes('.jpg') || this.value[0].name.includes('.jpeg')) {

                        var type = `data:image/png;base64,${this.value[0].code}`
                        console.log(type)
                        console.log(this.value[0])
                    } else if (this.value[0].name.includes('.jpg') || this.value[0].name.includes('.jpeg')) {
                        var type = `data:image/jpg;base64,${this.value[0].code}`
                    }
                    return type
=======
                    }
                    return type

>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8
                }
            },
        },
        watch: {
<<<<<<< HEAD
            // value(newVal) {
            //     // console.log()
            //     this.code = ''
            //
            //     this.code = newVal[0][0].code


            // console.log(newVal)
            // let fileName = newVal[0].name[0];
            // let list=newVal[1].value;
            // this.definitionList=list
            // }
        },
        methods: {
            onScroll(e) {
                // this.offsetTop = e.target.scrollTop
=======
            value(newVal) {
                // console.log()
                this.code = ''

                this.code = newVal[0][0].code


                // console.log(newVal)
                // let fileName = newVal[0].name[0];
                // let list=newVal[1].value;
                // this.definitionList=list
            }
        },
        methods: {
            onScroll (e) {
                this.offsetTop = e.target.scrollTop
>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8
            },
            onCmReady(cm) {
                // console.log('the editor is readied!', cm)

            },
            onCmFocus(cm) {
                // console.log('the editor is focus!', cm)
            },
            onCmCodeChange(newCode) {
                // console.log('this is new code', newCode)
                // this.code = newCode
            },
        },
        mounted() {
        }
    }
</script>
<style>
    .CodeMirror {
        height: auto;
    }
</style>
