<template>
    <div>
        <el-card class="box-card">
            <!-- Title -->
            <el-row type="flex" class="row-bg" justify="space-between">
                <el-col :span="6">
                    <div style="font-size: 20px; font-weight: bold;">{{ types.toUpperCase() }}
                    </div>
                </el-col>
                <el-col :span="6">
                </el-col>
                <el-col :span="6">
                    <el-button style='font-align: "right;"' @click="dialogVisible = true; status = 'add'">
                        ADD
                    </el-button>
                </el-col>
            </el-row>


            <!-- Pods Table Start -->
            <el-table
                    v-if="types=='pod'"
                    :data="list"
                    style="width: 100%"
                    height="250"
                    :default-sort="{prop: 'namespace', order: 'ascending'}"
                    v-loading="loading"
            >
                <el-table-column type="expand">
                    <template slot-scope="props">
                        <p>Name: {{ props.row.name }}</p>
                        <p>NameSpace: {{ props.row.namespace }}</p>
                        <p>Type: {{ props.row.type }}</p>
                        <p>CreateTime: {{ props.row.createTimeStamp }}</p>
                        <p>Image: {{ props.row.image }}</p>
                        <p>Node Name: {{ props.row.nodeName }}</p>
                        <p>host Ip: {{ props.row.hostIp }}</p>
                        <p>Pod Ip: {{ props.row.podIp }}</p>
                        <p>Status: {{ props.row.status }}</p>
                        <p>UID: {{ props.row.uid }}</p>
                        <p>provider: {{ props.row.provider }}</p>
                    </template>
                </el-table-column>
                <el-table-column
                        label="Provider"
                        prop="provider"
                        width="80">
                    <template slot-scope="scope">
                        <div slot="reference" class="name-wrapper">
                            <el-tag size="medium">{{ scope.row.provider }}</el-tag>
                        </div>
                    </template>
                </el-table-column>
                <el-table-column
                        label="Name"
                        prop="name">
                </el-table-column>
                <el-table-column
                        label="NameSpace"
                        prop="namespace"
                        sortable
                        width="140"
                >
                </el-table-column>
                <el-table-column
                        label="Status"
                        prop="status"
                        width="80">
                </el-table-column>
                <el-table-column
                        label="Type"
                        prop="type"
                        width="100">
                </el-table-column>
                <el-table-column
                        label="CreateTime"
                        prop="createTimeStamp"
                        width="150">
                </el-table-column>
                <el-table-column
                        label="Image"
                        prop="image">
                </el-table-column>
                <el-table-column
                        label="Operations">
                    <template slot-scope="scope">
                        <el-button
                                size="mini"
                                type="primary"
                                @click="handleEdit(scope.$index, scope.row); status = 'edit'">Edit
                        </el-button>
                        <el-button
                                size="mini"
                                type="danger"
                                @click="handleDelete(scope.$index, scope.row)">Delete
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
            <!-- Pods Table End -->

            <!-- Service Table Start -->
            <el-table
                    v-else-if="types=='service'"
                    :data="list"
                    style="width: 100%"
                    height="250"
                    :default-sort="{prop: 'namespace', order: 'ascending'}"
                    v-loading="loading"
            >
                <el-table-column type="expand">
                    <template slot-scope="props">
                        <p>Name: {{ props.row.name }}</p>
                        <p>NameSpace: {{ props.row.namespace }}</p>
                        <p>Kind: {{ props.row.kind }}</p>
                        <p>Type: {{ props.row.type }}</p>
                        <p>apiVersion: {{ props.row.apiVersion }}</p>
                        <p>CreateTime: {{ props.row.createTimeStamp }}</p>
                        <p>Spec Cluster Ip: {{ props.row.specClusterIp }}</p>
                        <p>Spec Protocol: {{ props.row.specProtocol }}</p>
                        <p>Spec Session Affinity: {{ props.row.specSessionAffinity }}</p>
                        <p>Spec Type: {{ props.row.specType }}</p>
                        <p>HostName: {{ props.row.hostname }}</p>
                        <p>Ingress Ip: {{ props.row.ingressIp }}</p>
                    </template>
                </el-table-column>
                <el-table-column
                        label="Provider"
                        prop="provider"
                        width="80">
                    <template slot-scope="scope">
                        <div slot="reference" class="name-wrapper">
                            <el-tag size="medium">{{ scope.row.provider }}</el-tag>
                        </div>
                    </template>
                </el-table-column>
                <el-table-column
                        label="Name"
                        prop="name">
                </el-table-column>
                <el-table-column
                        label="NameSpace"
                        prop="namespace"
                        sortable
                        width="140"
                >
                </el-table-column>
                <el-table-column
                        label="Kind"
                        prop="kind"
                        width="80">
                </el-table-column>
                <el-table-column
                        label="Type"
                        prop="type"
                        width="140">
                </el-table-column>
                <el-table-column
                        label="CreateTime"
                        prop="createTimeStamp"
                        width="150">
                </el-table-column>
                <el-table-column
                        label="Operations">
                    <template slot-scope="scope">

                        <el-button
                                size="mini"
                                type="primary"
                                @click="handleEdit(scope.$index, scope.row); status = 'edit'">Edit
                        </el-button>
                        <el-button
                                size="mini"
                                type="danger"
                                @click="handleDelete(scope.$index, scope.row)">Delete
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
            <!-- Service Table End -->

            <!-- Deployment Table Start -->
            <el-table
                    v-else-if="types=='deployment'"
                    :data="list"
                    style="width: 100%"
                    height="250"
                    :default-sort="{prop: 'namespace', order: 'ascending'}"
                    v-loading="loading"
            >
                <el-table-column type="expand">
                    <template slot-scope="props">
                        <p>Name: {{ props.row.name }}</p>
                        <p>NameSpace: {{ props.row.namespace }}</p>
                        <p>Kind: {{ props.row.kind }}</p>
                        <p>Type: {{ props.row.type }}</p>
                        <p>apiVersion: {{ props.row.apiVersion }}</p>
                        <p>CreateTime: {{ props.row.createTimeStamp }}</p>
                        <p>Spec Replicas: {{ props.row.specReplicas }}</p>
                        <p>Strategy Type: {{ props.row.strategyType }}</p>
                        <p>UID: {{ props.row.uid }}</p>

                        <p v-if="props.row.statusReplicas != null">Status Replicas: {{ props.row.statusReplicas }}</p>
                        <p v-if="props.row.statusAvailableReplicas != null">Status Available Replicas: {{
                            props.row.statusAvailableReplicas }}</p>
                        <p v-if="props.row.statusReadyReplicas != null">Status Ready Replicas: {{
                            props.row.statusReadyReplicas }}</p>
                        <p v-if="props.row.statusUpdateReplicas != null">Status Update Replicas: {{
                            props.row.statusUpdateReplicas }}</p>
                    </template>
                </el-table-column>
                <el-table-column
                        label="Provider"
                        prop="provider"
                        width="80">
                    <template slot-scope="scope">
                        <div slot="reference" class="name-wrapper">
                            <el-tag size="medium">{{ scope.row.provider }}</el-tag>
                        </div>
                    </template>
                </el-table-column>
                <el-table-column
                        label="Name"
                        prop="name">
                </el-table-column>
                <el-table-column
                        label="NameSpace"
                        prop="namespace"
                        sortable
                        width="140"
                >
                </el-table-column>
                <el-table-column
                        label="Kind"
                        prop="kind"
                        width="140">
                </el-table-column>
                <el-table-column
                        label="Type"
                        prop="type"
                        width="140">
                </el-table-column>
                <el-table-column
                        label="CreateTime"
                        prop="createTimeStamp"
                        width="150">
                </el-table-column>
                <el-table-column
                        label="Operations">
                    <template slot-scope="scope">
                        <!--<el-button-->
                        <!--size="mini"-->
                        <!--@click="handleEdit(scope.$index, scope.row)">Edit</el-button>-->
                        <el-button
                                size="mini"
                                type="primary"
                                @click="handleEdit(scope.$index, scope.row); status = 'edit'">Edit
                        </el-button>
                        <el-button
                                size="mini"
                                type="danger"
                                @click="handleDelete(scope.$index, scope.row)">Delete
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
            <!-- Deployment Table End -->

            <!-- Dial Button -->
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
                >
                </codemirror>

                <el-form v-if="status == 'add'" :model="ruleForm" :rules="rules" ref="ruleForm" label-width="120px"
                         class="demo-ruleForm"
                         style="margin-top: 10px;">
                    <el-form-item label="NameSpace" prop="nameSpace">
                        <el-input v-model="ruleForm.nameSpace"></el-input>
                    </el-form-item>
                </el-form>

                <el-form v-else :model="ruleForm" :rules="rules" ref="ruleForm" label-width="120px"
                         class="demo-ruleForm"
                         style="margin-top: 10px;">
                    <el-form-item label="NameSpace" prop="nameSpace">
                        <el-input :disabled="true" v-model="ruleForm.nameSpace"></el-input>
                    </el-form-item>
                </el-form>

                <text-reader @load="plainText = $event"></text-reader>

                <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">Cancel</el-button>
                <el-button type="primary" @click="postYAML">Confirm</el-button>
            </span>
            </el-dialog>
        </el-card>
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
                list: [],
                providerFilters: [],
                dialogVisible: false,
                plainText: "",
                selectedRow: "",
                loading: false,
                yamlText: "",
                status: '',
                ruleForm: {
                    nameSpace: '',
                    name: ''
                },
                rules: {
                    nameSpace: [
                        {required: false, message: 'Please Input NameSpace', trigger: 'blur'},
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
            if (me.types == 'pod') {
                getURLType = me.types + 's'
            } else {
                getURLType = me.types
            }


            /*
                    TODO : 현재 Default만 받아오도록 설정되어있음.
            */

            me.$http.get(`${API_HOST}/kube/v1/` + getURLType + '/namespaces/default')
                .then((result) => {
                    var tmpData = result.data
                    var resultMap = []
                    var usedUid = []
                    return new Promise((resolve, reject) => {
                        tmpData.forEach(function (sortingData) {
                            if (!(usedUid.includes(sortingData.uid))) {
                                resultMap.push(sortingData)
                                usedUid.push(sortingData.uid)
                            } else {
                                resultMap.forEach(function (resultMapTmp, index) {
                                    if (resultMapTmp.uid == sortingData.uid) {
                                        if (resultMapTmp.type == 'DELETED') {
                                            resultMap = [
                                                ...resultMap.slice(0, index),
                                                resultMapTmp,
                                                ...resultMap.slice(index + 1)
                                            ]
                                        } else if (sortingData.type == 'DELETED') {
                                            resultMap = [
                                                ...resultMap.slice(0, index),
                                                sortingData,
                                                ...resultMap.slice(index + 1)
                                            ]
                                        } else {
                                            if (Date.parse(resultMapTmp.createTime) < Date.parse(sortingData.createTime)) {
                                                resultMap = [
                                                    ...resultMap.slice(0, index),
                                                    sortingData,
                                                    ...resultMap.slice(index + 1)
                                                ]
                                            }
                                        }
                                    }
                                })
                            }
                        })
                        resolve(resultMap)
                    }).then(function (resolveData) {
                        var deleteItemList = []
                        // console.log(resolveData)
                        resolveData.forEach(function (deleteTmp, index) {
                            if (deleteTmp.type == 'DELETED') {
                                deleteItemList.push(deleteTmp)
                            }
                        })
                        me.startSSE()
                        me.list = _.difference(resolveData, deleteItemList)
                    })
                })
        },
        watch: {
            plainText: function (newVal) {
                var me = this
            },
        },

        methods: {
            startSSE: function () {
                var me = this;
                // var tmp = [];

                me.evtSource = new EventSource(`${API_HOST}/kubesse/?instanceType=` + me.types)

                /*
                    TODO : 이벤트 수정
                 */
                me.evtSource.onmessage = function (e) {
                    // console.log(e.data)
                    var parseMessage = JSON.parse(e.data);
                    var tmpData = JSON.parse(parseMessage.message)
                    var listUidTmp = [];

                    me.list.forEach(function (listData) {
                        listUidTmp.push(listData.uid)
                    });

                    me.list.some(function (listTmp, index) {
                        if (listTmp.uid == tmpData.uid) {
                            if (tmpData.type == 'DELETED') {
                                me.list = [
                                    ...me.list.slice(0, index),
                                    ...me.list.slice(index + 1)
                                ]
                                me.loading = false;
                                return;
                            } else {
                                me.list = [
                                    ...me.list.slice(0, index),
                                    tmpData,
                                    ...me.list.slice(index + 1)
                                ]
                                return;
                            }
                        } else if (!listUidTmp.includes(tmpData.uid)) {
                            if (!(tmpData.type == 'DELETED')) {
                                me.list.push(tmpData)
                                listUidTmp.push(tmpData.uid)
                                return;
                            }
                        }
                    })

                }
                me.evtSource.onerror = function (e) {
                    me.evtSource.close();
                    me.startSSE();
                }
            },
            handleDelete(index, row) {
                var me = this
                var getURLType;

                this.$confirm('삭제하시겠습니까?', 'Warning', {
                    confirmButtonText: 'OK',
                    cancelButtonText: 'Cancel',
                    type: 'warning'
                }).then(() => {
                    if (me.types == 'pod') {
                        getURLType = me.types + 's'
                    } else {
                        getURLType = me.types
                    }
                    // console.log(row)
                    me.$http.delete(`${API_HOST}/kube/v1/` + getURLType + '/namespaces/' + row.namespace + '/' + row.name, {
                            headers: {
                                'Content-Type': 'application/json'
                            }
                        }
                    ).then(function () {
                        me.list = [
                            ...me.list.slice(0, index),
                            ...me.list.slice(index + 1)
                        ]
                        me.$notify({
                            title: 'Success',
                            message: '삭제되었습니다. 적용 중 입니다.',
                            type: 'Success'
                        });
                        me.loading = true;
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: 'Delete canceled'
                    });
                });
            },
            handleEdit(index, row) {
                var me = this
                var getURLType;
                me.dialogVisible = true
                var yamlData = row.sourceData
                me.selectedRow = row
                me.plainText = json2yaml.stringify(JSON.parse(yamlData)).replace(/- \n[ ]+/g, '- ').replace(/\\"/g, '\'')
                me.ruleForm.nameSpace = row.namespace
            },
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
                var nameSpace
                if (me.ruleForm.nameSpace == "") {
                    nameSpace = 'default'
                } else {
                    nameSpace = me.ruleForm.nameSpace
                }

                if (me.plainText == "") {
                    this.$alert('입력값이 부족합니다.', '알림', {
                        confirmButtonText: 'OK',
                    })
                }
                var me = this;
                // console.log(yaml.load(me.plainText))

                // var lines = me.plainText.split('\n')
                //
                // lines.splice(0,1)
                // for (var i in lines){
                //     lines[i] = lines[i].substring(2,lines[i].length)
                // }
                // var yamltext = lines.join('\n')

                var jsonYaml = yaml.load(me.plainText)

                console.log(jsonYaml)

                var getURLType;
                if (me.types == 'pod') {
                    getURLType = me.types + 's'
                } else {
                    getURLType = me.types
                }
                // console.log(jsonYaml)
                if (me.status == 'add') {
                    me.$http.post(`${API_HOST}/kube/v1/` + getURLType + '/namespaces/' + nameSpace, jsonYaml, {
                            headers: {
                                'Content-Type': 'application/json'
                            }
                        }
                    ).then(function () {
                        me.dialogVisible = false
                        me.notiOpen()
                        me.status = ''

                    })
                } else if (me.status == 'edit') {
                    me.$http.put(`${API_HOST}/kube/v1/` + getURLType + '/namespaces/' + nameSpace + '/' + me.selectedRow.name, jsonYaml, {
                            headers: {
                                'Content-Type': 'application/json'
                            }
                        }
                    ).then(function () {
                        me.dialogVisible = false
                        me.notiOpen()
                        me.status = ''
                    })
                }
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
