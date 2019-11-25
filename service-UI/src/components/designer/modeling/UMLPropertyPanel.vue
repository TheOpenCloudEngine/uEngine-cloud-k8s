<template>
    <v-layout wrap>
        <v-navigation-drawer v-model="value.drawer" absolute right width="390">
            <v-list class="pa-1">
                <v-list-item>
                    <v-list-item-avatar>
                        <img :src="img">
                    </v-list-item-avatar>

                    <v-list-item-content>
                        <v-list-item-title class="headline">{{ titleName }}</v-list-item-title>
                    </v-list-item-content>
                </v-list-item>
            </v-list>


            <v-list class="pt-0" dense v-if="value._type != 'org.uengine.model.Relation'">
                <v-divider></v-divider>
                <v-data-table
                        :headers="headers"
                        :items.sync="value.fieldDescriptors"
                        hide-default-header
                        hide-default-footer
                        class="elevation-1"
                >
                    <template v-slot:item.action="{ item }">
                        <v-icon
                                small
                                @click="UMLDeleteEntity(item)"
                        >
                            delete
                        </v-icon>
                    </template>
                </v-data-table>

                <v-row justify="center">
                    <v-flex xs4>
                        <v-select v-model="umlEntityType" :items=entityTypeList label="Type"
                                  :menu-props="{ top: true, offsetX: false }"
                                  style="margin-right: 15px;"></v-select>
                    </v-flex>
                    <v-flex xs6>
                        <v-text-field v-model="umlEntityName" label="Name"
                                      required></v-text-field>
                    </v-flex>
                </v-row>

                <v-layout justify-end wrap>
                    <v-btn rounded color="primary" @click="UMLEntityADD(umlEntityType,umlEntityName)" dark>ADD Attribute
                    </v-btn>
                </v-layout>
            </v-list>

            <v-list v-else>
                <v-radio-group v-model="value.relationValue">
                    <v-radio
                            v-for="n in relationList"
                            :key="n.name"
                            :label="n.shape"
                            :value="n.name"
                    ></v-radio>
                </v-radio-group>
            </v-list>
        </v-navigation-drawer>
    </v-layout>

</template>

<script>
    export default {
        name: 'uml-property-panel',
        props: {
            value: Object,
            titleName: String,
            img: String,
        },
        data: function () {
            return {
                entityTypeList: ['Int', 'String', 'Float', 'Double', 'Long'],
                umlEntityType: '',
                umlEntityName: '',
                relationList:[
                    {'name':'1', 'shape':'------------'},
                    {'name':'2', 'shape':'------------>'},
                    {'name':'3', 'shape':'------------0'},

                ],
                headers: [
                    {
                        text: 'type',
                        value: 'className',
                        align: 'center'
                    },
                    {
                        text: 'name',
                        value: 'name',
                        align: 'center'
                    },
                    {
                        text: 'Actions',
                        value: 'action',
                        sortable: false,
                        align: 'center'
                    },],
                translateText: '',
                usedTranslate: false,
                UMLInput: '',

            }
        },
        methods:{
            UMLDeleteEntity(val) {
                var me = this

                me.value.fieldDescriptors.forEach(function (element, idx) {
                    if (element.name == val.name && element.type == val.type) {

                        if (idx > -1)
                            me.value.fieldDescriptors.splice(idx, 1)
                    }
                })
            },
            UMLEntityADD: function (type, name) {
                var me = this
                if (type.length != 0 && name.length != 0) {

                    //"namePascalCase": name.charAt(0).toUpperCase() + name.slice(1)
                    let tmpObject = null

                    if (name == 'id') {
                        tmpObject = {
                            "_type": "org.uengine.uml.model.FieldDescriptor",
                            "name": name,
                            "className": type,
                            "isKey": true
                        }
                    }else{
                        tmpObject = {
                            "_type": "org.uengine.model.FieldDescriptor",
                            "name": name,
                            "className": type
                        }
                    }

                    me.value.fieldDescriptors.push(tmpObject);
                    this.umlEntityType = ""
                    this.umlEntityName = ""

                } else {
                    var designer = this.$parent.getComponent('modeling-designer')
                    designer.text = "TYPE & NAME INPUT REQUEST"
                    designer.snackbar = true
                }
            },

        },
        watch:{
            "value.drawer": function (val) {
                this.navigationDrawer = val;
            },
            navigationDrawer: {
                handler: function (val, oldval) {
                    var opengraph = this.$parent.getComponent('opengraph')

                    if (val == true) {
                        this._item = this.value;

                        if (this.value.elementView) {
                            this.x = this.value.elementView.x;
                            this.y = this.value.elementView.y;
                            this.width = this.value.elementView.width;
                            this.height = this.value.elementView.height;
                        }
                        // this.value.drawer = val
                        // this.$emit('update:drawer', val);

                    } else {
                        //프로퍼티 에디팅 해제.
                        // this.$emit('update:drawer', false);
                    }
                }
            },

        }

    }
</script>
