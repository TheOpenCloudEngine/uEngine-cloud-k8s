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
                v-on:rotateShape="onRotateShape"
<<<<<<< HEAD
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
          'stroke': '#5FC08B',
          fill: '#5FC08B',
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
                        :text="value.classReference ? value.classReference : '<< View >>'">
                </text-element>
            </sub-elements>
        </geometry-element>


        <modeling-property-panel
<<<<<<< HEAD
=======
                :drawer.sync="value.drawer"
                :titleName.sync="value.name"
                :inputText.sync="value.inputText"
                :img="'https://raw.githubusercontent.com/kimsanghoon1/k8s-UI/master/public/static/image/event/view.png'"
                :aggregate.sync="value.aggregate"
                :aggregateList.sync="aggregateList"
                :connectAggregateName.sync="value.connectAggregateName"
>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8
                v-model="value"
                :titleName="'View'"
                :img="'https://raw.githubusercontent.com/kimsanghoon1/k8s-UI/master/public/static/image/event/view.png'"
        >
        </modeling-property-panel>

    </div>
</template>

<script>
    import Element from '../../modeling/Element'

    export default {
        mixins: [Element],
        name: 'view-definition',
        props: {},
        computed: {
            defaultStyle() {
                return {}
            },
            className() {
                return 'org.uengine.model.View'
            },
            createNew(elementId, x, y, width, height) {
                return {
                    description_text: '행위와 결정을 하기 위하여 유저가 참고하는 데이터\n' +
                        '(데이터 프로젝션이 필요 :  CQRS 등으로 수집)',
                    _type: this.className(),
                    name: '',
                    aggregateName: '',
                    namePascalCase: '',
                    elementView: {
<<<<<<< HEAD
                        '_type': this.className(),
=======
                        '_type': 'org.uengine.uml.model.View',
>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8
                        'id': elementId,
                        'x': x,
                        'y': y,
                        'width': width,
                        'height': height,
                        'style': JSON.stringify({})
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
                    connectAggregateName: ''
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
            "value.name": function (newVal, oldVal) {
                if (newVal != oldVal)
                    this.value.checkValue = true
                this.value.namePascalCase = newVal.charAt(0).toUpperCase() + newVal.slice(1)
            },
=======
            "value.connectAggregateName": function (newVal) {
                console.log(newVal)
                var me = this
                var designer = this.getComponent('modeling-designer')
                console.log(me.value.inputText)
                designer.value.definition.forEach(function (temp) {
                    console.log(temp.inputText, newVal)
                    if (temp._type == "org.uengine.uml.model.Aggregate" && temp.inputText == newVal) {
                        temp.innerAggregate[me.type.toLowerCase()].push(me.value.inputText)
                    }
                })
            }
>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8
        },
        mounted: function () {

        },
        methods: {}
    }
</script>


<style scoped lang="scss" rel="stylesheet/scss">

</style>
