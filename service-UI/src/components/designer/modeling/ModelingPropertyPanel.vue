<!--
       * Todo: Property 작성 하는 부분 *
-->
<template>
    <v-layout wrap>
        <v-navigation-drawer v-model="navigationDrawer" absolute right temporary width="390">

            <v-list class="pa-1">
                <v-list-tile avatar>

                    <v-list-tile-avatar>
                        <img :src="img">
                    </v-list-tile-avatar>

                    <v-list-tile-content>
                        <v-list-tile-title>{{ titleName }}</v-list-tile-title>
                    </v-list-tile-content>

                </v-list-tile>
            </v-list>

            <v-list class="pt-0" dense>
                <v-divider></v-divider>

                <v-card v-if="value.name == 'Bounded Context'">
                    <v-card-title>
                        <span class="headline" v-if="titleName">{{titleName}} 내용 입력 </span>
                    </v-card-title>

                    <v-card-text>
                        <v-textarea name="input-7-1" outline :label="titleName" auto-grow v-model="input"></v-textarea>
                    </v-card-text>

                    <!-- <v-card-text>
                      <v-autocomplete v-model="restApiType" :items="restApiList" label="REST API TYPE" persistent-hint prepend-icon="mdi-city">
                      </v-autocomplete>
                    </v-card-text> -->
                </v-card>

                <v-card v-else-if="value.name == 'Aggregate'">
                    <v-card-text>
                        <v-textarea name="input-7-1" outline :label="titleName" auto-grow v-model="input"></v-textarea>
                    </v-card-text>

                    <v-data-table :items="aggregateEntity" class="elevation-1" hide-actions hide-headers>
                        <template v-slot:items="props">
                            <td>{{ props.item.type }}</td>
                            <td class="text-xs-right">{{ props.item.name }}</td>
                            <v-icon
                                    small
                                    @click="entitySub(props.index)"
                            >
                                delete
                            </v-icon>
                        </template>
                    </v-data-table>


                    <v-layout justify-center row style="align: center;">
                        <v-flex xs4>
                            <v-select v-model="entityType" :items="entityTypeList" label="Standard"
                                      style="margin-left: 10px; margin-right: 15px;"></v-select>
                        </v-flex>
                        <v-flex xs6>
                            <v-text-field v-model="entityName" :counter="10" label="Name" required></v-text-field>
                        </v-flex>
                    </v-layout>

                    <v-layout justify-end row wrap>
                        <v-btn round color="primary" @click="entityADD(entityType,entityName);" dark>Entity ADD</v-btn>
                    </v-layout>

                    <!-- <v-autocomplete v-model="restApiType" :items="restApiList" label="REST API TYPE" persistent-hint
                                                prepend-icon="mdi-city">
                                </v-autocomplete> -->

                    <v-card-title>
                        <span class="headline" v-if="titleName">연결 리스트 </span>
                    </v-card-title>
                    <!--expand 표시 부분  -->
                    <template>
                        <div>
                            <v-expansion-panel>

                                <v-expansion-panel-content EventExpand>
                                    <template v-slot:header>연결된 리스트</template>
                                    <v-card>
                                        <v-card-text style="padding-top: 0px">
                                            <v-layout row wrap>
                                                <v-flex xs1>
                                                    <v-card-text class="px-0" align="center">Index</v-card-text>
                                                </v-flex>
                                                <v-flex xs3>
                                                    <v-card-text class="px-0" align="center">To</v-card-text>
                                                </v-flex>
                                                <v-flex xs3>
                                                    <v-img style="margin-top: 13px"
                                                           :src="'https://raw.githubusercontent.com/kimsanghoon1/k8s-UI/master/public/static/image/event/right-arrow.png'"></v-img>
                                                </v-flex>
                                                <v-flex xs3>
                                                    <v-card-text class="px-0" align="center">From</v-card-text>
                                                </v-flex>
                                            </v-layout>
                                            <v-layout v-for="(item, index) in connectedList" row wrap>
                                                <v-flex xs1>
                                                    <v-card-text class="px-0" align="center">{{index + 1}}</v-card-text>
                                                </v-flex>
                                                <v-flex xs3>
                                                    <v-card-text class="px-0" align="center">{{item.to.inputText}}
                                                    </v-card-text>
                                                </v-flex>
                                                <v-flex xs3 grow>
                                                    <v-img style="margin-top: 13px"
                                                           :src="'https://raw.githubusercontent.com/kimsanghoon1/k8s-UI/master/public/static/image/event/right-arrow.png'"></v-img>
                                                </v-flex>
                                                <v-flex xs3>
                                                    <v-card-text class="px-0" align="center">{{item.from.inputText}}
                                                    </v-card-text>
                                                </v-flex>
                                            </v-layout>
                                        </v-card-text>
                                    </v-card>
                                </v-expansion-panel-content>

                                <v-expansion-panel-content CommandExpand>
                                    <template v-slot:header>연결X 리스트</template>
                                    <v-card>
                                        <v-card-text>
                                            연결 가능 리스트
                                        </v-card-text>
                                        <v-layout row wrap>
                                            <v-flex xs4>
                                                <v-autocomplete v-model="selectCommand" :items="commandNameList"
                                                                label="CommandList" persistent-hint
                                                                prepend-icon="mdi-city"></v-autocomplete>
                                            </v-flex>

                                            <!--<v-flex xs3>-->
                                            <!--<v-img style="margin-top: 13px"-->
                                            <!--:src="'https://raw.githubusercontent.com/kimsanghoon1/k8s-UI/master/public/static/image/event/right-arrow.png'"></v-img>-->
                                            <!--</v-flex>-->
                                            <v-flex xs4>
                                                <v-autocomplete v-model="selectEvent" :items="domainNameList"
                                                                label="EventList" persistent-hint
                                                                prepend-icon="mdi-city"></v-autocomplete>
                                            </v-flex>
                                            <v-flex xs1>
                                                <v-btn style="margin-top: 17px" small
                                                       @click="addRelation(selectCommand,selectEvent)" color="success">
                                                    추가
                                                </v-btn>
                                            </v-flex>
                                        </v-layout>
                                    </v-card>
                                </v-expansion-panel-content>

                            </v-expansion-panel>
                        </div>
                    </template>

                </v-card>

                <v-card v-else>
                    <v-card-text>
                        <v-textarea name="input-7-1" outline :label="titleName" auto-grow v-model="input"></v-textarea>
                    </v-card-text>

                    <v-autocomplete v-model="restApiType" :items="restApiList" label="REST API TYPE" persistent-hint
                                    prepend-icon="mdi-city">
                    </v-autocomplete>

                    <v-card-title>
                        <span class="headline" v-if="titleName">연결된 Aggregate</span>
                    </v-card-title>

                    <v-card-text style="margin-top: 17px font-size: 100px">
                        {{ connectAggregateName }}
                    </v-card-text>
                </v-card>

            </v-list>
        </v-navigation-drawer>
    </v-layout>
