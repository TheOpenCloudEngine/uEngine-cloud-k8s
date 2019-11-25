<!--
       * Todo: Property 작성 하는 부분 *
-->
<template>
<<<<<<< HEAD

    <v-layout wrap>
        <v-navigation-drawer v-model="value.drawer" absolute right width="390">
=======
    <v-layout wrap v-if="value.name=='Class'">
        <v-navigation-drawer v-model="navigationDrawer" absolute right width="390">
            <v-list class="pt-0" dense>
                <v-divider></v-divider>
                <v-data-table
                        :headers="headers"
                        :items="fieldDescriptors"
                        hide-default-header
                        hide-default-footer
                        class="elevation-1"
                >
                    <template v-slot:item.action="{ item }">
                        <v-icon
                                small
                                @click=""
                        >
                            delete
                        </v-icon>
                    </template>
                </v-data-table>

                <v-layout justify-center row style="align: center;">
                    <v-flex xs4>
                        <v-select v-model="entityType" :items="entityTypeList" label="Type"
                                  style="margin-left: 10px; margin-right: 15px;"></v-select>
                    </v-flex>
                    <v-flex xs6>
                        <v-text-field v-model="entityName" label="Value" required></v-text-field>
                    </v-flex>
                </v-layout>

                <v-layout justify-end wrap>
                    <v-btn rounded color="primary" @click="UMLEntityADD(entityType,entityName)" dark>ADD Attribute
                    </v-btn>
                </v-layout>
            </v-list>
        </v-navigation-drawer>
    </v-layout>


    <v-layout v-else wrap>
        <v-navigation-drawer v-model="navigationDrawer" absolute right width="390">
>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8
            <!--  상단 이미지 및 선택 타이틀 이름-->
            <v-list class="pa-1">
                <v-list-item>
                    <v-list-item-avatar>
                        <img :src="img">
                    </v-list-item-avatar>
<<<<<<< HEAD
                    <v-list-item-title class="headline">{{ titleName }}
                    </v-list-item-title>
                    <v-tooltip v-model="show" top>
                        <template v-slot:activator="{ on }">
                            <v-btn icon v-on="on">
                                <v-icon color="grey lighten-1">mdi-information</v-icon>
                            </v-btn>
                        </template>
                        <span>{{value.description_text}}</span>
                    </v-tooltip>
=======

                    <v-list-item-content>
                        <v-list-item-title class="headline">{{ titleName }}</v-list-item-title>
                    </v-list-item-content>
>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8
                </v-list-item>
            </v-list>

            <!--  해당 판넬의 내용  -->
            <v-list class="pt-0" dense flat>
                <v-divider></v-divider>
<<<<<<< HEAD
                <v-card v-if="value._type == 'org.uengine.model.Relation' && value.sourceElement._type == 'org.uengine.model.Event'">
=======
                <v-card v-if="value.name == 'Relation' && value.sourceElement._type == 'org.uengine.uml.model.Domain'">
>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8
                    <v-card-text>
                        <span class="headline">Type</span>
                        <v-layout>
                            <v-col>
<<<<<<< HEAD
                                <v-radio-group v-model="value.relationKey" style="padding-left: 10px;">
                                    <v-radio label="Event-Driven(Pub/Sub)" value="RestAPI"></v-radio>
                                    <v-radio label="Request & Response(Restful)" value="Restful"></v-radio>
                                </v-radio-group>
                                <v-select :disabled="value.relationKey == 'RestAPI'" v-model="value.relationValue"
                                          :items="restfulList"
=======
                                <v-radio-group v-model="radioCheck" style="padding-left: 10px;">
                                    <v-radio label="Event-Driven(Pub/Sub)" value="RestAPI"></v-radio>
                                    <v-radio label="Request & Response(Restful)" value="Restful"></v-radio>
                                </v-radio-group>
                                <v-select :disabled="radioCheck != 'Restful'" v-model="restApiType" :items="restApiList"
>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8
                                          label="Request & Response(Restful)"></v-select>
                            </v-col>
                        </v-layout>
                    </v-card-text>
                </v-card>
                <v-card outlined v-else>
                    <v-card-text>
                        <span class="headline" v-if="titleName">{{titleName}} Name</span>
                        <v-textarea name="input-7-1" outline :label="'Input Text'" auto-grow
<<<<<<< HEAD
                                    v-model="value.name"></v-textarea>
