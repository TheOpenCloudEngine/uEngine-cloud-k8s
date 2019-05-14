<template>
    <div>
        <image-element
                selectable
                movable
                resizable
                connectable
                deletable
                labelEditable
                :id.sync="value.elementView.id"
                :x.sync="value.elementView.x"
                :y.sync="value.elementView.y"
                :width.sync="value.elementView.width"
                :height="value.elementView.height"
                v-on:selectShape="selectedActivity"
                v-on:deSelectShape="deSelectedActivity"
                v-on:dblclick="showProperty"
                :label="value.inputText"
                :image="'https://raw.githubusercontent.com/kimsanghoon1/k8s-UI/master/public/static/image/event/aggregate.png'"
        >
            <!--v-on:dblclick="$refs['dialog'].open()"-->
            <sub-elements labelEditable>
                <!--title-->
                <text-element
                        :sub-width="'100%'"
                        :sub-height="titleH"
                        :sub-top="0"
                        :sub-left="0"
                        :sub-style="{'font-weight': 'bold'}"
                        :text="value.classReference ? value.classReference : value.name">
                </text-element>
            </sub-elements>
        </image-element>


        <modeling-property-panel
                :drawer.sync="value.drawer"
                :titleName="value.name"
                :inputText.sync="value.inputText"
                v-model="value"
        >
        </modeling-property-panel>

    </div>
</template>

<script>
    import Element from '../../modeling/Element'

    export default {
        mixins: [Element],
        name: 'aggregate-definition',
        props: {},
        computed: {
            defaultStyle() {
                return {}
            },
            type() {
                return 'Aggregate'
            },
            className() {
                return 'org.uengine.uml.model.Aggregate'
            },
            createNew(elementId, x, y, width, height) {
                return {
                    _type: this.className(),
                    name: 'Aggregate',
                    fieldDescriptors: [],
                    elementView: {
                        '_type': 'org.uengine.modeling.Aggregate',
                        'id': elementId,
                        'x': x,
                        'y': y,
                        'width': 100,
                        'height': 100,
                        'style': JSON.stringify({})
                    },
                    drawer: false,
                    selected: false,
                    inputText: ''
                }
            }
        },
        data: function () {
            return {
                itemH: 200,
                titleH: (this.value.classReference ? 60 : 30),
                reference: this.value.classReference != null,
                referenceClassName: this.value.classReference
            };
        },
        created: function () {

        },
        watch: {
            drawer: function () {
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

