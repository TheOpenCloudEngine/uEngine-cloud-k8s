<template>
    <div>
        <edge-element
                v-if="value.sourceElement._type != 'org.uengine.uml.model.Policy'"
                selectable
                connectable
                deletable
                :vertices.sync="vertices"
                :from.sync="value.from"
                :to.sync="value.to"
                :_style="style_"
<<<<<<< HEAD
                :label="value.name"
=======
                :label="value.relationType"
>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8
                v-on:removeShape="onRemoveShape"
                v-on:dblclick="showProperty"
                v-on:selectShape="selectedActivity"
                v-on:deSelectShape="deSelectedActivity"
        >
        </edge-element>

        <uml-property-panel
            v-if="value.sourceElement._type == 'org.uengine.uml.model.Class'"
            v-model="value"
            :titleName="'Relation'"
        ></uml-property-panel>

        <modeling-property-panel
<<<<<<< HEAD
                v-else-if="value.sourceElement._type != 'org.uengine.uml.model.Class' && value.sourceElement._type != 'org.uengine.uml.model.Policy'"
                v-model="value"
                :titleName="'Relation'"
=======
                v-if="value.sourceElement._type != 'org.uengine.uml.model.Policy'"
                :drawer.sync="value.drawer"
                :titleName="value.name"
                :inputText.sync="value.inputText"
                v-model="value"
>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8
        ></modeling-property-panel>
    </div>
</template>

<script>
    import Element from '../../modeling/Element'
    import UmlPropertyPanel from "../../modeling/UMLPropertyPanel";

    export default {
        components: {UmlPropertyPanel},
        mixins: [Element],
        name: 'class-relation',
        props: {
            value: Object
        },

        created: function () {
            if (this.value && this.value.relationView) {

                this.value.from = this.value.relationView.from;
                this.value.to = this.value.relationView.to;

            }
        },
        computed: {
            defaultStyle() {
                return {}
            },
            className() {
                return 'org.uengine.model.Relation'
            },
            style_() {
<<<<<<< HEAD
                var style = {}
                if (this.value._type == "org.uengine.model.Relation" && this.value.relationValue == 'Pub/Sub') {
                    style = {
=======
                if (this.value._type == "org.uengine.uml.model.relation" && this.value.relationType == 'Pub/Sub') {
                    var style = {
>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8
                        "arrow-end": "block",
                        'stroke-width': '1.3',
                        'stroke-dasharray': '- ',
                        'font-size':20,
                    }
<<<<<<< HEAD
                } else if (this.value._type == "org.uengine.model.Relation" &&
                    this.value.relationValue == 'GET'&&
                    this.value.relationValue == 'POST' &&
                    this.value.relationValue == 'PATCH'&&
                    this.value.relationValue == 'DELETE') {
                    style = {
                        "arrow-end": "block",
                        'font-size':20,
                    }
                } else if(this.value.relationValue == '1'){
                    style = {
                        'stroke-width': '1.3',
                        'font-size':30,
                        'marker': {
                            'start': {
                                'id': 'OG.marker.RectangleMarker',
                                'size': [20, 20]
                            },
                            'mid': {
                                'id': 'OG.marker.RectangleMarker',
                                'size': [20, 20]
                            },
                            'end': {
                                'id': 'OG.marker.RectangleMarker',
                                'size': [20, 20]
                            }
                        }
                    }
                    this.value.name = "1:1"
                } else if( this.value.relationValue == '2' ){
                    style = {
                        'arrow-start': 'block',
                        "arrow-end": "diamond",
                        'stroke-width': '1.3',
                        'font-size':30,

                    }
                    this.value.name = "1:1"

                } else if( this.value.relationValue == '3' ){
                    style = {
                        'arrow-start': 'diamond',
                        "arrow-end": "block",
                        'stroke-width': '1.3',
                        'font-size':30,
                    }
                    this.value.name = "0:N"

                }
                return style
            },
            createNew(from, to, vertices) {
                return {
                    _type: this.className(),
                    name: '',
                    relationKey: '',
                    relationValue:'',
=======
                } else if (this.value._type == "org.uengine.uml.model.relation") {
                    var style = {
                        "arrow-end": "block",
                        'font-size':20,
                    }
                }
                return style
            },
            type() {
                return 'org.uengine.uml.model.relation'
            },
            createNew(from, to, vertices) {
                return {
                    _type: this.type(),
                    name: 'Relation',
                    relationType: '',
>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8
                    sourceElement: from,
                    targetElement: to,
                    from: from.elementView.id,
                    to: to.elementView.id,
                    selected: false,
                    relationView: {
                        style: JSON.stringify({
                            "arrow-start": "none",
                            "arrow-end": "none",
                        }),
                        value: vertices,
                        from: from.elementView.id,
                        to: to.elementView.id,
                        needReconnect: true,
                    },
                    sourceMultiplicity: 1,
                    targetMultiplicity: 1,
                    drawer: false
                }
            },
            vertices: {
                get: function () {
                    var style;
                    try {
                        return JSON.parse(this.value.relationView.value);
                    } catch (e) {
                        return null;
                    }
                },
                set: function (val) {
                    this.value.relationView.value = JSON.stringify(val);
                }
            }
        },
        data: function () {
            return {
                otherwise: false
            };
        },
<<<<<<< HEAD
        watch: {
            "value.relationValue":function (newVal) {
                if(this.value.sourceElement._type == "org.uengine.uml.model.Class" ){
                    console.log(newVal)
                }else{
                    this.value.name=newVal
                    this.value.sourceElement.eventToPolicy=newVal
                    this.value.targetElement.eventToPolicy=newVal
                    this.value.targetElement.relationEventInfo=this.value.sourceElement
                }
            },
        },
=======
        watch: {},
>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8
        mounted: function () {

        },
        methods: {
        }
    }
</script>


<style scoped lang="scss" rel="stylesheet/scss">

</style>