=======
                                    v-model="input"></v-textarea>
>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8

                        <v-card outlined v-if="usedTranslate">
                            <v-card-text @click="changeTranslate()">
                                추천 단어 : {{ translateText }}
                            </v-card-text>
                            <v-card-text>
                                선택시 변경 됩니다.
                            </v-card-text>
                        </v-card>


<<<<<<< HEAD
                        <span class="headline" v-if="value._type == 'org.uengine.model.Event'">Trigger</span>
                        <v-layout v-if="value._type == 'org.uengine.model.Event'" flat>
                            <v-radio-group v-model="value.trigger" style="font-size: small">
                                <v-row>
                                    <v-col>
                                        <v-radio label="Pre Persist" value="@PrePersist"></v-radio>
                                        <v-radio label="Pre Update" value="@PreUpdate"></v-radio>
                                        <v-radio label="Pre Delete" value="@PreDelete"></v-radio>
                                    </v-col>
                                    <v-col>
                                        <v-radio label="Post Persist" value="@PostPersist"></v-radio>
                                        <v-radio label="Post Update" value="@PostUpdate"></v-radio>
                                        <v-radio label="Post Delete" value="@PostDelete"></v-radio>
                                    </v-col>
                                </v-row>
                            </v-radio-group>
                        </v-layout>


                        <span class="headline"
                              v-if="value._type == 'org.uengine.model.Event' ||
                              value._type == 'org.uengine.model.Aggregate'">Attributes</span>
                        <v-layout
                                v-if="value._type == 'org.uengine.model.Event'"
                                flat>
                            <v-col>
                                <v-data-table
                                        :headers="headers"
                                        :items="value.fieldDescriptors"
                                        hide-default-header
                                        hide-default-footer
                                        class="elevation-1"
                                >

                                    <template v-slot:item.action="{ item }">
                                        <v-icon
                                                small
                                                @click="deleteEntity(item)"
                                        >
                                            delete
                                        </v-icon>
                                    </template>
                                </v-data-table>


                                <v-row justify="center">
                                    <v-flex xs4>
                                        <v-select v-model="entityType" :items="entityTypeList" label="Type"
                                                  style="margin-right: 15px;"></v-select>
                                    </v-flex>
                                    <v-flex xs6>
                                        <v-text-field v-model="entityName" label="Name"
                                                      v-on:keyup.enter="attributeADD(entityType,entityName)"
                                                      required></v-text-field>
                                    </v-flex>
                                </v-row>
                                <v-row justify="end">
                                    <v-btn rounded color="primary" @click="attributeADD(entityType,entityName);" dark>
                                        ADD ATTRIBUTE
                                    </v-btn>
                                </v-row>
                            </v-col>
                        </v-layout>

                        <v-layout
                                v-else-if="value._type == 'org.uengine.model.Aggregate'"
                                flat>
                            <v-col>
                                <v-data-table
                                        :headers="headers"
                                        :items="value.aggregateRoot.fieldDescriptors"
=======
                        <span class="headline" v-if="value.name == 'Event' || value.name == 'Aggregate'">Attributes</span>
                        <v-layout v-if="value.name == 'Event' || value.name == 'Aggregate'" flat>
                            <v-col>
                                <v-data-table
                                        :headers="headers"
                                        :items="entity"
>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8
                                        hide-default-header
                                        hide-default-footer
                                        class="elevation-1"
                                >

                                    <template v-slot:item.action="{ item }">
                                        <v-icon
                                                v-if="item.name != 'id'"
                                                small
<<<<<<< HEAD
                                                @click="deleteEntity(item)"
=======
                                                @click="deleteEntity(entity,item)"
>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8
                                        >
                                            delete
                                        </v-icon>
                                    </template>
                                </v-data-table>


                                <v-row justify="center">
                                    <v-flex xs4>
                                        <v-select v-model="entityType" :items="entityTypeList" label="Type"
                                                  style="margin-right: 15px;"></v-select>
                                    </v-flex>
                                    <v-flex xs6>
                                        <v-text-field v-model="entityName" label="Name"
<<<<<<< HEAD
                                                      v-on:keyup.enter="attributeADD(entityType,entityName)"
=======
>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8
                                                      required></v-text-field>
                                    </v-flex>
                                </v-row>
                                <v-row justify="end">
