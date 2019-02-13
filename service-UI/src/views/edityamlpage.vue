<template>
    <div class="edityaml">

        <el-row>
            <el-col :span="12">
                <codemirror
                        :options="{
                theme: 'darcula',
                mode: 'yaml',
                lineNumbers: true,
                lineWrapping: true,
                }"
                        :value="yamlText"
                        v-model="yamlText"
                        @focus="onYamlFocus()"
                >
                </codemirror>
            </el-col>

            <el-col :span="12">
                <div class="grid-content bg-purple-light"></div>
            </el-col>
        </el-row>

        <text-reader @load="plainText = $event"></text-reader>


        <ul>
            <li v-for="data in object_list">
                <el-input placeholder="Please input" v-model="data.val" @focus="onUiFocus()" >
                    <template slot="prepend">{{ data.key_name}}</template>
                </el-input>
            </li>
        </ul>

    </div>
</template>


<style>
    .CodeMirror-scroll {
        text-align: left;
    }

    .el-input__inner {
        width: 200px;
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


    import VueCodemirror from 'vue-codemirror'
    // import VueCodeMirror from 'vue-codemirror-lite'

    export default {

        name: 'editYAML',
        data() {
            return {
                plainText: "",
                yamlText: "",
                object_list:"",
                temp_object_list:"",
                json_data:"",
                regex:":",
                channel:'all'

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
        },
        methods: {
            onUiFocus(){
                var me = this;
                if (me.channel != 'ui')
                    me.channel='ui'
                //console.log("ui")
                // me.json_data = me.obj2json(me.object_list)
            },
            onYamlFocus(){
                var me = this
                if (me.channel != 'ya')
                    me.channel='ya'
                //console.log("ya")
                // me.json_data = yaml.load(me.yamlText)
            },
            newObjectData(key_name, val) {
                var me = this
                var a = key_name.split(me.regex)
                let data={
                    key_list : a,
                    key_name : key_name.replace(/:[0-9]+:/g, ':-:'),
                    val : val
                }
                return data
            },
            json2objs(obj, name) {
                var me = this;
                var type = typeof obj
                if (type != "object") {
                    me.object_list.push(me.newObjectData(name, obj) )
                } else if (typeof obj == "object") {
                    var key_vals = Object.keys(obj)
                    for (var i in key_vals) {
                        var name_val
                        if (name == '') {
                            name_val = key_vals[i] + me.regex
                        } else {
                            name_val = name + key_vals[i] + me.regex
                        }
                        me.json2objs(obj[key_vals[i]], name_val)
                    }
                } else
                    console.log("Unidentified obj: " + obj)
            },
            updateObjList(){
                var me = this
                var json = JSON.parse(JSON.stringify(me.json_data))
                me.object_list = []
                me.json2objs(json,'')
                //return me.object_list
            },
            modifyJSON(json_data, modify_data){
                var me = this
                var find = modify_data.key_list.splice(0,1)
                if (modify_data.key_list.length <= 0 ){
                    return modify_data.val
                } else {
                    json_data[ find ] = me.modifyJSON( json_data[ find ], modify_data)
                    return json_data
                }
            },
            obj2json(value){
                var me = this
                var json = JSON.parse(JSON.stringify(me.json_data))
                var obj_list = value
                for (var i in obj_list ){
                    json = me.modifyJSON(json,obj_list[i])
                }
                return json
            },
            updateYAML( ){
                var me = this
                var json = JSON.parse(JSON.stringify(me.json_data))
                console.log()
                me.yamlText = json2yaml.stringify(json)
            },
            compareObjectList(arr1,arr2){
                if (arr1.length == arr2.length){
                    for (var i in arr1){
                        if (arr1[i].key_name == arr2[i].key_name && arr1[i].val == arr2[i].val){
                        } else
                            return false
                    }
                    return true
                } else {
                    return false
                }
            }
        },
        watch: {
            plainText: function (newVal) {
                var me = this
                me.channel = 'all'
                me.json_data = yaml.load(newVal)
            },
            yamlText: function (newVal) {
                var me = this
                if (me.channel=='all' || me.channel == 'ya'){
                    var value = yaml.load(newVal)
                    if (typeof value == 'object'){
                        me.json_data = value
                    }
                }


            },
            object_list: {
                handler: function(newVal){
                    var me = this
                    if (me.channel=='all' || me.channel == 'ui') {
                        if (!me.compareObjectList(me.temp_object_list, newVal)) {
                            me.temp_object_list = newVal
                            me.json_data = me.obj2json(newVal)
                        }
                    }

                },
                deep: true
            },
            json_data: function (newVal) {
                var me = this
                me.updateObjList()
                me.updateYAML()
                if (me.channel =='all')
                    me.channel = 'none'
            }
        }
    }

</script>
