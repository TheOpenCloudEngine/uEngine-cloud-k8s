<template>
    <div>
        <image-element
                selectable
                movable
                resizable
                connectable
                deletable
                :id.sync="value.elementView.id"
                :x.sync="value.elementView.x"
                :y.sync="value.elementView.y"
                :width.sync="value.elementView.width"
                :height.sync="value.elementView.height"
                v-on:selectShape="selectedActivity"
                v-on:deSelectShape="deSelectedActivity"
                v-on:dblclick="showProperty"
                v-on:addedToGroup="onAddedToGroup"
                :label="value.inputText"
                :image="'https://raw.githubusercontent.com/kimsanghoon1/k8s-UI/master/public/static/image/event/event.png'"
        >
            <!--v-on:dblclick="$refs['dialog'].open()"-->

            <sub-elements>
                <!--title-->
                <text-element
                        :sub-width="'100%'"
                        :sub-height="titleH"
                        :sub-top="0"
                        :sub-left="0"
                        :sub-style="{'font-weight': 'bold'}"
                        :text="value.classReference ? value.classReference : value.name">
                </text-element>

            </sub-elements>
        </image-element>


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
    import ImageElement from "../../../opengraph/shape/ImageElement";

    export default {
        components: {ImageElement},
        mixins: [Element],
        name: 'domain-event-definition',
        props: {},
        computed: {
            defaultStyle() {
                return {}
            },
            type() {
                return 'Domain'
            },
            className() {
                return 'org.uengine.uml.model.Domain'
            },
            createNew(elementId, x, y, width, height) {
                return {
                    _type: this.className(),
                    name: 'Domain',
                    fieldDescriptors: [],
                    elementView: {
                        '_type': 'org.uengine.modeling.Domain',
                        'id': elementId,
                        'x': x,
                        'y': y,
                        'width': 100,
                        'height': 100,
                        'style': JSON.stringify({})
                    },
                    drawer: false,
                    selected: false,
                    inputText: ''
                }
            }
        },
        data: function () {
            return {
                itemH: 20,
                titleH: (this.value.classReference ? 60 : 30),
                reference: this.value.classReference != null,
                referenceClassName: this.value.classReference
            };
        },
        created: function () {
            if (this.value.fieldDescriptors) {
                this.value.fieldDescriptors.forEach(function (attribute) {
                    if (!attribute.attributes)
                        attribute.attributes = {};
                });
            }

        },
        watch: {
            referenceClassName: function () {
                this.updateClassInfo();
            },
        },
        mounted: function () {

        },
        methods: {

            addAttribute: function () {
                this.value.fieldDescriptors.push({
                    name: 'attribute',
                    className: 'java.lang.String',
                    attributes: {},
                    _type: 'org.uengine.uml.model.Attribute'

                });
            },
            removeAttribute: function (attribute) {
                this.value.fieldDescriptors.splice(this.value.fieldDescriptors.indexOf(attribute), 1);
            },
            // className() {
            //     return this.className()
            // },
            updateClassInfo: function () {
                if (this.reference) {
                    var definitionAndClassName = this.referenceClassName.split("#");
                    var definitionName = definitionAndClassName[0];
                    var classNameOnly = definitionAndClassName[1];

                    var me = this;

                    var result;
                    var uri = (encodeURI(window.backend.$bind.ref + "/definition/raw/" + definitionName + ".ClassDiagram.json"))

                    console.log("try to get class diagram: " + uri);

                    var xhr = new XMLHttpRequest();
                    var me = this;

                    xhr.open('GET', uri, false);
                    xhr.setRequestHeader("access_token", localStorage['access_token']);
                    xhr.onload = function () {
                        result = JSON.parse(xhr.responseText)

                        var classDiagram = result.definition;

                        for (var i in classDiagram.classDefinitions[1]) {

                            var classDefinition = classDiagram.classDefinitions[1][i];
                            if (classDefinition.name == classNameOnly) {


                                classDefinition.elementView.x = me.value.elementView.x;
                                classDefinition.elementView.y = me.value.elementView.y;

                                classDefinition.classReference = me.referenceClassName;

                                me.value = classDefinition;
                                //me.$emit("input", me.value);

                                break;
                            }

                        }
                    };
                    xhr.send();


                }
            }
        }
    }
</script>


<style scoped lang="scss" rel="stylesheet/scss">

</style>

