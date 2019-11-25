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
                v-on:dblclick="showProperty"
                v-on:selectShape="selectedActivity"
                v-on:deSelectShape="deSelectedActivity"
<<<<<<< HEAD
                v-on:addedToGroup="onAddedToGroup"
                :label.sync="value.name"
=======
                :label.sync="value.inputText"
>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8
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
                        :text="value.classReference ? value.classReference : '<< Command >>'">
                </text-element>
            </sub-elements>
        </geometry-element>

        <modeling-property-panel
<<<<<<< HEAD
=======
                :drawer.sync="value.drawer"
                :titleName.sync="value.name"
                :inputText.sync="value.inputText"
                :img="'https://raw.githubusercontent.com/kimsanghoon1/k8s-UI/master/public/static/image/event/command.png'"
                :aggregate.sync="value.aggregate"
                :aggregateList.sync="aggregateList"
                :aggregateText.sync="value.aggregateText"
                :connectAggregateName.sync="value.connectAggregateName"
>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8
                v-model="value"
                :titleName="'Command'"
                :img="'https://raw.githubusercontent.com/kimsanghoon1/k8s-UI/master/public/static/image/event/command.png'"
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
            namePascalCase() {
                var me = this
                return me.name.charAt(0).toUpperCase() + me.name.slice(1)
            },

            defaultStyle() {
                return {}
            },
            className() {
                return 'org.uengine.model.Command'
            },
            createNew(elementId, x, y, width, height) {
                return {
                    description_text: '행동, 결정 등의 값들에 대한 정의\n' +
                        '(UI 혹은 API)',
                    _type: this.className(),
                    name: '',
                    namePascalCase: '',
                    nameCamelCase:'',
                    aggregate: [] ,
                    aggregateId: '',
                    boundedContext: '',
                    restfulType: 'GET',
                    elementView: {
<<<<<<< HEAD
                        '_type': this.className(),
=======
                        '_type': 'org.uengine.uml.model.Command',
>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8
                        'id': elementId,
                        'x': x,
                        'y': y,
                        'width': width,
                        'height': height,
                        'style': JSON.stringify({}),
                        'z-index': 999
                    },
                    checkValue: false,
                    drawer: false,
                    selected: false,
<<<<<<< HEAD
                    editing: false,
=======
                    inputText: '',
                    restApi: '',
                    editing: false,
                    connectAggregateName: '',
                    code: '',
                    codeInputText: ''
>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8
                }
            }
        },
        data: function () {
            return {
                itemH: 20,
                titleH: (this.value.classReference ? 60 : 30),
                reference: this.value.classReference != null,
                referenceClassName: this.value.classReference,

            };
        },
        created: function () {

        },
        watch: {
<<<<<<< HEAD
            "value.aggregateId": function (newVal, oldVal) {
                var me = this
                var designer = this.getComponent('modeling-designer')

                designer.value.definition.forEach(function (temp) {
                    if (oldVal != null)
                        if (temp._type == "org.uengine.model.Aggregate" && temp.elementView.id == oldVal) {
                            temp.commands.forEach(function (co, idx) {
                                if (co.elementView.id == me.value.elementView.id) {
                                    if (idx > -1) temp.commands.splice(idx, 1)
                                }
                            })
                        }


                    if (temp._type == "org.uengine.model.Aggregate" && temp.elementView.id == newVal) {
                        // me.value.aggregateName = temp.name
                        me.value.aggregate = JSON.parse(JSON.stringify(temp))
                        me.value.fieldDescriptors = JSON.parse(JSON.stringify(temp.aggregateRoot.fieldDescriptors))
                        temp.commands.push(me.value)
                    }
                })
            },
            "value.name": function (newVal) {
                this.value.checkValue = true
            },
            "value.drawer": function (newVal) {
                var me = this
                if (newVal == false) {
                    if (me.value.checkValue) {
                        var me = this
                        var designer = this.getComponent('modeling-designer')

                        //text
                        // me.value.namePascalCase = me.value.name.charAt(0).toUpperCase() + me.value.name.slice(1)
                        // me.value.nameCamelCase = me.value.name.charAt(0).toUpperCase() + me.value.name.slice(1)

                        //update command name
                        designer.value.definition.forEach(function (element) {
                            if (element._type == "org.uengine.model.Aggregate" && element.name == me.value.aggregate.name) {
                                element.commands.forEach(function (co) {
                                    if (co.elementView.id == me.value.elementView.id) {
                                        co.name = me.value.name
                                    }
                                })
                            }
                        })
                        me.value.checkValue = false
                    }
                }
            }

=======
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
            "value.inputText": function (newVal) {
                // this.value.codeInputText = _.camelCase(newVal)
                this.value.codeInputText = newVal.charAt(0).toUpperCase() + newVal.slice(1)
                this.value.code = this.setCommandTemplate()
            }
>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8
        },
        mounted: function () {

        },
<<<<<<< HEAD
        methods: {}
=======
        methods: {

            setCommandTemplate() {
                var me = this;
                return Mustache.render(
                    "    @RequestMapping(value = \"/{{connectAggregateName}}/{{inputText}}/\", method = RequestMethod.{{restApi}}, produces = \"application/json;charset=UTF-8\")\n" +
                    "    public void {{codeInputText}}(HttpServletRequest request, HttpServletResponse response \n " +
                    "    ) throws Exception { \n" +
                    "    \n"+
                    "    }\n\n", me.value)
            },
        }
>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8
    }
</script>


<style scoped lang="scss" rel="stylesheet/scss">

</style>
