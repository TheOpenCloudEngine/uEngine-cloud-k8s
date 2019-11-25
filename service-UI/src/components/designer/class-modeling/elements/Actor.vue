<template>
    <div>
        <geometry-element
                selectable
                movable
<<<<<<< HEAD
=======
                resizable
>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8
                deletable
                :id.sync="value.elementView.id"
                :x.sync="value.elementView.x"
                :y.sync="value.elementView.y"
                :width.sync="value.elementView.width"
                :height.sync="value.elementView.height"
                :angle.sync="value.elementView.angle"
                v-on:selectShape="selectedActivity"
                v-on:deSelectShape="deSelectedActivity"
                v-on:dblclick="showProperty"
                v-on:addedToGroup="onAddedToGroup"
                :_style="{
                'label-angle':value.elementView.angle,
                }"
        >
            <!--v-on:dblclick="$refs['dialog'].open()"-->
            <geometry-rect
                    :_style="{
          'fill-r': 1,
          'fill-cx': .1,
          'fill-cy': .1,
          'stroke-width': 1.4,
          'stroke': '#F8D454',
          fill: '#F8D454',
          'fill-opacity': 1,
          r: '1'
        }"
            >
            </geometry-rect>
            <sub-elements>
                <circle-element
                        :sub-align="'center'"
                        :sub-height="22"
                        :sub-width="22"
                        :sub-top="'28px'"
                >
                </circle-element>
                <geometry-line
                    :from="[50,35.5]"
                    :to="[50,63]"
                ></geometry-line>
                <geometry-line
                        :from="[28,42]"
                        :to="[72,42]"
                ></geometry-line>
                <geometry-line
                        :from="[50,63]"
                        :to="[72,80]"
                ></geometry-line>
                <geometry-line
                        :from="[50,63]"
                        :to="[27,80]"
                ></geometry-line>
                <!--title-->
                <text-element
                        :sub-width="'100%'"
                        :sub-height="titleH"
                        :sub-top="0"
                        :sub-left="0"
                        :text="value.classReference ? value.classReference : '<< ' + 'Actor' + ' >>'">
                </text-element>
                <text-element
                        :sub_style="{
                         'font-weight': 'bold'
                        }"
                        :sub-verticle-align="'bottom'"
                        :sub-height="titleH"
                        :sub-top="110"
                        :sub-left="0"
<<<<<<< HEAD
                        :text="value.classReference ? value.classReference :  value.name">
=======
                        :text="value.classReference ? value.classReference :  value.inputText">
>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8
                </text-element>
            </sub-elements>
        </geometry-element>
        <modeling-property-panel
<<<<<<< HEAD
                v-model="value"
                titleName="Actor"
=======
                :drawer.sync="value.drawer"
                :titleName="value.name"
                :inputText.sync="value.inputText"
                :innerAggregate="value.innerAggregate"
                :entity.sync="value.aggregateEntity"
                :img="'https://raw.githubusercontent.com/kimsanghoon1/k8s-UI/master/public/static/image/event/aggregate.png'"
                :restApi.sync="value.restApi"
                v-model="value"
>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8
        >
        </modeling-property-panel>

    </div>
</template>

<script>
    import Element from '../../modeling/Element'
    import RectangleElement from "../../../opengraph/shape/RectangleElement";
    import CircleElement from "../../../opengraph/shape/CircleElement";
    import EdgeElement from "../../../opengraph/shape/EdgeElement";
    import EllipseElement from "../../../opengraph/shape/EllipseElement";
    import GeometryLine from "../../../opengraph/geometry/Line";
    import SubElements from "../../../opengraph/shape/SubElements";

    export default {
        components: {SubElements, GeometryLine, EllipseElement, EdgeElement, CircleElement, RectangleElement},
        mixins: [Element],
        name: 'actor-definition',
        props: {},
        computed: {
<<<<<<< HEAD
=======
            // upName() {
            //     var me= this
            //     return me.inputText.charAt(0).toUpperCase() + me.inputText.slice(1)
            // },
>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8
            defaultStyle() {
                return {}
            },
            type() {
                return 'Actor'
            },
            className() {
                return 'org.uengine.uml.model.Actor'
            },
            createNew(elementId, x, y, width, height) {
                return {
<<<<<<< HEAD
                    _type: this.className(),
                    name: '',
=======
                    upName: '',
                    _type: this.className(),
                    name: 'Actor',
>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8
                    innerAggregate: {
                        'event': [],
                        'command': [],
                        'view': [],
                        'policy': [],
                        'external': []
                    },
                    elementView: {
                        '_type': 'org.uengine.uml.model.Actor',
                        'id': elementId,
                        'x': x,
                        'y': y,
                        'width': 100,
                        'height': 140,
                        'style': JSON.stringify({})
                    },
                    drawer: false,
                    selected: false,
<<<<<<< HEAD
=======
                    inputText: '',
>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8
                    boundedContext: ''
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
        watch: {},
        mounted: function () {


        },
        methods: {}

    }
</script>


<style scoped lang="scss" rel="stylesheet/scss">

</style>
