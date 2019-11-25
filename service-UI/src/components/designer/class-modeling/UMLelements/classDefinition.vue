<template>
    <div>
        <geometry-element
                selectable
                :movable="!value.editing"
                :resizable="!value.editing"
                deletable
                connectable
                :id.sync="value.elementView.id"
                :x.sync="value.elementView.x"
                :y.sync="value.elementView.y"
                :width.sync="value.elementView.width"
                :height.sync="setHeight"
                :angle.sync="value.elementView.angle"
                v-on:selectShape="selectedActivity"
                v-on:deSelectShape="deSelectedActivity"
                v-on:dblclick="showProperty"
                v-on:labelChanged="onLabelChanged"
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
          'stroke': '#000000',
          'fill-opacity': 1,
          r: '1'
        }"
            >
            </geometry-rect>

            <sub-elements>
                <text-element
                        :sub-width="'100%'"
                        :sub-height="itemH+10"
                        :sub-style="{'font-weight': 'bold'}"
                        :text="'Class'">
                </text-element>

                <edge-element
                        :vertices="[[0,titleH - (value.classReference ? 10:0)],['100%',titleH - (value.classReference ? 10:0)]]"
                        :sub-style="{'arrow-start': 'none', 'stroke-width' : '0.5', 'arrow-end': 'none'}"

                >
                </edge-element>

                <text-element
                        v-if="value.fieldDescriptors.length > 0"
                        v-for="(item, index) in value.fieldDescriptors"
                        :sub-width="'90%'"
                        :sub-height="'30%'"
                        :sub-top="titleH + (index * 13)"
                        :sub-style="{'text-anchor': 'start', 'font-size': 15 }"
                        :text="'+'+item.name + ': ' + item.className"
                >
                </text-element>


                <edge-element
                        :vertices="[[0,setHeight/2+10],['100%',setHeight/2+10]]"
                        :sub-style="{'arrow-start': 'none', 'stroke-width' : '0.5', 'arrow-end': 'none'}"

                >
                </edge-element>
                <text-element
                        v-if="value.fieldDescriptors.length > 0"
                        v-for="(item, index) in value.fieldDescriptors"
                        :sub-width="'90%'"
                        :sub-height="'30%'"
                        :sub-top="setHeight/1.5 + (index * 13)"
                        :sub-style="{'text-anchor': 'start', 'font-size': 15 }"
                        :text="'+'+item.name + ': ' + item.className"
                >
                </text-element>


            </sub-elements>
        </geometry-element>


        <uml-property-panel
                v-model="value"
                titleName="UML-class"
                :img="'https://raw.githubusercontent.com/kimsanghoon1/k8s-UI/master/public/static/image/event/event.png'"
        >
        </uml-property-panel>

    </div>
</template>

<script>
    import Element from '../../modeling/Element'

    export default {
        mixins: [Element],
        name: 'uml-class-definition',
        props: {},
        computed: {
            namePascalCase() {
                var me = this
                return me.name.charAt(0).toUpperCase() + me.name.slice(1)
            },
            defaultStyle() {
                return {}
            },
            className() {
                return 'org.uengine.uml.model.Class'
            },
            createNew(elementId, x, y, width, height, angle) {
                return {
                    _type: this.className(),
                    name: 'Class',
                    namePascalCase: '',
                    fieldDescriptors: [],
                    elementView: {
                        '_type': this.className(),
                        'id': elementId,
                        'x': x,
                        'y': y,
                        'width': 200,
                        'height': 150,
                        'style': JSON.stringify({}),
                        'angle': 0,
                    },
                    drawer: false,
                    selected: false,
                    editing: false,
                }
            }
        },
        data: function () {
            return {
                itemH: 10,
                titleH: (this.value.classReference ? 60 : 30),
                reference: this.value.classReference != null,
                referenceClassName: this.value.classReference,
                setHeight: 100,
            };
        },
        created: function () {

        },
        watch: {
            "value.fieldDescriptors": function () {
                var add = this.value.fieldDescriptors.length * 20
                this.setHeight = 100 + add

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
