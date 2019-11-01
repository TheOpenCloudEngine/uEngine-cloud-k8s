<!--
       * Todo: Property 작성 하는 부분 *
-->
<template>
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
            <!--  상단 이미지 및 선택 타이틀 이름-->
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

            <!--  해당 판넬의 내용  -->
            <v-list class="pt-0" dense flat>
                <v-divider></v-divider>
                <v-card v-if="value.name == 'Relation' && value.sourceElement._type == 'org.uengine.uml.model.Domain'">
                    <v-card-text>
                        <span class="headline">Type</span>
                        <v-layout>
                            <v-col>
                                <v-radio-group v-model="radioCheck" style="padding-left: 10px;">
                                    <v-radio label="Event-Driven(Pub/Sub)" value="RestAPI"></v-radio>
                                    <v-radio label="Request & Response(Restful)" value="Restful"></v-radio>
                                </v-radio-group>
                                <v-select :disabled="radioCheck != 'Restful'" v-model="restApiType" :items="restApiList"
                                          label="Request & Response(Restful)"></v-select>
                            </v-col>
                        </v-layout>
                    </v-card-text>
                </v-card>
                <v-card outlined v-else>
                    <v-card-text>
                        <span class="headline" v-if="titleName">{{titleName}} Name</span>
                        <v-textarea name="input-7-1" outline :label="'Input Text'" auto-grow
                                    v-model="input"></v-textarea>

                        <v-card outlined v-if="usedTranslate">
                            <v-card-text @click="changeTranslate()">
                                추천 단어 : {{ translateText }}
                            </v-card-text>
                            <v-card-text>
                                선택시 변경 됩니다.
                            </v-card-text>
                        </v-card>


                        <span class="headline" v-if="value.name == 'Event' || value.name == 'Aggregate'">Attributes</span>
                        <v-layout v-if="value.name == 'Event' || value.name == 'Aggregate'" flat>
                            <v-col>
                                <v-data-table
                                        :headers="headers"
                                        :items="entity"
                                        hide-default-header
                                        hide-default-footer
                                        class="elevation-1"
                                >

                                    <template v-slot:item.action="{ item }">
                                        <v-icon
                                                v-if="item.name != 'id'"
                                                small
                                                @click="deleteEntity(entity,item)"
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
                                                      required></v-text-field>
                                    </v-flex>
                                </v-row>
                                <v-row justify="end">
                                    <v-btn rounded color="primary" @click="entityADD(entityType,entityName);" dark>
                                        ADD ATTRIBUTE
                                    </v-btn>
                                </v-row>
                            </v-col>
                        </v-layout>

                        <v-layout v-if="value.name == 'Command'">
                            <v-autocomplete v-model="restApiType" :items="restApiList"
                                            label="REST API TYPE" persistent-hint>
                            </v-autocomplete>
                        </v-layout>


                        <span class="headline"
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


                    </v-card-text>
                </v-card>
            </v-list>
        </v-navigation-drawer>
    </v-layout>
</template>

<script>
    var googleTranslate = require('google-translate')(process.env.VUE_APP_TRANSLATE_KEY);
    var tensify = require('tensify');

    export default {
        name: 'modeling-property-panel',
        props: {
            drawer: Boolean,
            value: Object,
            titleName: String,
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
                var designer = this.$parent.getComponent('modeling-designer')

                var tmp = []
                var inner = false
                this.innerAggregate.command.forEach(function (command) {
                    if (designer.value.relation.length == 0) {
                        tmp.push(command.inputText)
                    } else {
                        designer.value.relation.forEach(function (relation, index) {
                            if (relation.from == command.elementView.id) {
                                inner = true
                            }
                            if (designer.value.relation.length - 1 == index && inner == false) {
                                tmp.push(command.inputText)
                            }
                        })
                        inner = false
                    }
                })
                return tmp
            },
            domainNameList: function () {
                var designer = this.$parent.getComponent('modeling-designer')

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
                })
                return tmp
            }
        },
        data: function () {
            return {
                radioCheck: '',
                navigationDrawer: false,
                _item: this.value,
                preventWatch: false,
                x: null,
                y: null,
                width: null,
                height: null,
                style: [],
                active: null,
                tracingTag: null,
                input: '',
                angle: null,
                selectAggregate: '',
                selectEvent: '',
                selectCommand: '',
                connectedList: [],
                connectedListName: {},
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

            }
        },
        created: function () {

        },
        mounted: function () {
        },
        watch: {
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
            },
            drawer: function (val) {
                this.navigationDrawer = val;
                if (this.inputText != this.input) {
                    this.input = this.inputText
                }
            },
            //프로퍼티 창이 오픈되었을 때 모델값을 새로 반영한다.
            navigationDrawer: {
                handler: function (val, oldval) {
                    var opengraph = this.$parent.getComponent('opengraph')
                    if (this.titleName == 'Aggregate') {
                        this.getRelation()
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
            aggregate: function (val) {
                // console.log(val)
                this.$emit('update:connectAggregateName', val);
            }
        },
        mounted: function () {

        },
        methods: {
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

            },
            addRelation: function (commandInputText, eventInputText) {
                var designer = this.$parent.getComponent('modeling-designer')
                var opengraph = this.$parent.getComponent('opengraph')

                var commandId, eventId;
                var me = this

                // console.log(this.innerAggregate['command'])
                me.innerAggregate.command.forEach(function (commandTmp) {
                    if (commandTmp.inputText == commandInputText) {
                        commandId = commandTmp.elementView.id
                    }
                })

                me.innerAggregate.event.forEach(function (eventTmp) {
                    if (eventTmp.inputText == eventInputText) {
                        eventId = eventTmp.elementView.id
                    }
                })

                var OGcommand = designer.canvas.getElementById(commandId)
                var OGevent = designer.canvas.getElementById(eventId)

                designer.canvas._RENDERER._CANVAS.connect(OGcommand, OGevent, null, null, null, null, null, null, null);

                this.selectCommand = ''
                this.selectEvent = ''

                this.getRelation()
                this.$forceUpdate()
            },
            getRelation() {
                var me = this
                me.connectedList = []
                var designer = this.$parent.getComponent('modeling-designer')
                let commandList = this.innerAggregate.command
                let domainList = this.innerAggregate.domain
                let relationList = designer.value.relation

                commandList.forEach(function (commandTmp) {
                    relationList.forEach(function (relationTmp) {
                        if (commandTmp.elementView.id == relationTmp.from) {
                            domainList.forEach(function (domainTmp) {
                                if (domainTmp.elementView.id == relationTmp.to) {
                                    me.connectedList.push({
                                        'to': commandTmp,
                                        'from': domainTmp
                                    })
                                }
                            })
                        }
                    })
                })
            }
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
