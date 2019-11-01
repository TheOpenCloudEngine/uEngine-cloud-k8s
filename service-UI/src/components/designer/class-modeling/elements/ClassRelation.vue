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
                :label="value.relationType"
                v-on:removeShape="onRemoveShape"
                v-on:dblclick="showProperty"
                v-on:selectShape="selectedActivity"
                v-on:deSelectShape="deSelectedActivity"
        >
        </edge-element>

        <modeling-property-panel
                v-if="value.sourceElement._type != 'org.uengine.uml.model.Policy'"
                :drawer.sync="value.drawer"
                :titleName="value.name"
                :inputText.sync="value.inputText"
                v-model="value"
        ></modeling-property-panel>
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
                if (this.value._type == "org.uengine.uml.model.relation" && this.value.relationType == 'Pub/Sub') {
                    var style = {
                        "arrow-end": "block",
                        'stroke-width': '1.3',
                        'stroke-dasharray': '- ',
                        'font-size':20,
                    }
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
        watch: {},
        mounted: function () {

        },
        methods: {
        }
    }
</script>


<style scoped lang="scss" rel="stylesheet/scss">

</style>
