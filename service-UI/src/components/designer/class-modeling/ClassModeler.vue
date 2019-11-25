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
<<<<<<< HEAD
                        'component': 'uml-class-definition',
                        'label': 'Class',
                        'width': '100',
                        'height': '100',
                        'src': `${ window.location.protocol + "//" + window.location.host}/static/image/event/event.png`
                    },
                    {
                        'icon': 'bpmn-icon-start-event-none',//'OG.shape.essencia.Alpha',
                        'component': 'uml-class-definition',
                        'label': 'relation',
                        'width': '100',
                        'height': '100',
                        'src': `${ window.location.protocol + "//" + window.location.host}/static/image/event/event.png`
=======
                        'component': 'class-definition',
                        'label': 'Class',
                        'width': '100',
                        'height': '100',
                        'src': 'https://raw.githubusercontent.com/kimsanghoon1/k8s-UI/master/public/static/image/event/event.png'
                    },
                    {
                        'icon': 'bpmn-icon-start-event-none',//'OG.shape.essencia.Alpha',
                        'component': 'class-definition',
                        'label': 'relation',
                        'width': '100',
                        'height': '100'
>>>>>>> 797c7f9cdf5590053c70e9ea705e6ebb6c5801b8
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


