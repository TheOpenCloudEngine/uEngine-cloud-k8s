<template>
    <div>
        <geometry-element
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
                :angle.sync="value.elementView.angle"
                v-on:selectShape="selectedActivity"
                v-on:deSelectShape="deSelectedActivity"
                v-on:dblclick="showProperty"
                v-on:rotateShape="onRotateShape"
                v-on:labelChanged="onLabelChanged"
                v-on:addedToGroup="onAddedToGroup"
                :label.sync="value.inputText"
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
          'stroke': '#F1A746',
          fill: '#F1A746',
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
                        :text="value.classReference ? value.classReference : '<< ' + value.name + ' >>'">
                </text-element>

            </sub-elements>
        </geometry-element>


        <modeling-property-panel
                :drawer.sync="value.drawer"
                :titleName.sync="value.name"
                :inputText.sync="value.inputText"
                :img="'https://raw.githubusercontent.com/kimsanghoon1/k8s-UI/master/public/static/image/event/event.png'"
                :aggregate.sync="value.aggregate"
                :entity.sync="value.entity"
                :aggregateList.sync="aggregateList"
                :connectAggregateName.sync="value.connectAggregateName"
                :connectAggregateEntity.sync="value.connectAggregateEntity"
                v-model="value"
        >
        </modeling-property-panel>

    </div>
</template>

<script>
    import Element from '../../modeling/Element'

    var Mustache = require('mustache')

    export default {
        mixins: [Element],
        name: 'domain-event-definition',
        props: {},
        computed: {
            defaultStyle() {
                return {}
            },
            type() {
                return 'Event'
            },
            className() {
                return 'org.uengine.uml.model.Domain'
            },
            createNew(elementId, x, y, width, height, angle) {
                return {
                    upName: '',
                    _type: this.className(),
                    name: 'event',
                    elementView: {
                        '_type': 'org.uengine.uml.model.Domain',
                        'id': elementId,
                        'x': x,
                        'y': y,
                        'width': 100,
                        'height': 100,
                        'style': JSON.stringify({}),
                        'angle': 0,
                    },
                    drawer: false,
                    selected: false,
                    inputText: '',
                    restApi: '',
                    editing: false,
                    connectAggregateName: '',
                    connectAggregateEntity:[],
                    entity: [],
                    code: '',
                    relationInfo:'',
                    boundedContext: ''
                }
            }
        },
        data: function () {
            return {
                itemH: 20,
                titleH: (this.value.classReference ? 60 : 30),
                reference: this.value.classReference != null,
                referenceClassName: this.value.classReference,
                aggregateList: [],


            };
        },
        created: function () {

        },
        watch: {
            "value.relationInfo": function (newVal) {
                // console.log(newVal)
            },
            "value.connectAggregateName": function (newVal, oldVal) {
                // console.log(newVal,oldVal)
                var me = this
                var designer = this.getComponent('modeling-designer')
                console.log(me.value.inputText)
                designer.value.definition.forEach(function (temp) {
                    console.log(temp.inputText, newVal)
                    if(temp._type == "org.uengine.uml.model.Aggregate" && temp.inputText == oldVal) {
                        temp.innerAggregate[me.type.toLowerCase()].splice(temp.innerAggregate[me.type.toLowerCase()].indexOf(oldVal),1);
                    }
                    if (temp._type == "org.uengine.uml.model.Aggregate" && temp.inputText == newVal) {
                        me.value.entity = JSON.parse(JSON.stringify(temp.aggregateEntity))
                        temp.innerAggregate[me.type.toLowerCase()].push(me.value.inputText)
                    }
                })
            },
            "value.inputText": function (newVal) {
                // console.log(this.value)
                // console.log(this.code)
                // this.code = this.codeGenerate;
                // console.log(newVal.charAt(0).toUpperCase(),newVal.slice(1))
                this.value.upName = newVal.charAt(0).toUpperCase() + newVal.slice(1)
                this.value.code = this.setEventTemplate(newVal, this.value)

            },
            "value.entity": function () {
                var me = this
                console.log(this.value)
                // console.log(this.code)
                // this.code = this.codeGenerate;
                this.value.code = this.setEventTemplate()
            }
        },
        mounted: function () {
        },
        methods: {
            setEventTemplate() {
                var me = this
                return Mustache.render(
                    "package com.example.template;\n" +
                    "\n" +
                    "import java.io.Serializable;\n" +
                    "\n" +
                    "public class {{upName}} extends AbstractEvent {\n" +
                    "\n" +
                    "{{#entity}}" +
                    "    public {{type}} {{name}};\n" +
                    "{{/entity}}\n" +
                    "\n" +
                    "{{#entity}}" +
                    "    public {{type}} get{{upName}}() {\n" +
                    "        return {{name}};\n" +
                    "    }\n" +
                    "\n" +
                    "    public void set{{upName}}({{type}} {{name}}) {\n" +
                    "        this.{{name}} = {{name}};\n" +
                    "    }\n" +
                    "{{/entity}}" +
                    "}", me.value)
            },
        }
    }
</script>


<style scoped lang="scss" rel="stylesheet/scss">
</style>
