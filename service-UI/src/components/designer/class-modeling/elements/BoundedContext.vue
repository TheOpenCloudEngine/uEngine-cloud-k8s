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
                :_style="{stroke:'black'}">
            <sub-elements>
                <!--title-->
                <text-element
                        :sub-width="'100%'"
                        :sub-height="titleH"
                        :sub-top="0"
                        :sub-left="0"
                        :sub-style="{'font-weight': 'bold','font-size': '16'}"
                        :text="value.inputText">
                </text-element>
            </sub-elements>
        </group-element>

        <modeling-property-panel
                :drawer.sync="value.drawer"
                :titleName="value.name"
                :inputText.sync="value.inputText"
                :aggregateList="aggregateList"
                v-model="value"
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
            type() {
                return 'bounded'
            },
            className() {
                return 'org.uengine.uml.model.bounded'
            },
            createNew(elementId, x, y, width, height) {
                return {
                    _type: this.className(),
                    name: 'Bounded Context',
                    elementView: {
                        '_type': 'org.uengine.modeling.bounded',
                        'id': elementId,
                        'x': x,
                        'y': y,
                        'width': 300,
                        'height': 300,
                        'style': JSON.stringify({})
                    },
                    drawer: false,
                    selected: false,
                    inputText: 'Bounded Context',
                    dataList: [],
                    restApi: String,
                }
            }
        },
        data: function () {
            return {
                itemH: 20,
                titleH: (this.value.classReference ? 60 : 30),
                reference: this.value.classReference != null,
                referenceClassName: this.value.classReference,
                aggregateList: []
            };
        },
        created: function () {

        },
        watch: {
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
            'value.inputText': function (newVal) {
                this.value.elementView.x = this.value.elementView.x + 1
                this.$nextTick(function () {
                    this.value.elementView.x = this.value.elementView.x -1
                })
            }
        },
        mounted: function () {


        },
        methods: {}
    }
</script>


<style scoped lang="scss" rel="stylesheet/scss">

</style>
