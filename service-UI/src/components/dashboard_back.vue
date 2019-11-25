<template>
    <div class="DashBoard" style="text-align:center; margin: 10px">
        <!--<h1> List </h1>-->
        <div>
            <!--<dropdown :options="nameSpaceList"-->
            <!--:selected="selectedNamespace"-->
            <!--v-on:updateOption="methodToRunOnSelect"-->
            <!--:placeholder="'Select an Namespace'"-->
            <!--&gt;-->
            <!--</dropdown>-->
        </div>
        <div class="table">
            <h2>Response</h2>

            <el-table
                    ref="filterTable"
                    :data="list"
                    style="width: 100%">
                <el-table-column
                        fixed
                        sortable
                        prop="provider"
                        label="Provider"
                        width="120"
                        :filters="[{text: 'EC2', value: 'EC2'}, {text: 'K8S', value: 'K8S'}]"
                        :filter-method="filterHandler"
                >
                    <template slot-scope="scope">
                        <el-tag
                                :type="scope.row.provider === 'K8S' ? 'primary' : 'warning'"
                                disable-transitions>{{scope.row.provider}}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column
                        fixed
                        sortable
                        prop="name"
                        label="Name"
                >
                </el-table-column>
                <el-table-column
                        fixed
                        prop="id"
                        label="ID"
                >
                </el-table-column>
                <el-table-column
                        prop="type"
                        label="Type"
                        width="100">
                </el-table-column>
                <el-table-column
                        prop="status"
                        label="Status"
                        width="100"
                >
                </el-table-column>
                <el-table-column
                        prop="instanceState"
                        label="Instance State"
                        width="120"
                >
                </el-table-column>
                <el-table-column
                        prop="createDate"
                        label="Create Date"
                        column-key="date"
                        width="100"
                />
                <el-table-column
                        prop="regDate"
                        label="Reg Date"
                        column-key="date"
                        width="100"
                />
                <el-table-column
                        prop="properties"
                        label="Properties"
                >
                </el-table-column>

            </el-table>

        </div>
    </div>
</template>

<script>
    import VueTable from 'vuetable-2'
    import dropdown from 'vue-dropdowns';

    export default {

        name: 'DashBoard',
        props: {
            user: Array,
        },
        components: {
            'vuetable': VueTable,
            'dropdown': dropdown,
        },
        data() {
            return {
                evtSource: null,
                selectedNamespace: {},
                list: [],
                providerFilters: []
            }
        },

        beforeDestroy: function () {
            var me = this
            console.log("closing evtSource beforeDestroy");
            me.evtSource.close();
        },
        mounted() {
            var me = this;
            // console.log(me.user)
            me.startSSE(me.user[0]);
            this.getNameSpace().then(function (list) {
                /* Provider별 정렬 */
                list.sort(function (a, b) {
                    return a.provider < b.provider ? -1 : a.provider > b.provider ? 1 : 0;
                });
                me.list = list
            });
        },

        watch: {
            user: function (newVal) {
                var me = this
                if (me.evtSource != null) {
                    me.evtSource.close();
                }
                me.list = [];
                var name;
                var provider;
                var resultArray = [];

                newVal.forEach(function (tmp, index) {
                    name = tmp.name;
                    provider = tmp.provider;
                    if (provider == 'All') {
                        me.$http.get(`${API_HOST}/kube/instance/`)
                            .then((result) => {
                                me.list = result.data;
                                me.list.sort(function (a, b) {
                                    return a.name < b.name ? -1 : a.name > b.name ? 1 : 0;
                                });
                            });
                        if (me.evtSource != null) {
                            me.evtSource.close();
                            // console.log('All ' + tmp)
                            console.log(tmp)
                            me.startSSE(tmp);
                        }
                    } else {
                        return new Promise(function (resolve, reject) {
                            var responseList = [];
                            me.$http.get(`${API_HOST}/kube/instance/` + provider + '/' + name)
                                .then((result) => {
                                    me.list = [];
                                    if (me.list.length == 0) {
                                        result.data.forEach(function (data) {
                                            if (!(data.instanceState == 'DELETE')) {
                                                resultArray.push(data)
                                            }
                                        })
                                    }
                                    resolve(resultArray)
                                });
                        }).then(
                            function (list) {
                                list.sort(function (a, b) {
                                    return a.name < b.name ? -1 : a.name > b.name ? 1 : 0;
                                })
                                me.list = list
                                if (me.evtSource != null && newVal[index].provider =='K8S') {
                                    me.evtSource.close();
                                    console.log(newVal[index].provider)
                                    me.startSSE(newVal[index]);
                                }
                            }
                        );
                    }

                })
            },
        },
        methods: {
            filterHandler(value, row, column) {
                const property = column['property'];
                return row[property] === value;
            },
            getNameSpace: function (callback) {
                var me = this;
                return new Promise(function (resolve, reject) {
                    var responseList = [];
                    me.$http.get(`${API_HOST}/kube/instance/`)
                        .then((result) => {
                            console.log(result);
                            if (me.list.length == 0) {
                                result.data.forEach(function (data) {
                                    if (!(data.instanceState == 'DELETE')) {
                                        responseList.push(data)
                                    }
                                })
                            }
                            resolve(responseList);
                        })
                });
            },
            startSSE: function (user) {
                var me = this;
                var tmp = [];

                if (!(user.provider == 'All')) {
                    me.evtSource = new EventSource(`${API_HOST}/kubesse/?provider=` + user.provider + '&name=' + user.name)
                } else {
                    me.evtSource = new EventSource(`${API_HOST}/kubesse/`)
                }
                me.evtSource.onmessage = function (e) {
                    var parse = JSON.parse(e.data);
                    var parseMessage = JSON.parse(parse.message);
                    var listNameListTmp = [];
                    me.list.forEach(function (name) {
                        listNameListTmp.push(name.id)
                    });

                    me.list.some(function (listTmp, index) {
                        if (listTmp.id == parseMessage.id) {
                            // console.log(me.list[index] + ':' + parseMessage);
                            if (parseMessage.instanceState == 'DELETE') {
                                me.list = [
                                    ...me.list.slice(0, index),
                                    ...me.list.slice(index + 1)
                                ]
                            } else {
                                me.list = [
                                    ...me.list.slice(0, index),
                                    parseMessage,
                                    ...me.list.slice(index + 1)
                                ]
                            }
                            me.list.sort(function (a, b) {
                                return a.name < b.name ? -1 : a.name > b.name ? 1 : 0;
                            });
                        } else if (!listNameListTmp.includes(parseMessage.id)) {
                            if (!(parseMessage.instanceState == 'DELETE')) {
                                me.list.push(parseMessage)
                                listNameListTmp.push(parseMessage.id)

                                me.list.sort(function (a, b) {
                                    return a.id < b.id ? -1 : a.id > b.id ? 1 : 0;
                                });

                                return;
                            }
                        }
                    })

                }
                me.evtSource.onerror = function (e) {
                    if (me.evtSource) {
                        console.log("closing evtSource and reconnect");
                        setTimeout(function () {
                        me.user.forEach(function (usr) {
                            if (usr.provider == 'K8S') {
                                me.evtSource.close();
                                console.log(usr.provider)
                                me.startSSE(usr);
                            }
                        })
                        }, 3000)
                    }
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
