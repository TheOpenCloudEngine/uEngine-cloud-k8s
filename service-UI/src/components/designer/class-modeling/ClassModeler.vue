<template xmlns:v-on="http://www.w3.org/1999/xhtml">

    <modeling-designer
            :elementTypes="elementTypes"
            elementListBeanPath="classDefinitions"
            relationListBeanPath="relations"
            relationVueComponentName="class-relation"
            ref="designer"
    >
    </modeling-designer>


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
                        'component': 'domain-event-definition',
                        'label': 'Event',
                        'width': '100',
                        'height': '100',
                        'src': 'https://raw.githubusercontent.com/kimsanghoon1/k8s-UI/master/public/static/image/event/event.png'
                    },
                    {
                        'icon': 'bpmn-icon-start-event-none',//'OG.shape.essencia.Alpha',
                        'component': 'policy-definition',
                        'label': 'Policy',
                        'width': '100',
                        'height': '100',
                        'src': 'https://raw.githubusercontent.com/kimsanghoon1/k8s-UI/master/public/static/image/event/policy.png'
                    },
                    {
                        'icon': 'bpmn-icon-start-event-none',//'OG.shape.essencia.Alpha',
                        'component': 'command-definition',
                        'label': 'Command',
                        'width': '100',
                        'height': '100',
                        'src': 'https://raw.githubusercontent.com/kimsanghoon1/k8s-UI/master/public/static/image/event/command.png'

                    },
                    {
                        'icon': 'bpmn-icon-start-event-none',//'OG.shape.essencia.Alpha',
                        'component': 'aggregate-definition',
                        'label': 'Aggregate',
                        'width': '100',
                        'height': '100',
                        'src': 'https://raw.githubusercontent.com/kimsanghoon1/k8s-UI/master/public/static/image/event/aggregate.png'

                    },
                    {
                        'icon': 'bpmn-icon-start-event-none',//'OG.shape.essencia.Alpha',
                        'component': 'external-definition',
                        'label': 'External',
                        'width': '100',
                        'height': '100',
                        'src': 'https://raw.githubusercontent.com/kimsanghoon1/k8s-UI/master/public/static/image/event/external.png'

                    },
                    {
                        'icon': 'bpmn-icon-start-event-none',//'OG.shape.essencia.Alpha',
                        'component': 'view-definition',
                        'label': 'View',
                        'width': '100',
                        'height': '100',
                        'src': 'https://raw.githubusercontent.com/kimsanghoon1/k8s-UI/master/public/static/image/event/view.png'

                    },
                    {
                        'icon': 'bpmn-icon-start-event-none',//'OG.shape.essencia.Alpha',
                        'component': 'bounded-context-definition',
                        'label': 'Bounded Context',
                        'width': '100',
                        'height': '100',
                        'src': 'https://raw.githubusercontent.com/kimsanghoon1/k8s-UI/master/public/static/image/event/bounded2.png'

                    },
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


