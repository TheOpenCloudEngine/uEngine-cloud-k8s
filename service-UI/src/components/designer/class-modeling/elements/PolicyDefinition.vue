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
<<<<<<< HEAD
                v-on:addedToGroup="onAddedToGroup"
                :label="value.name"
=======
                :label="value.inputText"
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
          'stroke': '#BB94BF',
          fill: '#BB94BF',
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
<<<<<<< HEAD
                        :text="value.classReference ? value.classReference : '<< Policy >>'">
=======
                        :text="value.classReference ? value.classReference : '<< ' + value.name + ' >>'">
>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8
                </text-element>
            </sub-elements>
        </geometry-element>


        <modeling-property-panel
<<<<<<< HEAD
                v-model="value"
                titleName="Policy"
                :img="'https://raw.githubusercontent.com/kimsanghoon1/k8s-UI/master/public/static/image/event/policy.png'"
=======
                :drawer.sync="value.drawer"
                :titleName="value.name"
                :inputText.sync="value.inputText"
                :img="'https://raw.githubusercontent.com/kimsanghoon1/k8s-UI/master/public/static/image/event/policy.png'"
                :aggregate.sync="value.aggregate"
                :aggregateList.sync="aggregateList"
                :connectAggregateName.sync="value.connectAggregateName"
                v-model="value"
>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8
        >
        </modeling-property-panel>

    </div>
</template>

<script>
    import Element from '../../modeling/Element'

    var Mustache = require('mustache')

    export default {
        mixins: [Element],
        name: 'policy-definition',
        props: {},
        computed: {
<<<<<<< HEAD
            namePascalCase() {
                var me = this
                return me.name.charAt(0).toUpperCase() + me.name.slice(1)
            },
            defaultStyle() {
                return {}
            },
            className() {
                return 'org.uengine.model.Policy'
=======
            defaultStyle() {
                return {}
            },
            type() {
                return 'Policy'
            },
            className() {
                return 'org.uengine.uml.model.Policy'
>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8
            },
            createNew(elementId, x, y, width, height) {

                return {
<<<<<<< HEAD
                    description_text: '업무정책\n' +
                        '이벤트에 대한 반응 (subscribe)\n',
                    _type: this.className(),
                    name: '',
                    namePascalCase: '',
                    aggregate:[],
                    aggregateId: '',
                    boundedContext: '',
                    elementView: {
                        '_type': this.className(),
=======
                    _type: this.className(),
                    name: 'Policy',
                    elementView: {
                        '_type': 'org.uengine.uml.model.Policy',
>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8
                        'id': elementId,
                        'x': x,
                        'y': y,
                        'width': width,
                        'height': height,
                        'style': JSON.stringify({})
                    },
<<<<<<< HEAD
                    checkValue: false,
                    drawer: false,
                    selected: false,
                    editing: false,
                    eventToPolicy: '',
                    relationEventInfo: [],
=======
                    drawer: false,
                    selected: false,
                    inputText: '',
                    restApi: '',
                    code:'',
                    editing: false,
                    connectAggregateName: '',
                    relationInfo: '',
                    relationEventInfo:[],
                    boundedContext: '',
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
<<<<<<< HEAD
=======
                aggregateList: [],
>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8
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
                            temp.policies.forEach(function (po, idx) {
                                if (po.elementView.id == me.value.elementView.id) {
                                    if (idx > -1) temp.policies.splice(idx, 1)
                                }
                            })
                        }

                    if (temp._type == "org.uengine.model.Aggregate" && temp.elementView.id == newVal) {
                        // me.value.aggregateName = temp.name
                        me.value.aggregate = JSON.parse(JSON.stringify(temp))
                        me.value.fieldDescriptors = JSON.parse(JSON.stringify(temp.aggregateRoot.fieldDescriptors))
                        temp.policies.push(me.value)
                    }
                })
            },
            "value.name": function (newVal, oldVal) {
                if (newVal != oldVal)
                    this.value.checkValue = true
            },
            "value.drawer": function (newVal) {
                var me = this
                if (newVal == false) {
                    if (me.value.checkValue) {
                        var me = this

                        //text 변경
                        // me.value.namePascalCase = me.value.name.charAt(0).toUpperCase() + me.value.name.slice(1)

                        //update policy name
                        var designer = this.getComponent('modeling-designer')
                        designer.value.definition.forEach(function (element) {
                            if (element._type == "org.uengine.model.Aggregate" && element.name == me.value.aggregateName) {
                                element.policies.forEach(function (po) {
                                    if (po.elementView.id == me.value.elementView.id) {
                                        po.name = me.value.name
                                    }
                                })
                            }
                        })

                        // 변경 완
                        me.value.checkValue = false
                    }
                }
=======
            "value.relationInfo": function (newVal) {
                if (newVal == 'Pub/Sub') {
                    this.value.code = this.setPolicyKafkaTemplate(this.value)
                } else {
                    this.value.code = this.setPolicyRestTemplate(this.value)
                }
            },
            "value.connectAggregateName": function (newVal) {
                console.log(newVal)
                var me = this
                var designer = this.getComponent('modeling-designer')
                // console.log(me.value.inputText)
                designer.value.definition.forEach(function (temp) {
                    if (temp._type == "org.uengine.uml.model.Aggregate" && temp.inputText == newVal) {
                        temp.innerAggregate[me.type.toLowerCase()].push(me.value)
                    }
                })
            },
            "value.inputText": function (newVal) {
                // console.log(this.value)
                // console.log(this.code)
                // this.code = this.codeGenerate;
                this.value.code = this.setPolicyKafkaTemplate(this.value)
>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8
            }
        },
        mounted: function () {

        },
<<<<<<< HEAD
        methods: {}
=======
        methods: {
            setPolicyKafkaTemplate(definition) {
                return Mustache.render(
                    "     @KafkaListener(topics = \"${eventTopic}\", groupId = \"{{inputText}}\") \n " +
                    "{{#relationEventInfo}}" +
                    "     public void on{{upName}}(@Payload String message, ConsumerRecord<?, ?> consumerRecord) {\n" +
                    "        System.out.println(\"##### listener : \" + message); \n" +
                    "        ObjectMapper objectMapper = new ObjectMapper();\n" +
                    "        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);\n\n" +
                    "           {{upName}} {{inputText}} = null; \n" +
                    "           try {\n\n" +
                    "               {{inputText}} = objectMapper.readValue(message, {{upName}}.class);\n" +
                    "               System.out.println(\" #### type = \" + {{inputText}}.getEventType());\n\n" +
                    "\n" +
                    "                  if( {{inputText}}.getEventType() != null && {{inputText}}.getEventType().equals({{upName}}.class.getSimpleName())){\n\n" +
                    "                   // TO-DO :: Implement your Logic here.  \n\n" +
                    "               }\n" +
                    "           } catch (Exception e) {\n\n" +
                    "           }\n" +
                    "       }\n" +
                    "{{/relationEventInfo}}\n" +
                    "\n\n", definition)
            },
            setPolicyRestTemplate(name, definition) {
                return Mustache.render(
                    " ", definition)
            }
        }
>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8
    }
</script>


<style scoped lang="scss" rel="stylesheet/scss">

</style>