</template>

<script>
    export default {
        name: 'modeling-property-panel',
        props: {
            drawer: Boolean,
            value: Object,
            titleName: String,
            inputText: String,
            // aggregateList: Array,
            connectAggregateName: String,
            otherList: Array,
            img: String,
            restApi: String,
            innerAggregate: Object,
            aggregateEntity: Array,
        },
        computed: {
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
                this.innerAggregate.domain.forEach(function (domain) {
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
                componentKey: 0,
                restApiList: ['GET', 'POST', 'PUT', 'DELETE'],
                restApiType: '',
                entityTypeList: ['int', 'String', 'float', 'double', 'long'],
                entityType: '',
                entityName: '',

            }
        },
        created: function () {

        },
        mounted: function () {

        },
        watch: {
            inputText: function (newVal) {
                console.log(newVal)
                if(this.input != newVal) {
                    this.input = newVal
                }
            },
            input: function (newVal) {
                if (this.titleName == "Aggregate") {
                    this.$emit('update:inputText', newVal)
                } else if (this.titleName == "Boundary Context") {
                    this.$emit('update:inputText', newVal)
                } else {
                    if (this.selectAggregate.length > 0) {
                        this.$emit('update:inputText', newVal)
                        this.$emit('update:aggregateText', '\n \n \n Aggregate:\n' + this.selectAggregate)
                    } else {
                        this.$emit('update:inputText', newVal)
                    }
                }
            },
            restApiType: function (newVal) {
                // console.log(newVal);
                this.$emit('update:restApi', newVal)
            },
            restApi: function (newVal) {
                if (newVal != this.restApiType) {
                    this.restApiType = newVal
                }
            },
            selectAggregate: function (newVal) {
                this.$emit('update:aggregate', newVal)
                this.$emit('update:inputText', this.input)
                this.$emit('update:aggregateText', '\n \n \n Aggregate:\n' + newVal)
            },
            drawer: function (val) {
                this.navigationDrawer = val;
                console.log(this.input)
                console.log(this.inputText)
                if(this.inputText != this.input) {
                    this.input = this.inputText
                }
                // if(this.value._type=="org.uengine.uml.model.Aggregate" && val){
                //   // this.domainNameList;
                //   // this.commandNameList;
                //   console.log(this.domainNameList);
                // }

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
            }
        },
        mounted: function () {

        },
        methods: {
            entityADD: function (type, name) {
                var me = this
                console.log(type, name);
                if (type.length != 0 && name.length != 0) {

                    let tmpObject = {"type": type, "name": name}
                    me.aggregateEntity.push(tmpObject);
                    this.entityType = ""
                    this.entityName = ""
                } else {
                    var designer = this.$parent.getComponent('modeling-designer')
                    designer.text = "TYPE & NAME INPUT REQUEST"
                    designer.snackbar = true

                    console.log("GI");
                }
            },

            entitySub: function (idx) {
                var me = this
                me.aggregateEntity[idx] = null
                me.aggregateEntity = me.aggregateEntity.filter(n => n)

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

                me.innerAggregate.domain.forEach(function (eventTmp) {
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
