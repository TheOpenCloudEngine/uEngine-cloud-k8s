<template xmlns:v-on="http://www.w3.org/1999/xhtml">
    <uml-designer
            :elementTypes="elementTypes"
            elementListBeanPath="classDefinitions"
            relationListBeanPath="relations"
            relationVueComponentName="class-relation"
            ref="designer"
    >
    </uml-designer>
</template>

<script>
    export default {
        name: 'class-designer',
        props: {

        },
        data() {
            return {
                value: {},
                elementTypes: [
                    {
                        'icon': 'bpmn-icon-start-event-none',//'OG.shape.essencia.Alpha',
                        'component': 'class-definition',
                        'label': 'Class',
                        'width': '100',
                        'height': '100'
                    }
                ],
            }
        },

        created: function () {

        },
        mounted: function () {

        },
        methods: {

        }
    }
</script>


