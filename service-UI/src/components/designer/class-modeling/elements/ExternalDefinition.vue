<template>
    <div>
        <geometry-element
                selectable
                :movable="!value.editing"
                :resizable="!value.editing"
                deletable
                :angle.sync="value.elementView.angle"
                :id.sync="value.elementView.id"
                :x.sync="value.elementView.x"
                :y.sync="value.elementView.y"
                :width.sync="value.elementView.width"
                :height.sync="value.elementView.height"
                v-on:selectShape="selectedActivity"
                v-on:deSelectShape="deSelectedActivity"
                v-on:dblclick="showProperty"
                :label="value.name"
                :_style="{
                'label-angle':value.elementView.angle,
                'font-weight': 'bold','font-size': '16'
                }"
        >
            <!--v-on:dblclick="$refs['dialog'].open()"-->
            <geometry-rect
                    :_style="{
          'fill-r': 1,
          'fill-cx': .1,
          'fill-cy': .1,
          'stroke-width': 1.4,
          'stroke': '#ED73B6',
          fill: '#ED73B6',
          'fill-opacity': 1,
          r: '1'
        }"
            >
            </geometry-rect>

            <sub-elements>
                <!--title-->
                <text-element
                        :sub-width="'100%'"
                        :sub-height="titleH"
                        :sub-top="0"
                        :sub-left="0"
                        :text="value.classReference ? value.classReference : '<< External >>'">
                </text-element>
            </sub-elements>
        </geometry-element>


        <modeling-property-panel
                v-model="value"
                :titleName="'External'"
                :img="'https://raw.githubusercontent.com/kimsanghoon1/k8s-UI/master/public/static/image/event/external.png'"
        >
        </modeling-property-panel>

    </div>
</template>

<script>
    import Element from '../../modeling/Element'

    export default {
        mixins: [Element],
        name: 'external-definition',
        props: {},
        computed: {
            defaultStyle() {
                return {}
            },
            className() {
                return 'org.uengine.model.External'
            },
            createNew(elementId, x, y, width, height) {

                return {
                    description_text: '외부 시스템\n',
                    _type: this.className(),
                    name: '',
                    elementView: {
<<<<<<< HEAD
                        '_type': this.className(),
=======
                        '_type': 'org.uengine.uml.model.External',
>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8
                        'id': elementId,
                        'x': x,
                        'y': y,
                        'width': width,
                        'height': height,
                        'style': JSON.stringify({})
                    },
                    checkValue: false,
                    drawer: false,
                    selected: false,
                    restApi: '',
                    editing: false
                }
            }
        },
        data: function () {
            return {
                itemH: 20,
                titleH: (this.value.classReference ? 60 : 30),
                reference: this.value.classReference != null,
                referenceClassName: this.value.classReference
            };
        },
        created: function () {

        },
        watch: {
            "value.name":function (newVal) {
                this.value.checkValue = true
            }
        },
        mounted: function () {

        },
        methods: {
            onRotateShape: function () {

            }
        }
    }
</script>


<style scoped lang="scss" rel="stylesheet/scss">

</style>
