<template>
    <div>
        <v-layout row style="margin-bottom: 10px;">
            <v-flex
                    grow
                    pa-1
            >
                <v-text-field
                        v-model="search"
                        append-icon="search"
                        label="Search"
                        single-line
                        hide-details
                ></v-text-field>
            </v-flex>
            <v-flex
                    shrink
                    pa-1
            >
                <v-btn color="info" @click="codeModalShow(); status = 'add'">ADD</v-btn>

            </v-flex>
        </v-layout>
        <!-- Title -->
        <v-data-table
                :rows-per-page-items="pageItems"
                :headers="headers"
                :items="list"
                class="elevation-1"
                :loading="tableLoad"
                :search="search"
        >
            <template slot="items" slot-scope="props">
                <td>{{ props.item.name }}</td>
                <td>{{ props.item.namespace }}</td>
                <!-- pod Column -->
                <td class="text-xs-left" v-if="types == 'pod'">{{ props.item.status }}</td>
                <td class="text-xs-center" v-if="types == 'pod'">{{ props.item.createTimeStamp }}</td>
                <!-- Deployment Column -->
                <td class="text-xs-center" v-if="types == 'deployment'">{{ props.item.statusReadyReplicas }}</td>
                <td class="text-xs-center" v-if="types == 'deployment'">{{ props.item.statusReplicas }}</td>
                <td class="text-xs-center" v-if="types == 'deployment'">{{ props.item.statusUpdateReplicas }}</td>
                <td class="text-xs-center" v-if="types == 'deployment'">{{ props.item.statusAvailableReplicas }}</td>
                <td class="text-xs-center" v-if="types == 'deployment'">{{ props.item.createTimeStamp }}</td>
                <!-- Service Column -->
                <td class="text-xs-left" v-if="types == 'service'">{{ props.item.specType }}</td>
                <td class="text-xs-left" v-if="types == 'service'">{{ props.item.specClusterIp }}</td>
                <td class="text-xs-left" v-if="types == 'service'">{{ props.item.ingressIp }}</td>
                <td class="text-xs-left" v-if="types == 'service'">{{ props.item.specPort }}</td>
                <td class="text-xs-center" v-if="types == 'service'">{{ props.item.createTimeStamp }}</td>
                <td class="justify-center layout px-0">
                    <v-icon
                            small
                            class="mr-2"
                            @click="handleEdit(props.item); status = 'edit'"
                    >
                        edit
                    </v-icon>
                    <v-icon
                            small
                            @click="deleteModalShow(props.item)"
                    >
                        delete
                    </v-icon>
                </td>
            </template>
            <template slot="no-data">
            </template>
        </v-data-table>

        <!-- Edit & Add Modal -->
        <modal ref="modal" name="codeModal" :height='"60%"' :width="'80%'">
            <v-layout justify-space-between>
                <v-flex>
                    <v-card-title class="headline">
                        {{types.toUpperCase()}} Editor
                    </v-card-title>
                </v-flex>
                <v-flex style="text-align: right">
                    <v-btn fab flat @click="codeModalhide">
                        <v-icon>
                            clear
                        </v-icon>
                    </v-btn>
                </v-flex>
            </v-layout>
            <EditYaml :status="status" :plainText.sync="plainText" :types="types"></EditYaml>
        </modal>

        <!-- Delete Modal -->
        <modal scrollable ref="modal" name="deleteModal" :height='"auto"' :width='500'>
            <v-card>
                <v-card-title primary-title>
                    <div>
                        <div class="headline">삭제 안내</div>
                        <span class="text-capitalize"> {{ deleteName }}를 삭제하시겠습니까?</span>
                    </div>
                </v-card-title>

                <v-card-actions>
                    <v-btn color="primary" @click="deleteModalhide">Close</v-btn>
                    <v-btn color="primary" @click="handleDelete(selectedRow)">Confirm</v-btn>
                </v-card-actions>
            </v-card>
        </modal>

        <v-snackbar
                v-model="snackbar.status"
                :bottom="snackbar.y === 'bottom'"
                :top="snackbar.y === 'top'"
                :left="snackbar.x === 'left'"
                :right="snackbar.x === 'right'"
                :timeout="snackbar.timeout"
                :color="snackbar.color"
        >
            {{ snackbar.text }}
            <v-btn
                    dark
                    flat
                    @click="snackbar.status = false"
            >
                Close
            </v-btn>
        </v-snackbar>
    </div>
