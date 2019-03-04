<template>
    <v-container grid-list-md text-xs-center>
        <v-layout row wrap>
            <v-flex xs6>
                <v-card>
                    <v-card-text>
                        <v-text-field
                                v-model="fileName"
                                label="File Name"
                        ></v-text-field>
                        <codemirror
                                ref="myCm"
                                :options="{
                                    theme: 'darcula',
                                    lineNumbers: true,
                                    lineWrapping: true,
                                    }"
                                :value="yaml_text"
                                v-model="yaml_text"
                                @focus="onYamlFocus()"
                        >
                        </codemirror>
                        <text-reader :fileName.sync="fileName"
                                     :plainText.sync="yaml_text"
                                     @load="yaml_text = $event">
                            Load
                        </text-reader>
                        <v-btn color="info" @click="createPod()">Deploy Sample</v-btn>
                        <v-btn color="info" @click="createDeployment()">Deploy Sample</v-btn>
                        <v-btn color="info" @click="createService()">Service Sample</v-btn>
                    </v-card-text>
                    <v-btn color="success" block @click="download">Download</v-btn>
                </v-card>
            </v-flex>
            <v-flex xs6>
                <v-card>
                    <v-card-text>
                        <template v-for="item in ui_list">
                            <v-text-field v-if="item.ui_type=='string'"
                                          :label="item.ui_name"
                                          v-model="item.val"
                                          @focus="onUiFocus()"
                            ></v-text-field>

                            <v-text-field v-else-if="item.ui_type=='number'"
                                          v-model="item.val" @focus="onUiFocus()"
                                          type="number"
                                          :label='item.ui_name'
                            ></v-text-field>
                        </template>
                    </v-card-text>
                </v-card>
            </v-flex>
        </v-layout>
    </v-container>
</template>


<style>
    .CodeMirror-scroll {
        text-align: left;
    }
</style>

