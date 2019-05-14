<template>
    <div>
        <vertical-lane-element
                selectable
                movable
                resizable
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
                :label="value.inputText"
                :_style="{stroke:'black'}">
        </vertical-lane-element>

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

    export default {
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
                    fieldDescriptors: [],
                    elementView: {
                        '_type': 'org.uengine.modeling.bounded',
                        'id': elementId,
                        'x': x,
                        'y': y,
                        'width': 100,
                        'height': 100,
                        'style': JSON.stringify({})
                    },
                    drawer: false,
                    selected: false,
                    inputText: '',
                    dataList: []
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
            'value.drawer': function(newValue, oldValue){
                var designer = this.getComponent('modeling-designer')

                var me = this

                if(newValue == true) {

                        designer.value.forEach(function (aggregateTmp) {
                            if(aggregateTmp.name == 'Aggregate') {
                                me.aggregateList.push(aggregateTmp.inputText)
                            }
                        })
                }
            }
        },
        mounted: function () {


        },
        methods: {

        }
    }
</script>


<style scoped lang="scss" rel="stylesheet/scss">

</style>

