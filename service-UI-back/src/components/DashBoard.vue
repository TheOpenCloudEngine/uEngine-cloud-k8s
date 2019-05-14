<template>
    <div>
        <!-- Title -->
        <div class="md-layout md-alignment-center-right">
            <md-button class="md-raised md-primary" @click="active = true; status = 'add'">ADD</md-button>
        </div>
        <!-- Pods Table Start -->
        <md-table v-model="searched" md-sort="name" md-sort-order="asc" md-card md-fixed-header @md-selected="onSelect"
                  v-if="types=='pod'">
            <md-table-toolbar>
                <div class="md-toolbar-section-start">
                    <h1 class="md-title">{{types.toUpperCase()}}</h1>
                </div>

                <md-field md-clearable class="md-toolbar-section-end">
                    <md-input placeholder="Search by name..." v-model="search" @input="searchOnTable"/>
                </md-field>
            </md-table-toolbar>

            <md-table-empty-state
                    md-label="No users found"
                    :md-description="`No {{ types }} found for this '${search}' query. Try a different search term or create a new user.`">
                <md-button class="md-primary md-raised">Create New User</md-button>
            </md-table-empty-state>
            <md-table-row slot="md-table-row" slot-scope="{ item }" md-selectable="single" style="text-align: left">
                <md-table-cell md-label="Name" md-sort-by="name"> {{ item.name }}</md-table-cell>
                <md-table-cell md-label="Status" md-sort-by="status">{{ item.status }}</md-table-cell>
                <md-table-cell md-label="Image" md-sort-by="image">{{ item.image }}</md-table-cell>
                <md-table-cell md-label="Host IP" md-sort-by="hostIp">{{ item.hostIp }}</md-table-cell>
                <md-table-cell md-label="Action" style="min-width: 158px;">
                    <div>
                        <md-button class="md-fab md-mini md-primary" @click="handleEdit(item); status = 'edit'">
                            <md-icon>edit</md-icon>
                        </md-button>
                        <md-button class="md-fab md-mini md-accent">
                            <md-icon @click="handleDelete(scope.$index, scope.row);">delete</md-icon>
                        </md-button>
                    </div>
                </md-table-cell>


            </md-table-row>

        </md-table>

        <md-table v-model="searched" md-sort="name" md-sort-order="asc" md-card md-fixed-header @md-selected="onSelect"
                  v-if="types=='service'">
            <md-table-toolbar>
                <div class="md-toolbar-section-start">
                    <h1 class="md-title">{{types.toUpperCase()}}</h1>
                </div>

                <md-field md-clearable class="md-toolbar-section-end">
                    <md-input placeholder="Search by name..." v-model="search" @input="searchOnTable"/>
                </md-field>
            </md-table-toolbar>

            <md-table-empty-state
                    md-label="No users found"
                    :md-description="`No user found for this '${search}' query. Try a different search term or create a new user.`">
                <md-button class="md-primary md-raised">Create New User</md-button>
            </md-table-empty-state>

            <md-table-row slot="md-table-row" slot-scope="{ item }" md-selectable="single" style="text-align: left">
                <md-table-cell md-label="Name" md-sort-by="name">{{ item.name }}</md-table-cell>
                <md-table-cell md-label="SpecType" md-sort-by="specType">{{ item.specType }}</md-table-cell>
                <md-table-cell md-label="Type" md-sort-by="type">{{ item.type }}</md-table-cell>
                <md-table-cell md-label="CreateTimeStamp" md-sort-by="createTimeStamp">{{ item.createTimeStamp }}
                </md-table-cell>
                <md-table-cell md-label="Action">
                    <div>
                        <md-button class="md-fab md-mini md-primary" @click="handleEdit(item); status = 'edit'">
                            <md-icon>edit</md-icon>
                        </md-button>
                        <md-button class="md-fab md-mini md-accent">
                            <md-icon @click="handleDelete(scope.$index, scope.row);">delete</md-icon>
                        </md-button>
                    </div>
                </md-table-cell>

            </md-table-row>
        </md-table>

        <md-table v-model="searched" md-sort="name" md-sort-order="asc" md-card md-fixed-header @md-selected="onSelect"
                  v-if="types=='deployment'">
            <md-table-toolbar>
                <div class="md-toolbar-section-start">
                    <h1 class="md-title">{{types.toUpperCase()}}</h1>
                </div>

                <md-field md-clearable class="md-toolbar-section-end">
                    <md-input placeholder="Search by name..." v-model="search" @input="searchOnTable"/>
                </md-field>
            </md-table-toolbar>

            <md-table-empty-state
                    md-label="No users found"
                    :md-description="`No user found for this '${search}' query. Try a different search term or create a new user.`">
                <md-button class="md-primary md-raised">Create New User</md-button>
            </md-table-empty-state>
            <md-table-row slot="md-table-row" slot-scope="{ item }" md-selectable="single" style="text-align: left">
                <md-table-cell md-label="Name" md-sort-by="name">{{ item.name }}</md-table-cell>
                <md-table-cell md-label="Replicas" md-sort-by="statusReplicas" md-numeric>{{ item.statusReplicas }}
                </md-table-cell>
                <md-table-cell md-label="strategyType" md-sort-by="strategyType">{{ item.strategyType }}</md-table-cell>
                <md-table-cell md-label="createTimeStamp" md-sort-by="createTimeStamp">{{ item.createTimeStamp }}
                </md-table-cell>
            </md-table-row>
        </md-table>

        <!-- Dial Button -->

        <md-dialog
                :md-active.sync="active"
                style="height: 800px;"
        >
            <md-dialog-content style="max-width: 1000px;">
                <codemirror
                        ref="mycm"
                        :options="
                cmOptions
                "
                        :value="plainText"
                        v-model="plainText"
                        style="postion: absolute"
                        @ready="onCmReady"
                        @focus="onFocus"
                        @close="onClose"
                >
                </codemirror>

            </md-dialog-content>

            <text-reader @load="plainText = $event" style="width: 900px !important;"></text-reader>



            <md-dialog-actions>
                <md-button class="md-raised md-primary" @click="active = false; plainText= ''">Cancel</md-button>
                <md-button class="md-raised md-primary" @click="postYAML">Confirm</md-button>
            </md-dialog-actions>
        </md-dialog>

        <md-snackbar :md-position="position" :md-duration="isInfinity ? Infinity : duration"
                     :md-active.sync="showAddSnackbar" md-persistent>
            <span>{{types.toUpperCase()}} 생성 시작 되었습니다.</span>
            <md-button class="md-primary" @click="showAddSnackbar = false">확인</md-button>
        </md-snackbar>

        <md-snackbar :md-position="position" :md-duration="isInfinity ? Infinity : duration"
                     :md-active.sync="showAddSnackbar" md-persistent>
            <span>{{types.toUpperCase()}} 수정 되었습니다.</span>
            <md-button class="md-primary" @click="showEditSnackbar = false">확인</md-button>
        </md-snackbar>

        <md-snackbar :md-position="position" :md-duration="isInfinity ? Infinity : duration"
                     :md-active.sync="showAddSnackbar" md-persistent>
            <span>{{selected.name}} 삭제하였습니다.</span>
            <md-button class="md-primary" @click="showEditSnackbar = false">확인</md-button>
        </md-snackbar>
    </div>