<script>
    var FileSaver = require('file-saver');

    // @ is an alias to /src
    import TextReader from "@/components/yaml.vue";
    import yaml from 'js-yaml'
    import json2yaml from 'json2yaml'
    import 'vue-codemirror'
    import 'codemirror/mode/yaml/yaml.js'
    import 'codemirror/theme/darcula.css'
    import {codemirror} from 'vue-codemirror'
    import {saveAs} from 'file-saver';

    export default {
        name: 'EditYaml',
        props:
            ['yaml_text_tmp_local'],
        data() {
            return {
                fileName: '',
                temp_text: "",
                ui_list: [],
                yaml_text: "",
                json_data: "",
                cursor_pos: "",
                channel: "yaml",
                auto_edit: true,
            }
        },
        created: function () {

        },

        computed: {
            codemirror: function () {
                return this.$refs.myCm.codemirror;
            },
        },
        components: {
            TextReader,
            yaml,
            json2yaml,
            codemirror,
            saveAs
        },


        methods: {
            createPod() {
                let me = this
                me.ui_list = [
                    {
                        key_lists: [
                            "metadata,name",
                            "metadata,labels,name",
                            "spec,containers,0,name"
                        ],
                        ui_name: "pod name",
                        ui_type: "string",
                        val: "name",
                    },
                    {
                        key_lists: [
                            "spec,containers,0,ports,0,containerPort"
                        ],
                        ui_name: "pod port",
                        ui_type: "number",
                        val: "0",
                    },
                    {
                        key_lists: [
                            "spec,containers,0,image"
                        ],
                        ui_name: "pod image",
                        ui_type: "string",
                        val: "image",
                    },

                ]

                me.yaml_text =
                    "apiVersion: v1\n" +
                    "kind: Pod\n" +
                    "metadata:\n" +
                    "  name: \n" +
                    "  labels:\n" +
                    "    name: \n" +
                    "spec:\n" +
                    "  containers:\n" +
                    "  - name: \n" +
                    "    image: \n" +
                    "    ports:\n" +
                    "    - containerPort: 0"

            },
            download() {
                var me = this;

                var text = me.yaml_text;

                var filename = me.fileName;

                if (filename == undefined) {
                    filename = 'nonamefile.yaml'
                } else if (!filename.includes('.yaml')) {
                    filename += '.yaml'
                }

                var file = new File([text], filename, {type: "text/yaml;charset=utf-8"});
                FileSaver.saveAs(file);
            },
            onYamlFocus() {
                let me = this
                me.channel = 'yaml'
            },
            onUiFocus() {
                let me = this;
                me.channel = 'ui'
            },
            modifyJson(json_data, key_list, modify_data) {
                let me = this
                if (key_list.length <= 0) {
                    json_data = modify_data
                } else {
                    let find = key_list.splice(0, 1)
                    if (json_data[find] == null)
                        json_data[find] = {}
                    json_data[find] = me.modifyJson(json_data[find], key_list, modify_data)
                }
                return json_data
            },
            findJson(json_data, key_list) {
                let me = this
                let find = key_list.splice(0, 1)
                if (key_list.length <= 0) {
                    return json_data[find]
                } else {
                    if (json_data[find] == null)
                        json_data[find] = {}
                    return me.findJson(json_data[find], key_list)
                }
            },
            uiToJson() {
                let me = this
                let json = JSON.parse(JSON.stringify(me.json_data))
                let ui_list = me.ui_list
                for (let idx in ui_list) {
                    let item = ui_list[idx]
                    let val = item.val
                    let key_lists = item.key_lists
                    for (let i in key_lists) {
                        let key_list = key_lists[i].split(',')
                        json = me.modifyJson(json, key_list, val)
                    }
                }
                me.json_data = json
            },
            getArrVal(arr, val) {
                for (let i in arr) {
                    if (arr[i] != val) {
                        return arr[i]
                    }
                }
                return val
            },
            compareArrVals(arr) {
                let start = arr.splice(0, 1)
                for (let i in arr) {
                    if (arr[i] != start) {
                        return false
                    }
                }
                return true
            },
            jsonToUi() {
                let me = this
                let json = JSON.parse(JSON.stringify(me.json_data))
                let ui_list = me.ui_list
                for (let idx in ui_list) {
                    let item = ui_list[idx]
                    let val = item.val
                    let key_lists = item.key_lists
                    let val_list = []
                    for (let i in key_lists) {
                        let key_list = key_lists[i].split(',')
                        val_list.push(me.findJson(json, key_list))
                    }
                    let newVal = me.getArrVal(val_list, val)
                    me.ui_list[idx].val = newVal
                    if (!me.compareArrVals(val_list)) {
                        for (let i in key_lists) {
                            let key_list = key_lists[i].split(',')
                            json = me.modifyJson(json, key_list, newVal)
                        }
                        me.json_data = json
                    }
                }
            },
            yamlFilter(yaml_text) {
                yaml_text = yaml_text.replace(/"/g, '')
                yaml_text = yaml_text.replace(/- \n[ ]+/g, '- ')
                let lines = yaml_text.split('\n')
                lines.splice(0, 1)
                for (let i in lines) {
                    lines[i] = lines[i].substring(2, lines[i].length)
                }
                yaml_text = lines.join('\n')
                yaml_text = yaml_text.replace(/ null/g, ' ')
                return yaml_text
            },
            createDeployment() {
                let me = this
                me.ui_list = [
                    {
                        key_lists: [
                            'metadata,name',
                            "metadata,labels,app",
                            "spec,selector,matchLabels,app",
                            "spec,template,metadata,labels,app",
                            "spec,template,spec,containers,0,name"
                        ],
                        ui_name: "deployment name",
                        ui_type: "string",
                        val: "name",
                    },
                    {
                        key_lists: [
                            "spec,template,spec,containers,0,image"
                        ],
                        ui_name: "image",
                        ui_type: "string",
                        val: "image_name",
                    },
                    {
                        key_lists: [
                            "spec,replicas"
                        ],
                        ui_name: "replicas",
                        ui_type: "number",
                        val: "0",
                    }
                    ,
                    {
                        key_lists: [
                            "spec,template,spec,containers,0,port"
                        ],
                        ui_name: "port",
                        ui_type: "number",
                        val: "0",
                    }

                ];
                me.yaml_text =
                    "apiVersion: apps/v1\n" +
                    "kind: Deployment\n" +
                    "metadata:\n" +
                    "  name: \n" +
                    "  labels:\n" +
                    "    app: \n" +
                    "spec:\n" +
                    "  selector:\n" +
                    "    matchLabels:\n" +
                    "      app:  \n" +
                    "  replicas: 0\n" +
                    "  template:\n" +
                    "    metadata:\n" +
                    "      labels: \n" +
                    "        app: \n" +
                    "    spec:\n" +
                    "      containers:\n" +
                    "        - name: \n" +
                    "          image: \n" +
                    "          port: 0"
                ;
            },
            createService() {
                let me = this
                me.ui_list = [
                    {
                        key_lists: [
                            "metadata,name",
                            "metadata,labels,app"
                        ],
                        ui_name: "service name",
                        ui_type: "string",
                        val: "name",
                    },
                    {
                        key_lists: [
                            "spec,selector,app"
                        ],
                        ui_name: "deployment name",
                        ui_type: "string",
                        val: "dep",
                    },
                    {
                        key_lists: [
                            "spec,ports,0,port"
                        ],
                        ui_name: "port",
                        ui_type: "number",
                        val: "0",
                    },
                    {
                        key_lists: [
                            "spec,ports,0,targetPort"
                        ],
                        ui_name: "target port",
                        ui_type: "number",
                        val: "0",
                    }
                ];
                me.yaml_text =
                    "apiVersion: v1\n" +
                    "kind: Service\n" +
                    "metadata:\n" +
                    "  name: \n" +
                    "  labels:\n" +
                    "    app: \n" +
                    "spec:\n" +
                    "  ports:\n" +
                    "    - port: 0\n" +
                    "      targetPort: 0\n" +
                    "  selector:\n" +
                    "    app: "
            },
            newLoad(data) {
                let me = this
                let json
                if (data == 'object') {
                    json = data
                } else {
                    try {
                        json = yaml.load(data)
                    } catch (e) {
                        return
                    }
                }

                let type = json["kind"]
                let ui = [
                    {
                        key_lists: [
                            "metadata,name",
                        ],
                        ui_name: type + " main name",
                        ui_type: "string",
                        val: me.findJson(json, "metadata,name".split(','))
                    }
                ];

                if (type.toLowerCase() == 'service') {
                    ui.push({
                        key_lists: [
                            "spec,selector,app"
                        ],
                        ui_name: "deployment name",
                        ui_type: "string",
                        val: me.findJson(json, "spec,selector,app".split(','))
                    })
                    ui.push(
                        {
                            key_lists: [
                                "spec,ports,0,port"
                            ],
                            ui_name: "port",
                            ui_type: "number",
                            val: me.findJson(json, "spec,ports,0,port".split(',')),
                        })
                    ui.push(
                        {
                            key_lists: [
                                "spec,ports,0,targetPort"
                            ],
                            ui_name: "target port",
                            ui_type: "string",
                            val: me.findJson(json, "spec,ports,0,targetPort".split(',')),
                        })
                    ui.push(
                        {
                            key_lists: [
                                "spec,type"
                            ],
                            ui_name: "type",
                            ui_type: "string",
                            val: me.findJson(json, "spec,type".split(',')),
                        })
                } else if (type.toLowerCase() == 'deployment') {
                    ui.push({
                        key_lists: [
                            "spec,template,spec,containers,0,image"
                        ],
                        ui_name: "image",
                        ui_type: "string",
                        val: me.findJson(json, "spec,template,spec,containers,0,image".split(','))
                    })
                    ui.push({
                        key_lists: [
                            "spec,replicas"
                        ],
                        ui_name: "replicas",
                        ui_type: "number",
                        val: me.findJson(json, "spec,replicas".split(','))
                    })
                    ui.push({
                        key_lists: [
                            "spec,template,spec,containers,0,port"
                        ],
                        ui_name: "port",
                        ui_type: "number",
                        val: me.findJson(json, "spec,template,spec,containers,0,port".split(','))
                    })
                } else if (type.toLowerCase() == 'pod') {
                    ui.push(
                        {
                            key_lists: [
                                "spec,containers,0,ports,0,containerPort"
                            ],
                            ui_name: "pod port",
                            ui_type: "number",
                            val: me.findJson(json, "spec,containers,0,ports,0,containerPort".split(',')),
                        })
                    ui.push({
                        key_lists: [
                            "spec,containers,0,image"
                        ],
                        ui_name: "pod image",
                        ui_type: "string",
                        val: me.findJson(json, "spec,containers,0,image".split(',')),
                    })
                }
                me.ui_list = ui;
            },
        },
        watch: {
            yaml_text_tmp_local: {
                immediate: true,
                handler(newVal, oldVal) {
                    var me = this
                    if (newVal != '') {
                        me.newLoad(newVal)
                        me.yaml_text = me.yamlFilter(newVal)
                        me.json_data = yaml.load(newVal)
                    }
                }
            },
            yaml_text: function () {
                var me = this
                if (me.auto_edit) {
                    try {
                        if (!(me.yaml_text == me.temp_text)) {
                            me.temp_text = me.yaml_text
                            me.cursor_pos = me.codemirror.getCursor("start")
                            me.json_data = yaml.load(me.yaml_text)
                        }
                        me.jsonToUi()
                        this.$nextTick(function () {
                            me.codemirror.setCursor(me.cursor_pos)
                        });
                    } catch (e) {
                    }
                }
            },
            ui_list: {
                handler: function () {
                    let me = this
                    if (me.channel == 'ui') {
                        me.uiToJson()
                    }
                    for (let idx in me.ui_list) {
                        let znum = (me.ui_list[idx].val)//.toString().match(/0[0-9]+/)
                        if (typeof  znum == 'string') {
                            let check_str = znum.match(/^0[0-9]+/)
                            if (check_str)
                                me.ui_list[idx].val = parseInt(check_str)
                        }
                    }
                    let yaml_text = json2yaml.stringify(JSON.parse(JSON.stringify(me.json_data)))
                    me.yaml_text = me.yamlFilter(yaml_text)
                }, deep: true
            },
        }
    }

</script>
