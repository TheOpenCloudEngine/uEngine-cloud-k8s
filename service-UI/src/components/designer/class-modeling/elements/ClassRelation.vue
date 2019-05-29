<template>
    <div>
        <edge-element
                selectable
                connectable
                deletable
                :vertices.sync="vertices"
                :from.sync="value.from"
                :to.sync="value.to"
                :_style="style_"
                :label="value.inputText"
                v-on:removeShape="onRemoveShape"
                v-on:dblclick="showProperty"
                v-on:selectShape="selectedActivity"
                v-on:deSelectShape="deSelectedActivity"
        >
        </edge-element>

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
                return 'org.uengine.uml.model.relation'
            },
            style_() {
                if (this.value._type == "org.uengine.uml.model.Aggregation") {
                    var style = {
                        "arrow-end": ("01".indexOf(this.value.sourceMultiplicity) > -1 ? "none" : "diamond"),
                        "arrow-start": ("01".indexOf(this.value.targetMultiplicity) > -1 ? "none" : "diamond")
                    };

                    if ("01".indexOf(this.value.sourceMultiplicity) == -1) {
                        if (!style.marker) style.marker = {};

                        style.marker.end = {
                            'id': 'OG.marker.AggregationMarker',
                            'size': [10, 8]
                        }
                    }

                    if ("01".indexOf(this.value.targetMultiplicity) == -1) {
                        if (!style.marker) style.marker = {};

                        style.marker.start = {
                            'id': 'OG.marker.AggregationMarker',
                            'size': [10, 8]
                        }
                    }

                    return style;

                } else if (this.value._type == "org.uengine.uml.model.Generalization") {
                    return ({
                        "arrow-end": "none",
                        'marker': {
                            'start': {
                                'id': 'OG.marker.GeneralizationMarker',
                                'size': [8, 8]
                            }
                        }

                    });

                }
            },
            type() {
                return 'org.uengine.uml.model.relation'
            },
            createNew(from, to, vertices) {
                return {
                    _type: this.type(),
                    name: 'Relation',
                    sourceClassName: from.name,
                    targetClassName: to.name,
                    from: from.elementView.id,
                    to: to.elementView.id,
                    selected: false,
                    relationView: {
                        style: JSON.stringify({
                            "arrow-start": "none",
                            "arrow-end": "none"
                        }),
                        value: vertices,
                        from: from.elementView.id,
                        to: to.elementView.id,
                        needReconnect: true
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
        watch: {


        },
        mounted: function () {
        },
        methods: {

        }
    }
</script>


<style scoped lang="scss" rel="stylesheet/scss">

</style>
