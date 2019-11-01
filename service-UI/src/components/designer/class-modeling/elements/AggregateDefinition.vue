<template>
    <div>
        <geometry-element
                selectable
                movable
                resizable
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
                v-on:addedToGroup="onAddedToGroup"
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
          'stroke': '#F8D454',
          fill: '#F8D454',
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
                :innerAggregate="value.innerAggregate"
                :entity.sync="value.aggregateEntity"
                :img="'https://raw.githubusercontent.com/kimsanghoon1/k8s-UI/master/public/static/image/event/aggregate.png'"
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
        name: 'aggregate-definition',
        props: {},
        computed: {
            // upName() {
            //     var me= this
            //     return me.inputText.charAt(0).toUpperCase() + me.inputText.slice(1)
            // },
            defaultStyle() {
                return {}
            },
            type() {
                return 'Aggregate'
            },
            className() {
                return 'org.uengine.uml.model.Aggregate'
            },
            createNew(elementId, x, y, width, height) {
                return {
                    upName: '',
                    _type: this.className(),
                    name: 'Aggregate',
                    innerAggregate: {
                        'event': [],
                        'command': [],
                        'view': [],
                        'policy': [],
                        'external': []
                    },
                    elementView: {
                        '_type': 'org.uengine.uml.model.Aggregate',
                        'id': elementId,
                        'x': x,
                        'y': y,
                        'width': 100,
                        'height': 100,
                        'style': JSON.stringify({})
                    },
                    drawer: false,
                    selected: false,
                    inputText: '',
                    restApi: '',
                    aggregateEntity: [{type: "Long", name: "id", upName: "Id", id: true}],
                    aggregateCode: '',
                    repositoryCode: '',
                    eventListenerCode: '',
                    controllerCode: '',
                    boundedContext: ''
                }

            }
        },
        data: function () {
            return {
                itemH: 200,
                titleH: (this.value.classReference ? 60 : 30),
                reference: this.value.classReference != null,
                referenceClassName: this.value.classReference
            };
        },
        created: function () {

        },
        watch: {
            "value.inputText": function (newVal) {
                console.log(this.value)
                // console.log(this.code)
                // this.code = this.codeGenerate;
                this.value.aggregateCode = this.setAggregateTemplate()
                this.value.repositoryCode = this.setRepositoryTemplate()
                this.value.controllerCode = this.setControllerTemplate()
                this.value.eventListenerCode = this.setEventListenerTemplate()
                this.value.upName = newVal.charAt(0).toUpperCase() + newVal.slice(1)
            },
            "value.aggregateEntity": function () {
                var me = this
                this.value.aggregateCode = me.setAggregateTemplate()
            },
            "value.innerAggregate": {
                handler () {
                    this.value.aggregateCode = this.setAggregateTemplate()
                    this.value.repositoryCode = this.setRepositoryTemplate()
                    this.value.controllerCode = this.setControllerTemplate()
                    this.value.eventListenerCode = this.setEventListenerTemplate()
                },
                deep: true
            }
        },
        mounted: function () {
            var me = this
            var aggId = this.value.elementView.id

            var $tr = $('#'+aggId);
            $tr.parent().children().first().before($tr)

        },
        methods: {
            setRepositoryTemplate() {
                var me = this
                return Mustache.render(
                    "package com.example.template;\n " +
                    "import org.springframework.data.repository.PagingAndSortingRepository; \n " +
                    "public interface {{ upName }}Repository extends PagingAndSortingRepository < {{ upName }}, Long > { \n " +
                    "}\n", me.value)
            },
            setControllerTemplate(){
                var me = this
                return Mustache.render(
                    "package com.example.template;\n" +
                    "\n" +
                    "import org.springframework.beans.factory.annotation.Autowired;\n" +
                    "import org.springframework.web.bind.annotation.PathVariable;\n" +
                    "import org.springframework.web.bind.annotation.RequestMapping;\n" +
                    "import org.springframework.web.bind.annotation.RequestMethod;\n" +
                    "import org.springframework.web.bind.annotation.RestController;\n" +
                    "\n" +
                    "import javax.servlet.http.HttpServletRequest;\n" +
                    "import javax.servlet.http.HttpServletResponse;\n" +
                    "import java.util.List;\n" +
                    "\n" +
                    "@RestController\n" +
                    "public class {{ upName }}Controller {\n" +
                    "\n" +
                    "{{#innerAggregate}}" +
                    "{{#command}}" +
                    "{{{code}}}" +
                    "{{/command}}" +
                    "{{/innerAggregate}}" +
                    "}", me.value)
            },
            setEventListenerTemplate(){
                var me = this
                return Mustache.render("package com.example.template;\n" +
                    "\n" +
                    "import com.fasterxml.jackson.databind.DeserializationFeature;\n" +
                    "import com.fasterxml.jackson.databind.ObjectMapper;\n" +
                    "import org.apache.kafka.clients.consumer.ConsumerRecord;\n" +
                    "import org.apache.kafka.clients.producer.ProducerRecord;\n" +
                    "import org.springframework.beans.factory.annotation.Autowired;\n" +
                    "import org.springframework.kafka.annotation.KafkaListener;\n" +
                    "import org.springframework.kafka.core.KafkaTemplate;\n" +
                    "import org.springframework.messaging.handler.annotation.Payload;\n" +
                    "import org.springframework.stereotype.Service;\n" +
                    "\n" +
                    "import java.io.IOException;\n" +
                    "import java.util.Optional;\n" +
                    "\n" +
                    "@Service\n" +
                    "public class {{ upName }}EventListener {\n" +
                    "\n" +
                    "    @Autowired\n" +
                    "    private KafkaTemplate kafkaTemplate;\n" +
                    "\n" +
                    "    @Autowired\n" +
                    "    private {{ upName }}Repository {{ upName }}Repository;\n" +
                    "\n" +
                    "{{#innerAggregate}}" +
                    "{{#policy}}" +
                    "{{{code}}}" +
                    "{{/policy}}" +
                    "{{/innerAggregate}}" +
                    "}", me.value)
            },
            setAggregateTemplate() {
                var me = this
                return Mustache.render(
                    "package com.example.template;\n" +
                    "\n" +
                    "import com.fasterxml.jackson.core.JsonProcessingException;\n" +
                    "import com.fasterxml.jackson.databind.ObjectMapper;\n" +
                    "import org.apache.kafka.clients.producer.ProducerRecord;\n" +
                    "import org.springframework.core.env.Environment;\n" +
                    "import org.springframework.kafka.core.KafkaTemplate;\n" +
                    "import org.springframework.web.client.RestTemplate;\n" +
                    "import org.springframework.beans.BeanUtils;\n" +
                    "\n" +
                    "import javax.persistence.*;\n" +
                    "\n" +
                    "@Entity\n" +
                    "@Table(name = \"{{inputText}}_table\")\n" +
                    "public class {{upName}} {\n" +
                    "\n" +
                    "{{#aggregateEntity}}\n" +
                        "{{#id}}"+
                    "    @Id\n" +
                    "    @GeneratedValue\n" +
                    "    private {{type}} {{name}};\n\n" +
                    "{{/id}}"+
                    "{{^id}}"+
                    "    public {{type}} {{name}};\n\n" +
                    "{{/id}}"+
                    "{{/aggregateEntity}}\n" +

                    "{{#aggregateEntity}} \n" +
                    "    public {{type}} get{{upName}}() { \n" +
                    "        return {{name}};\n" +
                    "    }\n" +
                    "\n" +
                    "    public void set{{upName}}({{type}} {{name}}) { \n" +
                    "        this.{{name}} = {{name}};\n" +
                    "    }\n" +
                    "{{/aggregateEntity}}\n" +
                    "\n" +
                    "{{#innerAggregate}}" +
                    "{{#event}}" +
                    "{{{publishTypeCode}}}" +
                    "{{/event}}" +
                    "{{/innerAggregate}}" +
                    "\n" +
                    "}", me.value)
            }
        },

    }
</script>


<style scoped lang="scss" rel="stylesheet/scss">

</style>
