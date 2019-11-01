<template>
    <v-container
    >

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
                        :options="cmOption"
                        @ready="onCmReady"
                        @Focus="onCmFocus"
                        @input="onCmCodeChange"
                        :height="700"

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
                code: '',
                offsetTop: 0,
                definitionList: []
            }
        },
        computed: {
            cmOption() {
                if (this.value[0].length > 0) {
                    if (this.value[0][0].name.includes('.java')) {
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
                            scrollbarStyle : null
                        }
                    } else if (this.value[0][0].name.includes('.yml') || this.value[0][0].name.includes('.yaml')) {
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
                    } else if (this.value[0][0].name.includes('.md')) {
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
                    } else if (this.value[0][0].name.includes('.properties')) {
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
                    } else if (this.value[0][0].name.includes('mvnw')) {
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
                    } else if (this.value[0][0].name.includes('.xml')) {
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
                    }
                    return type

                }
            },
        },
        watch: {
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
