<template>
    <div>
        <group-element
                selectable
                :movable="!value.editing"
                :resizable="!value.editing"
                connectable
                deletable
                :id.sync="value.elementView.id"
                :x.sync="value.elementView.x"
                :y.sync="value.elementView.y"
                :width.sync="value.elementView.width"
                :height.sync="value.elementView.height"
                v-on:selectShape="selectedActivity"
                v-on:deSelectShape="deSelectedActivity"
                v-on:dblclick="showProperty"
                v-on:addToGroup="onAddToGroup"
                :_style="{stroke:'black'}">
            <sub-elements>
                <!--title-->
                <text-element
                        :sub-width="'100%'"
                        :sub-height="titleH"
                        :sub-top="0"
                        :sub-left="0"
                        :sub-style="{'font-weight': 'bold','font-size': '16'}"
                        :text="value.name">
                </text-element>
            </sub-elements>
        </group-element>

        <modeling-property-panel
                v-model="value"
                titleName="BoundedContext"
        >
        </modeling-property-panel>

    </div>
</template>

<script>
    import Element from '../../modeling/Element'
    import GroupElement from "../../../opengraph/shape/GroupElement";

    export default {
        components: {GroupElement},
        mixins: [Element],
        name: 'bounded-context-definition',
        props: {},
        computed: {

            defaultStyle() {
                return {}
            },
            className() {
                return 'org.uengine.model.BoundedContext'
            },
            createNew(elementId, x, y, width, height) {
                return {
                    description_text: '이벤트의 내용을 정의하고 시스템의 경계를 구분\n.',
                    _type: this.className(),
                    name: 'BoundedContext'+x,
                    aggregates:[],
                    elementView: {
<<<<<<< HEAD
                        '_type': this.className(),
=======
                        '_type': 'org.uengine.uml.model.bounded',
>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8
                        'id': elementId,
                        'x': x,
                        'y': y,
                        'width': 300,
                        'height': 300,
                        'style': JSON.stringify({})
                    },
                    drawer: false,
                    checkValue:false,
                    selected: false,
<<<<<<< HEAD
                    portGenerated: 0
=======
                    inputText: 'Bounded Context'+x,
                    dataList: [],
                    restApi: String,
>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8
                }
            }
        },
        data: function () {
            return {
                itemH: 20,
                titleH: (this.value.classReference ? 60 : 30),
                reference: this.value.classReference != null,
                referenceClassName: this.value.classReference,
            };
        },
        created: function () {

        },
        watch: {
<<<<<<< HEAD
            'value.name': function (newVal) {
                var me = this
                me.value.checkValue = true
=======
            // 'value.drawer': function (newValue, oldValue) {
            //     var designer = this.getComponent('modeling-designer')
            //
            //     var me = this
            //     me.aggregateList=[]
            //     if (newValue == true) {
            //         me.value.dataList.forEach(function(aggregateId) {
            //             designer.value.definition.forEach(function (tmp) {
            //                 if(tmp.elementView.id == aggregateId) {
            //                     me.aggregateList.push(tmp.inputText)
            //                 }
            //             })
            //         })
            //     }
            // },
            'value.dataList': function () {
                var me = this
                if(me.value.dataList.length > 1) {
                    me.value.dataList.forEach(function (tmp) {
                        tmp.boundedContext = me.value.inputText
                    })
                }
            },
            'value.inputText': function (newVal) {
                var me = this
>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8
                this.value.elementView.x = this.value.elementView.x + 1
                this.$nextTick(function () {
                    this.value.elementView.x = this.value.elementView.x -1
                })
<<<<<<< HEAD
            },
            "value.drawer": function (newVal) {
                var me = this
                if (newVal == false) {
                    if (me.value.checkValue) {
                        if(me.value.aggregates.length > 0 ){

                            me.value.aggregates.forEach(function (agg) {
                                agg.boundedContext = JSON.parse(JSON.stringify(me.value.name))

                                if(agg.events.length > 0)
                                    agg.events.forEach(function (eve) {
                                        eve.boundedContext = JSON.parse(JSON.stringify(me.value.name))
                                    })

                                if(agg.commands.length > 0)
                                    agg.commands.forEach(function (co) {
                                        co.boundedContext = JSON.parse(JSON.stringify(me.value.name))
                                    })

                                if(agg.policies.length > 0)
                                    agg.policies.forEach(function (po) {
                                        po.boundedContext = JSON.parse(JSON.stringify(me.value.name))
                                    })
                            })

                        }
                        me.value.checkValue = false
                    }
=======

                if(me.value.dataList.length > 1) {
                    me.value.dataList.forEach(function (tmp) {
                        tmp.boundedContext = newVal
                    })
>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8
                }
            }

        },
        mounted: function () {
            var me = this
            var boundedId = this.value.elementView.id

            var $tr = $('#'+boundedId);
            $tr.parent().children().first().before($tr)

        },
        methods: {}
    }
</script>


<style scoped lang="scss" rel="stylesheet/scss">

</style>