<<<<<<< HEAD
                                    <v-btn rounded color="primary" @click="attributeADD(entityType,entityName);" dark>
=======
                                    <v-btn rounded color="primary" @click="entityADD(entityType,entityName);" dark>
>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8
                                        ADD ATTRIBUTE
                                    </v-btn>
                                </v-row>
                            </v-col>
                        </v-layout>

<<<<<<< HEAD
                        <v-layout v-if="value._type == 'org.uengine.model.Command'">
                            <v-autocomplete v-model="value.restfulType" :items="restfulList"
                                            label="Restful TYPE" persistent-hint>
=======
                        <v-layout v-if="value.name == 'Command'">
                            <v-autocomplete v-model="restApiType" :items="restApiList"
                                            label="REST API TYPE" persistent-hint>
>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8
                            </v-autocomplete>
                        </v-layout>


                        <span class="headline"
<<<<<<< HEAD
                              v-if="value._type == 'org.uengine.model.Event' || value._type == 'org.uengine.model.Policy' || value._type == 'org.uengine.model.Command' || value._type == 'org.uengine.model.External'">Associated Aggregate</span>
                        <v-layout
                                v-if="value._type == 'org.uengine.model.Event' || value._type == 'org.uengine.model.Policy' || value._type == 'org.uengine.model.Command' || value._type == 'org.uengine.model.External'"
                                flat>
                            <v-col>
                                <v-autocomplete
                                        v-model="value.aggregateId"
                                        style="margin-left: 20px; margin-right: 20px;"
                                        :items="aggregateList"
                                        item-text="name"
                                        item-value="id"
                                        label="Select Aggregate" persistent-hint>
                                </v-autocomplete>
                            </v-col>
                        </v-layout>
                        <v-layout v-else-if="value._type == 'org.uengine.model.Aggregate'">
                            <v-divider dark style="margin-top: 10px; margin-bottom: 10px;"></v-divider>
                            <v-btn block color="info" rounded @click="umlDiagramOpen()"> Edit Domain Model by UML
                            </v-btn>
                        </v-layout>

=======
                              v-if="value.name == 'Event' || value.name == 'Policy' || value.name == 'Command' || value.name == 'external'">Associated Aggregate</span>
                        <v-layout
                                v-if="value.name == 'Event' || value.name == 'Policy' || value.name == 'Command' || value.name == 'external'"
                                flat>
                            <v-col>
                                <v-autocomplete
                                        style="margin-left: 20px; margin-right: 20px;" v-model="aggregate"
                                        :items="aggregateList" label="Select Aggregate" persistent-hint>
                                </v-autocomplete>
                            </v-col>
                        </v-layout>
                        <v-layout v-else-if="value.name == 'Aggregate'">
                            <v-divider dark style="margin-top: 10px; margin-bottom: 10px;"></v-divider>
                            <v-btn block color="info" rounded @click="umlDiagramOpen()"> Edit Domain Model by UML</v-btn>
                        </v-layout>

                        <span class="headline" v-if="value.name == 'Event'">Trigger</span>
                        <v-layout v-if="value.name == 'Event'" flat>
                            <v-radio-group v-model="publishType" style="padding-left: 10px;">
                                <v-row>
                                    <v-col>
                                        <v-radio label="Pre Persist" value="@PrePersist"></v-radio>
                                        <v-radio label="Pre Update" value="@PreUpdate"></v-radio>
                                        <v-radio label="Pre Delete" value="@PreDelete"></v-radio>
                                    </v-col>
                                    <v-col>
                                        <v-radio label="Post Persist" value="@PostPersist"></v-radio>
                                        <v-radio label="Post Update" value="@PostUpdate"></v-radio>
                                        <v-radio label="Post Delete" value="@PostDelete"></v-radio>
                                    </v-col>
                                </v-row>
                            </v-radio-group>
                        </v-layout>


>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8
                    </v-card-text>
                </v-card>
            </v-list>
        </v-navigation-drawer>
    </v-layout>
</template>

<script>
<<<<<<< HEAD
    var _ = require('lodash')
    var googleTranslate = require('google-translate')(process.env.VUE_APP_TRANSLATE_KEY);
    var changeCase = require('change-case')
    //https://www.npmjs.com/package/change-case

=======
    var googleTranslate = require('google-translate')(process.env.VUE_APP_TRANSLATE_KEY);