</template>

<script>
    import TextReader from "@/components/yaml.vue";
    import yaml from 'js-yaml'
    import json2yaml from 'json2yaml'
    import 'vue-codemirror'
    import 'codemirror/mode/yaml/yaml.js'

    const toLower = text => {
        return text.toString().toLowerCase()
    }

    const searchByName = (items, term) => {
        if (term) {
            return items.filter(item => toLower(item.name).includes(toLower(term)))
        }

        return items
    }

    export default {

        name: 'DashBoard',
        props: {
            namespace: String,
            namespaceList: Array,
            types: String
        },
        components: {
            TextReader,
            yaml,
            json2yaml
        },
        created() {
        },
        data() {
            return {
                evtSource: null,
                cmOptions: {
                    tabSize: 4,
                    mode: 'yaml',
                    theme: 'darcula',
                    lineNumbers: true,
                    line: true,
                    viewportMargin: 15,
                },
                list: [],
                selected: '',
                searched: [],
                search: null,
                providerFilters: [],
                plainText: "",
                selectedRow: "",
                loading: false,
                yamlText: "",
                status: '',
                active: false,
                position: 'center',
                duration: 4000,
                isInfinity: false,
                showAddSnackbar: false,
                showEditSnackbar: false
            }
        },

        beforeDestroy: function () {
            var me = this
            console.log("closing evtSource beforeDestroy");
            this.evtSource.close();
        },
        computed: {
            codemirror() {
                console.log(this.$refs.mycm.codemirror)
                return this.$refs.mycm.codemirror
            }
        },
        mounted() {
            this.getList()
        },
        watch: {
            namespace: function () {
                this.getList()
            },
            list: function (newVal) {
                this.searched = newVal
            },
            active: function (newVal) {
                var me = this

                if (newVal == true) {
                    this.$nextTick(function () {
                        me.codemirror.refresh()

                        me.codemirror.setSize(900,500)
                        me.codemirror.setOption('lineWrapping',true)
                        me.codemirror.refresh()
                    })

                }
            }
        },

        methods: {
            onCmReady() {
                // 임시처리

            },
            onFocus() {
                var me = this
                me.codemirror.scrollTo(0,1000)
                me.codemirror.refresh()

                me.codemirror.scrollTo(0,0)
                me.codemirror.refresh()

            },
            onSelect(item) {
                this.selected = item
            },
            searchOnTable() {
                this.searched = searchByName(this.list, this.search)
            },
            startSSE: function () {
                var me = this;
                // var tmp = [];
                if (me.evtSource != null) {
                    me.evtSource.close()
                }
                if (me.namespace != null) {
                    console.log(me.namespace)

                    me.evtSource = new EventSource(`${API_HOST}/kubesse/?instanceType=` + me.types + '&namespace=' + me.namespace)
                } else if (me.namespace == null) {
                    console.log(me.namespace)

                    me.evtSource = new EventSource(`${API_HOST}/kubesse/?instanceType=` + me.types)
                }

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
                if (me.namespace == null) {
                    me.$http.get(`${API_HOST}/kube/v1/` + getURLType)
                        .then((result) => {
                            var tmpData = result.data
                            var resultMap = []
                            var usedUid = []
                            return new Promise((resolve, reject) => {
                                tmpData.forEach(function (sortingData) {
                                    if (!me.namespaceList.includes(sortingData.namespace)) {
                                        me.namespaceList.push(sortingData.namespace)
                                    }
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
                                // me.evtSource.close()
                                me.startSSE()
                                me.list = _.difference(resolveData, deleteItemList)

                                me.searched = me.list
                                me.$emit('update:namespaceList', me.namespaceList)

                            })
                        })
                } else if (me.namespace != null) {
                    me.$http.get(`${API_HOST}/kube/v1/` + getURLType + '/namespaces/' + me.namespace)
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

                                me.searched = me.list
                            })
                        })
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
                        me.$notify({
                            title: 'Success',
                            message: '삭제되었습니다. 적용 중 입니다.',
                            type: 'Success'
                        });
                        me.loading = true;
                    })
                })
            },

            handleEdit(item) {
                var me = this
                var getURLType;
                me.active = true
                var yamlData = item.sourceData
                me.selectedRow = item
                me.plainText = json2yaml.stringify(JSON.parse(yamlData)).replace(/- \n[ ]+/g, '- ').replace(/\\"/g, '\'')
            },
            postYAML() {
                var me = this
                var nameSpace = me.namespace;
                if (nameSpace == null) {
                    nameSpace = 'default'
                }

                // if (me.plainText == "") {
                //     this.$alert('입력값이 부족합니다.', '알림', {
                //         confirmButtonText: 'OK',
                //     })
                // }

                var me = this;

                var jsonYaml = yaml.load(me.plainText)

                console.log(jsonYaml)

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
                        me.active = false
                        me.showAddSnackbar = true;
                        me.status = ''

                    })
                } else if (me.status == 'edit') {
                    me.$http.put(`${API_HOST}/kube/v1/` + getURLType + '/namespaces/' + nameSpace + '/' + me.selectedRow.name, jsonYaml, {
                            headers: {
                                'Content-Type': 'application/json'
                            }
                        }
                    ).then(function () {
                        me.active = false
                        me.showEditnackbar = true;
                        me.status = ''
                    })
                }
            }
        },
    }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">
</style>
