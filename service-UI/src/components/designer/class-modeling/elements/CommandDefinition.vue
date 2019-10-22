<template>
    <div>
        <geometry-element
                selectable
                :movable="!value.editing"
                :resizable="!value.editing"
                connectable
                deletable
                :angle.sync="value.elementView.angle"
                :id.sync="value.elementView.id"
                :x.sync="value.elementView.x"
                :y.sync="value.elementView.y"
                :width.sync="value.elementView.width"
                :height.sync="value.elementView.height"
                v-on:dblclick="showProperty"
                v-on:selectShape="selectedActivity"
                v-on:deSelectShape="deSelectedActivity"
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
          'stroke': '#5099F7',
          fill: '#5099F7',
          'fill-opacity': 1,
          r: '1', 'z-index': '998'
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
                :img="'https://raw.githubusercontent.com/kimsanghoon1/k8s-UI/master/public/static/image/event/command.png'"
                :aggregate.sync="value.aggregate"
                :aggregateList.sync="aggregateList"
                :aggregateText.sync="value.aggregateText"
                :connectAggregateName.sync="value.connectAggregateName"
                :restApi.sync="value.restApi"
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
        name: 'command-definition',
        props: {},
        computed: {
            defaultStyle() {
                return {}
            },
            type() {
                return 'Command'
            },
            className() {
                return 'org.uengine.uml.model.Command'
            },
            createNew(elementId, x, y, width, height) {
                return {
                    _type: this.className(),
                    name: 'Command',
                    fieldDescriptors: [],
                    elementView: {
                        '_type': 'org.uengine.modeling.Command',
                        'id': elementId,
                        'x': x,
                        'y': y,
                        'width': width,
                        'height': height,
                        'style': JSON.stringify({}),
                        'z-index': 999
                    },
                    drawer: false,
                    selected: false,
                    inputText: '',
                    restApi: '',
                    editing: false,
                    connectAggregateName: '',
                    code: ''
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
            "value.connectAggregateName": function (newVal, oldVal) {
                console.log(newVal,oldVal)
                var me = this
                var designer = this.getComponent('modeling-designer')
                console.log(me.value.inputText)
                designer.value.definition.forEach(function (temp) {
                    console.log(temp.inputText, newVal)
                    if (temp._type == "org.uengine.uml.model.Aggregate" && temp.inputText == newVal) {
                        temp.innerAggregate[me.type.toLowerCase()].push(me.value)
                    }
                })

                this.value.code = me.setCommandTemplate()
            },
            "value.inputText": function () {
                // console.log(this.code)
                // this.code = this.codeGenerate;
                this.value.code = this.setCommandTemplate()
            }
        },
        mounted: function () {

        },
        methods: {

            setCommandTemplate() {
                var me = this;
                return Mustache.render(
                    "    @RequestMapping(value = \"/{{connectAggregateName}}/{{inputText}}/\", method = RequestMethod.GET, produces = \"application/json;charset=UTF-8\")\n" +
                    "    public void {{inputText}}(HttpServletRequest request, HttpServletResponse response \n " +
                    "    ) throws Exception { \n" +
                    "    \n"+
                    "    }\n\n", me.value)
            },
        }
    }
</script>


<style scoped lang="scss" rel="stylesheet/scss">

</style>