</template>

<script>
    // import EditYaml from "@/components/edityamlpage.vue";
    import yaml from 'js-yaml'
    import json2yaml from 'json2yaml'
    import {codemirror} from 'vue-codemirror'
    import 'codemirror/lib/codemirror.css'
    import 'codemirror/theme/darcula.css'
    import 'codemirror/mode/yaml/yaml.js'


    export default {

        name: 'DashBoard',
        props: {
            namespace: String,
            namespaceList: Array,
            types: String
        },
        components: {
            yaml,
            json2yaml,
            codemirror
        },
        created() {
            var me = this
            me.$EventBus.$on('deployStart', function () {
                me.postYAML()
            });
        },
        data() {
            return {
                pageItems: [10, 25, {"text": "$vuetify.dataIterator.rowsPerPageAll", "value": -1}],
                evtSource: null,
                cmOption: {
                    tabSize: 4,
                    mode: 'yaml',
                    theme: 'darcula',
                    lineNumbers: true,
                    lineWrapping: true,
                    viewportMargin: 20

                },
                modalStatus: false,
                tableLoad: false,
                list: [],
                search: '',
                visible: false,
                plainText: "",
                selectedRow: [],
                loading: false,
                yamlText: "",
                status: '',
                snackbar: {
                    status: false,
                    y: 'top',
                    x: null,
                    mode: '',
                    timeout: 6000,
                    text: ''
                },
            }
        },
        beforeDestroy: function () {
            var me = this
            console.log("closing evtSource beforeDestroy");
            this.evtSource.close();
            me.$EventBus.$off('deployStart')

        },
        computed: {
            codemirror: function () {
                return this.$refs.myCm.codemirror;
            },
            deleteName() {
                return this.selectedRow.name
            },
            deleteNamespace() {
                return this.selectedRow.namespace
            },
            headers() {
                if (this.types == 'pod') {
                    var result = [
                        {
                            text: 'Name',
                            align: 'left',
                            sortable: false,
                            value: 'name'
                        },
                        {text: 'Namespace', value: 'namespace', align: 'left', sortable: false},
                        {text: 'Status', value: 'status', sortable: false},
                        {text: 'Create Time', value: 'createTimeStamp', align: 'center', sortable: false},
                        {text: 'Action', value: 'name', sortable: false, align: 'center'},
                    ]
                    return result
                } else if (this.types == 'deployment') {
                    var result = [
                        {
                            text: 'Name',
                            align: 'left',
                            sortable: false,
                            value: 'name'
                        },
                        {text: 'Namespace', value: 'namespace', align: 'left', sortable: false},
                        {text: 'Desire', value: 'statusReadyReplicas', align: 'center', sortable: false},
                        {text: 'Current', value: 'Current', align: 'center', sortable: false},
                        {text: 'Up-To-Date', value: 'Up-to-Date', align: 'center', sortable: false},
                        {text: 'Available', value: 'Available', align: 'center', sortable: false},
                        {text: 'Create Time', value: 'createTimeStamp', align: 'center', sortable: false},
                        {text: 'Action', value: 'name', sortable: false, align: 'center'},
                    ]
                    return result

                } else if (this.types == 'service') {
                    var result = [
                        {
                            text: 'Name',
                            align: 'left',
                            sortable: false,
                            value: 'name'
                        },
                        {text: 'Namespace', value: 'namespace', align: 'left', sortable: false},
                        {text: 'Type', value: 'type', align: 'left', sortable: false},
                        {text: 'Cluster IP', value: 'cluster-ip', align: 'center', sortable: false},
                        {text: 'External IP', value: 'external-ip', align: 'center', sortable: false},
                        {text: 'PORT(S)', value: 'ports', align: 'center', sortable: false},
                        {text: 'Create Time', value: 'createTimeStamp', align: 'center', sortable: false},
                        {text: 'Action', value: 'name', sortable: false, align: 'center'},
                    ]
                    return result
                }
            }
        },
        mounted() {
            this.getList()
        },
        watch: {
            namespace: function () {
                this.getList()
            },
            plainText: function (newVal) {
                console.log('newVal!')
            },
        },

        methods: {
            codeModalShow() {
                this.plainText = '';
                this.$modal.show('codeModal');
            },
            codeModalhide() {
                this.$modal.hide('codeModal');
            },
            deleteModalShow(item) {
                this.selectedRow = item
                this.$modal.show('deleteModal');
            },
            deleteModalhide() {
                this.$modal.hide('deleteModal');
            },
            startSSE: function () {
                var me = this;
                // var tmp = [];
                if (me.evtSource != null) {
                    me.evtSource.close()
                }

                if (me.namespace == 'All') {
                    me.evtSource = new EventSource(`${API_HOST}/kubesse/?instanceType=` + me.types)
                } else if (me.namespace != null) {
                    me.evtSource = new EventSource(`${API_HOST}/kubesse/?instanceType=` + me.types + '&namespace=' + me.namespace)
                }

                /*
                    TODO : 이벤트 수정
                 */
                me.evtSource.onmessage = function (e) {
                    var parseMessage = JSON.parse(e.data);
                    var tmpData = JSON.parse(parseMessage.message)
                    console.log(tmpData)
                    var listIdTmp = [];

                    me.list.forEach(function (listData) {
                        listIdTmp.push(listData.id)
                    });
                    if(tmpData.apiVersion == 'DELETED') {
                        me.list.some(function (listTmp, index) {
                            if (listTmp.name == tmpData.name && listTmp.namespace == tmpData.namespace) {
                                me.list = [
                                    ...me.list.slice(0, index),
                                    ...me.list.slice(index + 1)
                                ]
                                me.tableLoad = false;
                                return;
                            }
                        })
                    } else {
                        me.list.some(function (listTmp, index) {
                            if (listTmp.id == tmpData.id) {
                                me.list = [
                                    ...me.list.slice(0, index),
                                    tmpData,
                                    ...me.list.slice(index + 1)
                                ]
                                return;
                            } else if (!listIdTmp.includes(tmpData.id)) {
                                if (!(tmpData.apiVersion == 'DELETED')) {
                                    me.list.push(tmpData)
                                    listIdTmp.push(tmpData.id)
                                    return;
                                }
                            }
                        })
                    }
                }
                me.evtSource.onerror = function (e) {
                    me.evtSource.close();

                    setTimeout(function() {
                        me.startSSE()
                    }, 2000)
                }
            },
            getList() {
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
                if (me.namespace == 'All') {
                    me.$http.get(`${API_HOST}/kube/v1/` + getURLType)
                        .then((result) => {
                            var tmpData = result.data
                            var resultMap = []
                            var usedId = []
                            return new Promise((resolve, reject) => {
                                tmpData.forEach(function (sortingData) {
                                    if (!me.namespaceList.includes(sortingData.namespace)) {
                                        me.namespaceList.push(sortingData.namespace)
                                    }
                                    if (!(usedId.includes(sortingData.id))) {
                                        resultMap.push(sortingData)
                                        usedId.push(sortingData.id)
                                    } else {
                                        resultMap.forEach(function (resultMapTmp, index) {
                                            if (resultMapTmp.id == sortingData.id) {
                                                if (resultMapTmp.apiVersion == 'DELETED') {
                                                    resultMap = [
                                                        ...resultMap.slice(0, index),
                                                        resultMapTmp,
                                                        ...resultMap.slice(index + 1)
                                                    ]
                                                } else if (sortingData.apiVersion == 'DELETED') {
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
                                    if (deleteTmp.apiVersion == 'DELETED') {
                                        deleteItemList.push(deleteTmp)
                                    }
                                })
                                // me.evtSource.close()
                                me.startSSE()
                                me.list = _.difference(resolveData, deleteItemList)

                                // me.searched = me.list
                                me.$emit('update:namespaceList', me.namespaceList)

                            })
                        })
                } else if (me.namespace != 'All') {
                    me.$http.get(`${API_HOST}/kube/v1/` + getURLType + '/namespaces/' + me.namespace)
                        .then((result) => {
                            var tmpData = result.data
                            var resultMap = []
                            var usedId = []
                            return new Promise((resolve, reject) => {
                                tmpData.forEach(function (sortingData) {
                                    if (!(usedId.includes(sortingData.id))) {
                                        resultMap.push(sortingData)
                                        usedId.push(sortingData.id)
                                    } else {
                                        resultMap.forEach(function (resultMapTmp, index) {
                                            if (resultMapTmp.id == sortingData.id) {
                                                if (resultMapTmp.apiVersion == 'DELETED') {
                                                    resultMap = [
                                                        ...resultMap.slice(0, index),
                                                        resultMapTmp,
                                                        ...resultMap.slice(index + 1)
                                                    ]
                                                } else if (sortingData.apiVersion == 'DELETED') {
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
                                    if (deleteTmp.apiVersion == 'DELETED') {
                                        deleteItemList.push(deleteTmp)
                                    }
                                })

                                me.startSSE()
                                me.list = _.difference(resolveData, deleteItemList)
                                // me.searched = me.list
                            })
                        })
                }
            },
            handleDelete(item) {
                var me = this
                var getURLType
                console.log(item)
                if (me.types == 'pod') {
                    getURLType = me.types + 's'
                } else {
                    getURLType = me.types
                }
                me.$http.delete(`${API_HOST}/kube/v1/` + getURLType + '/namespaces/' + item.namespace + '/' + item.name, {
                        headers: {
                            'Content-Type': 'application/json'
                        }
                    }
                ).then(function () {
                    me.snackbar.text = me.types.toUpperCase() + '삭제 진행중입니다.'
                    me.snackbar.x = 'right'
                    me.snackbar.y = 'top'
                    me.snackbar.timeout = 6000
                    me.snackbar.status = true
                    me.snackbar.color = 'cyan darken-2'
                    me.tableLoad = true;

                    me.status = ''
                    me.deleteModalhide()
                })

            },
            handleEdit(item) {
                var me = this
                // me.visible = true
                me.codeModalShow()
                var yamlData = item.sourceData
                me.selectedRow = item
                me.plainText = json2yaml.stringify(JSON.parse(yamlData)).replace(/- \n[ ]+/g, '- ').replace(/\\"/g, '\'')

            },
            postYAML() {
                var me = this
                me.$EventBus.$emit('postYAML')
                console.log('start PostYAML')
                var nameSpace = me.namespace;
                if (nameSpace == 'All') {
                    nameSpace = 'default'
                }

                if (me.status == 'edit') {
                    me.snackbar.text = me.types.toUpperCase() + '수정 진행중입니다.'
                    me.snackbar.x = 'right'
                    me.snackbar.y = 'top'
                    me.snackbar.timeout = 6000
                    me.snackbar.color = 'info'
                    me.snackbar.status = true
                } else if (me.status == 'add') {
                    me.snackbar.text = me.types.toUpperCase() + '추가 진행중입니다.'
                    me.snackbar.x = 'right'
                    me.snackbar.y = 'top'
                    me.snackbar.timeout = 6000
                    me.snackbar.color = 'info'
                    me.snackbar.status = true
                }

                var me = this;

                var jsonYaml = yaml.load(me.plainText)

                me.codeModalhide()

                var getURLType;
                if (me.types == 'pod') {
                    getURLType = me.types + 's'
                } else {
                    getURLType = me.types
                }

                if (me.status == 'add') {
                    me.$http.post(`${API_HOST}/kube/v1/` + getURLType + '/namespaces/' + nameSpace, jsonYaml, {
                            headers: {
                                'Content-Type': 'application/json'
                            }
                        }
                    ).then(function () {

                        me.snackbar.text = me.types.toUpperCase() + ' 추가되었습니다.'
                        me.snackbar.x = 'right'
                        me.snackbar.y = 'top'
                        me.snackbar.timeout = 6000
                        me.snackbar.status = true
                        me.snackbar.color = 'success'

                        me.status = ''

                    })
                } else if (me.status == 'edit') {
                    me.$http.put(`${API_HOST}/kube/v1/` + getURLType + '/namespaces/' + nameSpace + '/' + me.selectedRow.name, jsonYaml, {
                            headers: {
                                'Content-Type': 'application/json'
                            }
                        }
                    ).then(function () {
                        me.snackbar.text = me.types.toUpperCase() + '수정 되었습니다.'
                        me.snackbar.x = 'right'
                        me.snackbar.y = 'top'
                        me.snackbar.timeout = 6000
                        me.snackbar.status = true
                        me.snackbar.color = 'success'

                        me.status = ''
                    })
                }
            }
        },
    }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="css">
    .CodeMirror {
        height: 500px !important;
    }

    .v-card__text {
        padding: 0px !important;
    }

    .v-card__actions {
        text-align: right !important;
    }
</style>
