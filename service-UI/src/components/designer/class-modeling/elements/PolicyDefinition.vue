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
                :label="value.inputText"
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
                        :text="value.classReference ? value.classReference : '<< ' + value.name + ' >>'">
                </text-element>
            </sub-elements>
        </geometry-element>


        <modeling-property-panel
                :drawer.sync="value.drawer"
                :titleName="value.name"
                :inputText.sync="value.inputText"
                :img="'https://raw.githubusercontent.com/kimsanghoon1/k8s-UI/master/public/static/image/event/policy.png'"
                :aggregate.sync="value.aggregate"
                :aggregateList.sync="aggregateList"
                :connectAggregateName.sync="value.connectAggregateName"
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
        name: 'policy-definition',
        props: {},
        computed: {
            defaultStyle() {
                return {}
            },
            type() {
                return 'Policy'
            },
            className() {
                return 'org.uengine.uml.model.Policy'
            },
            createNew(elementId, x, y, width, height) {

                return {
                    _type: this.className(),
                    name: 'Policy',
                    elementView: {
                        '_type': 'org.uengine.uml.model.Policy',
                        'id': elementId,
                        'x': x,
                        'y': y,
                        'width': width,
                        'height': height,
                        'style': JSON.stringify({})
                    },
                    drawer: false,
                    selected: false,
                    inputText: '',
                    restApi: '',
                    editing: false,
                    connectAggregateName: '',
                    relationInfo: '',
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
                console.log(newVal)
                if (newVal == 'Pub' || newVal == 'Sub') {
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
            }
        },
        mounted: function () {

        },
        methods: {
            setPolicyKafkaTemplate(definition) {
                return Mustache.render(
                    "    @KafkaListener(topics = \"${eventTopic}\", groupId = \"{{inputText}}\") \n " +
                    "    public void {{inputText}}(@Payload String message, ConsumerRecord<?, ?> consumerRecord) {\n" +
                    "        System.out.println(\"##### listener : \" + message); \n" +
                    "    }\n\n", definition)
            },
            setPolicyRestTemplate(name, definition) {
                return Mustache.render(
                    " ", definition)
            }
        }
    }
</script>


<style scoped lang="scss" rel="stylesheet/scss">

</style>
