<template>
    <div>
        <el-header>{{ types }}</el-header>
        <el-button @click="dialogVisible = true">
            ADD
        </el-button>

        <el-dialog
                :title="'YAML Editor'"
                :visible.sync="dialogVisible"
                width="50%"
                :before-close="handleClose">
            <span>Edit Scope : {{types}}</span>
            <codemirror
                    :options="{
                theme: 'darcula',
                mode: 'yaml',
                lineNumbers: true,
                lineWrapping: true,
                }"
                    :value="plainText"
                    v-model="plainText"
                    style="blur: 2px;"
            >
            </codemirror>

            <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="120px" class="demo-ruleForm" style="margin-top: 10px;">
                <el-form-item label="NameSpace" prop="nameSpace">
                    <el-input v-model="ruleForm.nameSpace"></el-input>
                </el-form-item>
                <el-form-item label="Name" prop="podName">
                    <el-input v-model="ruleForm.podName"></el-input>
                </el-form-item>
            </el-form>

            <text-reader @load="plainText = $event"></text-reader>

            <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">Cancel</el-button>
                <el-button type="primary" @click="postYAML">Confirm</el-button>
            </span>
        </el-dialog>

    </div>
</template>

<script>
    import VueTable from 'vuetable-2'
    import dropdown from 'vue-dropdowns';
    import TextReader from "@/components/yaml.vue";
    import yaml from 'js-yaml'
    import json2yaml from 'json2yaml'
    import 'vue-codemirror'
    import 'codemirror/mode/yaml/yaml.js'

    export default {

        name: 'DashBoard',
        props: {
            user: Array,
            types: String
        },
        components: {
            'vuetable': VueTable,
            'dropdown': dropdown,
            TextReader,
            yaml,
            json2yaml
        },
        data() {
            return {
                evtSource: null,
                selectedNamespace: {},
                list: [],
                providerFilters: [],
                dialogVisible: false,
                plainText: "",
                json_data: {},
                yamlText: "",
                ruleForm: {
                    nameSpace : '',
                    podName: ''
                },
                rules: {
                    nameSpace: [
                        { required: true, message: 'Please Input NameSpace', trigger: 'blur' },
                    ],
                    podName: [
                        { required: true, message: 'Please Input podName', trigger: 'blur' },
                    ],
                }


            }
        },

        beforeDestroy: function () {
            var me = this
            // console.log("closing evtSource beforeDestroy");
            // me.evtSource.close();
        },

        mounted() {
            var me = this
            var getURLType
            // if (me.types == 'service') {
            //     getURLType = 'serviceses'
            // } else {
            //     getURLType = me.types + 's'
            // }
            // me.$http.get(`${API_HOST}/pods`)
            //     .then((result) => {
            //         console.log(result)
            //     })
        },
        watch: {
            plainText: function (newVal) {
                var me = this
            },
        },

        methods: {
            // startSSE: function (user) {
            //     var me = this;
            //     var tmp = [];
            //
            //     if (!(user.provider == 'All')) {
            //         me.evtSource = new EventSource(`${API_HOST}/kubesse/?provider=` + user.provider + '&name=' + user.name)
            //     } else {
            //         me.evtSource = new EventSource(`${API_HOST}/kubesse/`)
            //     }
            //     me.evtSource.onmessage = function (e) {
            //
            //
            //     }
            //     me.evtSource.onerror = function (e) {
            //
            //     }
            // },
            handleClose(done) {
                this.$confirm('Are you sure to close this dialog?')
                    .then(_ => {
                        done();
                    })
                    .catch(_ => {
                    });
            },
            notiOpen() {
                this.$notify({
                    title: 'Info',
                    message: 'YAML파일 적용 중 입니다.',
                    type: 'info'
                });
            },
            postYAML() {
                var me = this
                if(me.ruleForm.nameSpace == "" || me.ruleForm.podName == "" || me.plainText == "") {
                    this.$alert('입력값이 부족합니다.', '알림', {
                        confirmButtonText: 'OK',
                    })
                }
                var me = this;
                var jsonYaml = yaml.load(me.plainText)
                var getURLType;
                if (me.types == 'pod') {
                    getURLType = me.types + 's'
                } else {
                    getURLType = me
                }
                me.$http.post(`${API_HOST}/kube/v1/` + getURLType + '/namespaces/' + me.ruleForm.nameSpace + '/' + me.ruleForm.podName, jsonYaml, {headers: {
                    'Content-Type': 'application/json'
                }}
                ).then(function() {
                    me.dialogVisible = false
                    me.notiOpen()
                })

            }
        },
    }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">
    h3 {
        margin: 40px 0 0;
    }

    .table {
        float: left;
        width: 100%;
        margin: 5px;
    }

    ul {
        list-style-type: none;
        padding: 0;
    }

    li {
        display: inline-block;
        margin: 0 10px;
    }

    a {
        color: #42b983;
    }
</style>
