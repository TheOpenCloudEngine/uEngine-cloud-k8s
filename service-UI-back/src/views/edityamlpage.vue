<template>
    <div class="edityaml">
        <el-row :gutter="20">
            <el-col :span="12">
                <codemirror
                        :options="{
                theme: 'darcula',
                lineNumbers: true,
                lineWrapping: true,
                }"
                        :value="yamlText"
                        v-model="yamlText"
                        @focus="onYamlFocus()"
                >
                </codemirror>
                <text-reader :fileName.sync="fileName" :plainText.sync="plainText" @load="plainText = $event"></text-reader>
                <el-button @click="download">Download</el-button>
            </el-col>
            <el-col :span="10">
                <ul>
                    <li v-for="data in object_list">
                        <el-input placeholder="Please input" v-model="data.val" @focus="onUiFocus()">
                            <template slot="prepend">{{ key_name(data.key_list) }}</template>
                        </el-input>
                    </li>
                </ul>
            </el-col>
        </el-row>


    </div>
</template>


<style>

    .CodeMirror-scroll {
        text-align: left;
    }


</style>

<script>
    // @ is an alias to /src
    import TextReader from "@/components/yaml.vue";
    import yaml from 'js-yaml'
    import json2yaml from 'json2yaml'
    import 'vue-codemirror'
    import 'codemirror/mode/yaml/yaml.js'
    import 'codemirror/theme/darcula.css'
    import { saveAs } from 'file-saver';
    var FileSaver = require('file-saver');

    import VueCodemirror from 'vue-codemirror'
    // import VueCodeMirror from 'vue-codemirror-lite'

    export default {

        name: 'editYAML',
        props: {
          fileName: String
        },
        data() {
            return {
                plainText: "",
                yamlText: "",
                object_list: "",
                temp_object_list: "",
                json_data: "",
                regex: ":",
                channel: 'all',
                table: "",
            }
        },
        mounted() {
            var me = this;
            // $(this.$el).find('.CodeMirror').height(600);
        },
        computed: {},
        components: {
            TextReader,
            yaml,
            json2yaml,
            saveAs
        },
        methods: {
            download () {
                var me = this;

                var text = me.yamlText;
                var filename = me.fileName;

                var file = new File([text], filename, {type: "text/yaml;charset=utf-8"});
                FileSaver.saveAs(file);

            },
            onYamlFocus() {
                var me = this
                if (me.channel != 'ya')
                    me.channel = 'ya'
            },
            onUiFocus() {
                var me = this;
                if (me.channel != 'ui')
                    me.channel = 'ui'
                //console.log("ui")
                // me.json_data = me.obj2json(me.object_list)
            },
            newObjectData(key_list, val) {
                let data = {
                    key_list: key_list,
                    val: val
                }
                return data
            },
            key_name(key_list) {
                var str = key_list.toString()
                str = str.replace(/[0-9]+,/g, '- ')
                str = str.replace(/,/g, ' : ')
                return str

            },
            json2objs(obj, key_list) {
                var me = this;
                var type = typeof obj
                if (type != "object") {
                    me.object_list.push(me.newObjectData(key_list, obj))
                } else if (typeof obj == "object") {
                    try {
                        var key_vals
                        key_vals = Object.keys(obj)
                        for (var i in key_vals) {
                            var parent_list = key_list.concat(key_vals[i])
                            //console.log("son to objs")
                            //console.log(parent_list)
                            me.json2objs(obj[key_vals[i]], parent_list)
                        }
                    } catch (e) {
                    }
                } else
                    console.log("Unidentified obj: " + obj)
            },

            modifyJSON(json_data, modify_data) {
                var me = this
                if (modify_data.key_list.length <= 0) {
                    json_data = modify_data.val
                } else {
                    var find = modify_data.key_list.splice(0, 1)
                    json_data[find] = me.modifyJSON(json_data[find], modify_data)
                }
                return json_data
            },
            obj2json(value) {
                var me = this
                var json = JSON.parse(JSON.stringify(me.json_data))
                var obj_list = me.object_list
                for (var i in obj_list) {
                    json = me.modifyJSON(json, obj_list[i])
                }
                return json//JSON.parse(JSON.stringify(json))//json
            },
            updateObjList() {
                var me = this
                var json = JSON.parse(JSON.stringify(me.json_data))
                me.object_list = []
                me.json2objs(json, [])
            },
            updateYAML() {
                var me = this
                var yaml_text = json2yaml.stringify(JSON.parse(JSON.stringify(me.json_data)))
                var numbers = yaml_text.match(/"[0-9]+"/gi)
                for (var i in numbers) {
                    yaml_text = yaml_text.replace(numbers[i], parseInt(numbers[i].replace(/"/g, "")))
                }
                yaml_text = yaml_text.replace(/- \n[ ]+/g, '- ')

                var lines = yaml_text.split('\n')
                lines.splice(0, 1)
                for (var i in lines) {
                    lines[i] = lines[i].substring(2, lines[i].length)
                }
                me.yamlText = lines.join('\n')
            },

            compareObjectList(arr1, arr2) {
                if (arr1.length == arr2.length) {
                    for (var i in arr1) {
                        if (!(arr1[i].key_name == arr2[i].key_name && arr1[i].val == arr2[i].val)) {
                            return false
                        }
                    }
                    return true
                } else {
                    return false
                }
            },
        },
        watch: {
            plainText: function (newVal) {
                var me = this
                me.channel = 'all'
                me.json_data = yaml.load(newVal)
            },
            yamlText: function (newVal) {
                    var me = this
                    if (me.channel == 'all' || me.channel == 'ya') {
                        try {
                            var value = yaml.load(newVal)
                            if (typeof value == 'object') {
                                me.json_data = value
                            }
                        } catch (e) {

                        }
                    }
                },
            object_list: {
                handler: function (newVal) {
                    var me = this

                    if (me.channel == 'all' || me.channel == 'ui') {
                        if (!me.compareObjectList(me.temp_object_list, me.object_list)) {
                            me.temp_object_list = me.object_list

                            me.json_data = me.obj2json(me.object_list)

                        }

                    }
                },
                deep: true
            },
            json_data: function (newVal) {
                var me = this
                if (me.channel == 'all' || me.channel == 'ui')
                    me.updateYAML()

                me.updateObjList()

                if (me.channel == 'all')
                    me.channel = 'none'
            }
        }
    }

</script>