>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8
    var tensify = require('tensify');

    export default {
        name: 'modeling-property-panel',
        props: {
            value: Object,
            titleName: String,
<<<<<<< HEAD
            img: String,
        },
        computed: {
            aggregateList: function () {
=======
            inputText: String,
            publishType: String,
            connectAggregateName: String,
            connectAggregateEntity: Array,
            otherList: Array,
            img: String,
            innerAggregate: Object,
            entity: Array,
            fieldDescriptors: Array,
        },
        computed: {
            aggregateList: function () {
                var designer = this.$parent.getComponent('modeling-designer')

                var tmp = []
                designer.value.definition.forEach(function (tmpData) {
                    if (tmpData._type == 'org.uengine.uml.model.Aggregate')
                        tmp.push(tmpData.inputText)
                })
                return tmp
            },
            // aggregateListName:function(){
            //   var me = this
            //     var tmp= []
            //   me.aggregateList.forEach(function (agg) {
            //       tmp.push(agg.inputText)
            //   })
            //     return tmp
            // },
            commandNameList: function () {
>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8
                var designer = this.$parent.getComponent('modeling-designer')

                var tmp = []

<<<<<<< HEAD
                designer.value.definition.forEach(function (tmpData) {
                    if (tmpData._type == 'org.uengine.model.Aggregate')

                        var ob = {
                            'name': tmpData.name,
                            'id': tmpData.elementView.id
                        }
                    tmp.push(ob)
=======
                var tmp = []
                var inner = false
                // console.log(designer.value.relation);
                this.innerAggregate.event.forEach(function (domain) {
                    if (designer.value.relation.length == 0) {
                        //연결
                        tmp.push(domain.inputText)
                    } else {
                        designer.value.relation.forEach(function (relation, index) {
                            // console.log(relation.to)
                            // console.log(domain.elementView.id)
                            if (relation.to == domain.elementView.id) {
                                inner = true
                            }
                            if ((designer.value.relation.length - 1) == index && inner == false) {
                                tmp.push(domain.inputText)
                            }
                        })
                        inner = false
                    }
>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8
                })


                tmp = tmp.filter(n => n)
                return tmp
            },
        },
        data: function () {
            return {
<<<<<<< HEAD
                show: false,
=======
                radioCheck: '',
>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8
                navigationDrawer: false,
                _item: this.value,
                preventWatch: false,
                x: null,
                y: null,
                width: null,
                height: null,
                style: [],
                angle: null,
                connectedList: [],
                connectedListName: {},
<<<<<<< HEAD
                restfulList: ['GET', 'POST', 'PATCH', 'DELETE'],
                entityTypeList: ['Int', 'String', 'Float', 'Double', 'Long'],
                entityType: '',
                entityName: '',
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
                temporaryList: [],
=======
                componentKey: 0,
                restApiList: ['GET', 'POST', 'PATCH', 'DELETE'],
                restApiType: '',
                entityTypeList: ['int', 'String', 'float', 'double', 'long'],
                entityType: '',
                entityName: '',
                aggregate: '',
                headers: [{text: 'type', value: 'type', align: 'center'}, {
                    text: 'name',
                    value: 'name',
                    align: 'center'
                }, {
                    text: 'Actions',
                    value: 'action',
                    sortable: false,
                    align: 'center'
                },],
                translateText: '',
                usedTranslate: false,
                UMLInput: '',
>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8

            }
        },
        created: function () {

        },
        mounted: function () {
        },
        watch: {
<<<<<<< HEAD
            "value.relationKey": function (newVal) {
                var me = this

                // me.value.relationValue = newVal
                if (newVal == 'RestAPI') {
                    me.value.relationValue = 'Pub/Sub'
                }
                me.value.sourceElement.eventToPolicy = me.value.relationValue
                me.value.targetElement.eventToPolicy = me.value.relationValue
                me.value.targetElement.relationEventInfo = me.value.sourceElement;

            },
            "value.name": function (newVal) {
                var me = this

                me.translateText = '';
                me.getTranslate(newVal)
                me.setNameCase(newVal)

=======
            radioCheck:function(newVal){
                var me = this
                if(newVal == 'RestAPI'){
                    me.restApiTypeSet(me.value, 'Pub/Sub')
                }
            },
            restApiType: function (newVal) {
                var me = this
                console.log(me.value, newVal)
                if (me.value._type == 'org.uengine.uml.model.Command') {
                    me.value.restApi = newVal
                } else if (me.value._type == 'org.uengine.uml.model.relation') {
                    me.restApiTypeSet(me.value,newVal)
                    // if(me.radioCheck == 'RestAPI'){
                    //     me.restApiTypeSet(me.value, 'Pub/Sub')
                    // }
                }
                // me.restApiTypeSet(me.value,newVal)
                // this.$emit('update:restApi', newVal)
            },

            publishType: function (newVal) {
                console.log(newVal)
                this.$emit('update:publishType', newVal)
            },
            inputText: function (newVal) {
                // console.log(newVal)
                if (this.input != newVal) {
                    this.input = newVal
                }
            },
            input: function (newVal) {
                var me = this

                me.translateText = '';
                if (this.value.name == 'Event') {
                    googleTranslate.detectLanguage(newVal, function (err, detection) {
                        if (detection.language == 'ko') {
                            googleTranslate.translate(newVal, 'en', function (err, translation) {
                                me.usedTranslate = true
                                me.translateText = _.camelCase(tensify(translation.translatedText).past_participle, {pascalCase: true});
                            });
                        }
                    });
                } else if (this.value.name == 'policy') {
                    googleTranslate.detectLanguage(newVal, function (err, detection) {
                        if (detection.language == 'ko') {
                            googleTranslate.translate(newVal, 'en', function (err, translation) {
                                me.usedTranslate = true
                                me.translateText = _.camelCase(translation.translatedText, {pascalCase: true});
                            });
                        }
                    });
                } else {
                    googleTranslate.detectLanguage(newVal, function (err, detection) {
                        if (detection.language == 'ko') {
                            googleTranslate.translate(newVal, 'en', function (err, translation) {
                                me.usedTranslate = true
                                me.translateText = translation.translatedText;
                            });
                        }
                    });
                }


                if (this.titleName == "Aggregate") {
                    this.$emit('update:inputText', newVal)
                } else if (this.titleName == "Boundary Context") {
                    this.$emit('update:inputText', newVal)
                } else {
                    if (this.value.name == 'Event') {
                        if (this.selectAggregate.length > 0) {
                            this.$emit('update:inputText', newVal)
                            this.$emit('update:aggregateText', '\n \n \n Aggregate:\n' + this.selectAggregate)
                        } else {
                            this.$emit('update:inputText', newVal)
                        }
                    } else {
                        if (this.selectAggregate.length > 0) {
                            this.$emit('update:inputText', newVal)
                            this.$emit('update:aggregateText', '\n \n \n Aggregate:\n' + this.selectAggregate)
                        } else {
                            this.$emit('update:inputText', newVal)
                        }
                    }
                }
            },
            selectAggregate: function (newVal) {
                this.$emit('update:aggregate', newVal)
                this.$emit('update:inputText', this.input)
                this.$emit('update:aggregateText', '\n \n \n Aggregate:\n' + newVal)
>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8
            },
            "value.drawer": function (val) {
                this.navigationDrawer = val;
<<<<<<< HEAD
=======
                if (this.inputText != this.input) {
                    this.input = this.inputText
                }
>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8
            },
            //프로퍼티 창이 오픈되었을 때 모델값을 새로 반영한다.
            navigationDrawer: {
                handler: function (val, oldval) {
                    var opengraph = this.$parent.getComponent('opengraph')
                    if (this.titleName == 'Aggregate') {
                        // this.getRelation()
                    }
                    if (val == true) {
                        this._item = this.value;

                        if (this.value.elementView) {
                            this.x = this.value.elementView.x;
                            this.y = this.value.elementView.y;
                            this.width = this.value.elementView.width;
                            this.height = this.value.elementView.height;
                        }
                        this.$emit('update:drawer', val);

                    } else {
                        //프로퍼티 에디팅 해제.
                        this.$emit('update:drawer', false);
                    }
                }
            },
            x: function (val) {
                this._item.elementView.x = val;
                this.$emit('update:value', this._item);
            },
            y: function (val) {
                this._item.elementView.y = val;
                this.$emit('update:value', this._item);
            },
            width: function (val) {
                this._item.elementView.width = val;
                this.$emit('update:value', this._item);
            },
            height: function (val) {
                this._item.elementView.height = val;
                this.$emit('update:value', this._item);
            },
            angle: function (val) {
                this._item.elementView.angle = val;
                this.$emit('update:value', this._item);
            },
            style: {
                handler: function (newVal, oldVal) {
                    var style = {};
                    if (newVal && newVal.length) {
                        $.each(newVal, function (i, item) {
                            style[item.key] = item.value;
                        });
                    }
                    var view = this._item.elementView || this._item.relationView;
                    view.style = JSON.stringify(style);
                    this.$emit('update:value', this._item);
                },
                deep: true
            },
<<<<<<< HEAD
            "value.AggregateName": function (val) {

                var designer = this.$parent.getComponent('modeling-designer')
                console.log(designer)

=======
            aggregate: function (val) {
                // console.log(val)
                this.$emit('update:connectAggregateName', val);
>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8
            }
        },
        mounted: function () {

        },
        methods: {
<<<<<<< HEAD
            setNameCase: _.debounce(
                function (newVal) {
                    var me = this
                    if (me.value._type == 'org.uengine.model.Event' || me.value._type == 'org.uengine.model.Command' || me.value._type == 'org.uengine.model.Policy' || me.value._type == 'org.uengine.model.Aggregate') {
                        me.value.namePascalCase = changeCase.pascalCase(newVal)
                        me.value.nameCamelCase = changeCase.camelCase(newVal)
                    }
                }, 200
            ),
            getTranslate: _.debounce(
                function (newVal) {
                    var me = this
                    if (me.value._type == 'org.uengine.model.Event') {
                        try {
                            googleTranslate.detectLanguage(newVal, function (err, detection) {
                                if (detection.language != 'en') {
                                    googleTranslate.translate(newVal, 'en', function (err, translation) {
                                        me.usedTranslate = true
                                        me.translateText = changeCase.pascalCase(translation.translatedText);
                                    });
                                }
                            });
                        } catch (e) {
                            console.log(e)
                        }

                    } else if (me.value._type == 'org.uengine.model.Policy') {

                        try {
                            googleTranslate.detectLanguage(newVal, function (err, detection) {
                                if (detection.language != 'en') {
                                    googleTranslate.translate(newVal, 'en', function (err, translation) {
                                        me.usedTranslate = true
                                        me.translateText = changeCase.pascalCase(translation.translatedText);
                                    });
                                } else if (detection.language == 'en') {
                                    me.usedTranslate = true
                                    me.translateText = changeCase.pascalCase(newVal)
                                }
                            });
                        } catch (e) {
                            console.log(e)
                        }

                    } else if (me.value._type == 'org.uengine.model.Aggregate') {

                        try {
                            googleTranslate.detectLanguage(newVal, function (err, detection) {
                                if (detection.language != 'en') {
                                    googleTranslate.translate(newVal, 'en', function (err, translation) {
                                        me.usedTranslate = true
                                        me.translateText = changeCase.pascalCase(translation.translatedText);
                                    });
                                } else if (detection.language == 'en') {
                                    me.usedTranslate = true
                                    me.translateText = changeCase.pascalCase(newVal)
                                }
                            });
                        } catch (e) {
                            console.log(e)
                        }

                    } else {
                        try {
                            googleTranslate.detectLanguage(newVal, function (err, detection) {
                                if (detection.language != 'en') {
                                    googleTranslate.translate(newVal, 'en', function (err, translation) {
                                        me.usedTranslate = true
                                        me.translateText = translation.translatedText;
                                    });
                                }
                            });
                        } catch (e) {
                            console.log(e)
                        }

                    }
                }, 500
            ),
            umlDiagramOpen() {
                var me = this
                console.log("umlDiagram-Open")
                me.$ModelingBus.$emit('umlDiagram');
            },
            deleteEntity(val) {
                var me = this

                if (me.value._type == 'org.uengine.model.Aggregate') {
                    me.value.aggregateRoot.fieldDescriptors.forEach(function (element, idx) {
                        if (element.name == val.name && element.type == val.type) {

                            if (idx > -1)
                                me.value.aggregateRoot.fieldDescriptors.splice(idx, 1)

                            // me.$emit('update:aggregateRoot.fieldDescriptors', me.aggregateRoot.fieldDescriptors);
                        }
                    })
                } else {

                    me.value.fieldDescriptors.forEach(function (element, idx) {
                        if (element.name == val.name && element.type == val.type) {

                            if (idx > -1)
                                me.value.fieldDescriptors.splice(idx, 1)
=======
            umlDiagramOpen() {
                var me = this
                console.log("aa")
                me.$ModelingBus.$emit('umlDiagram');
            },
            deleteEntity(entity, val) {
                var me = this

                me.entity.forEach(function (element, idx) {
                    if (element.name == val.name && element.type == val.type) {

                        if (idx > -1)
                            me.entity.splice(idx, 1)

                        me.$emit('update:entity', me.entity);
                    }
                })

                console.log(val)
            },
            UMLDeleteEntity(entity, val) {
                var me = this

                me.UMLEntity.forEach(function (element, idx) {
                    if (element.name == val.name && element.type == val.type) {

                        if (idx > -1)
                            me.UMLEntity.splice(idx, 1)

                        me.$emit('update:UMLEntity', me.UMLEntity);
                    }
                })
            },
            restApiTypeSet(val, set) {
                val.sourceElement.relationInfo = set
                val.targetElement.relationInfo = set
                val.targetElement.relationEventInfo = val.sourceElement;

                val.relationType = set;
            },
            changeTranslate() {
                this.input = this.translateText
                this.usedTranslate = false
            },
            entityADD: function (type, name) {
                var me = this
                // console.log(type, name);
                if (type.length != 0 && name.length != 0) {

                    let tmpObject = {"type": type, "name": name, "upName": name.charAt(0).toUpperCase() + name.slice(1)}
                    me.entity.push(tmpObject);
                    this.entityType = ""
                    this.entityName = ""

                } else {
                    var designer = this.$parent.getComponent('modeling-designer')
                    designer.text = "TYPE & NAME INPUT REQUEST"
                    designer.snackbar = true
                }
            },
            UMLEntityADD: function (type, name) {
                var me = this
                if (type.length != 0 && name.length != 0) {

                    let tmpObject = {"type": type, "name": name, "upName": name.charAt(0).toUpperCase() + name.slice(1)}
                    me.fieldDescriptors.push(tmpObject);
                    this.entityType = ""
                    this.entityName = ""

                } else {
                    var designer = this.$parent.getComponent('modeling-designer')
                    designer.text = "TYPE & NAME INPUT REQUEST"
                    designer.snackbar = true
                }
            },


            entitySub: function (idx) {
                var me = this
                me.entity[idx] = null
                me.entity = me.entity.filter(n => n)
>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8

                            // me.$emit('update:fieldDescriptors', me.fieldDescriptors);
                        }
                    })

                }
            },
            changeTranslate() {
                this.value.name = this.translateText
                this.usedTranslate = false
            },
            attributeADD: function (type, name) {
                var me = this
                if(type.length < 1 ) {
                    type = 'String'
                }
                if (type.length != 0 && name.length != 0) {
                    let tmpObject = null

                    tmpObject = {
                        "_type": "org.uengine.model.FieldDescriptor",
                        "name": name,
                        "namePascalCase": changeCase.pascalCase(name),
                        "nameCamelCase": changeCase.camelCase(name),
                        "className": type,
                    }

<<<<<<< HEAD
                    if (name == 'id') {
                        tmpObject = {
                            "_type": "org.uengine.model.FieldDescriptor",
                            "name": name,
                            "namePascalCase": changeCase.pascalCase(name),
                            "nameCamelCase": changeCase.camelCase(name),
                            "className": type,
                            "isKey": true
                        }
=======
                me.innerAggregate.event.forEach(function (eventTmp) {
                    if (eventTmp.inputText == eventInputText) {
                        eventId = eventTmp.elementView.id
>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8
                    }

                    if (me.value._type == "org.uengine.model.Aggregate") {
                        me.value.aggregateRoot.fieldDescriptors.push(tmpObject);
                    } else {
                        me.value.fieldDescriptors.push(tmpObject);

                    }

                    this.entityType = ""
                    this.entityName = ""

                } else {
                    var designer = this.$parent.getComponent('modeling-designer')
                    designer.text = "TYPE & NAME INPUT REQUEST"
                    designer.snackbar = true
                }
            },

        }
    }
</script>


<style lang="scss" rel="stylesheet/scss">
    .md-sidenav .md-sidenav-content {
        width: 400px;
    }

    .md-sidenav.md-right .md-sidenav-content {
        width: 600px;
    }
</style>
