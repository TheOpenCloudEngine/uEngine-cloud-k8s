<template>

</template>

<script>
    export default {
        name: 'modeling-element-base',
        props: {
            value: Object,
            definition: Object,
        },
        created: function () {

        },
        data: function () {
            return {
                _id: null,
            }
        },
        computed: {
            type() {
                return ''
            },
            style: {
                get: function () {
                    var style;
                    //스타일이 없다면 디폴트 스타일을 사용한다.
                    if (this.value) {
                        if (this.value.elementView)
                            style = this.value.elementView.style;
                        else
                            style = this.value.relationView.style;
                    }

                    if (style) {
                        var jsonStyle = JSON.parse(style);
                        if ($.isEmptyObject(jsonStyle)) {
                            return this.defaultStyle;
                        } else {
                            return jsonStyle;
                        }
                    } else {
                        return this.defaultStyle;
                    }
                },
                set: function (val) {
                    if (this.value) {

                        if (this.value.elementView)
                            this.value.elementView.style = JSON.stringify(val);
                        else
                            this.value.relationView.style = JSON.stringify(val);
                    }
                }
            }
        },
        watch: {},
        mounted: function () {
        },
        methods: {
            selectedActivity: function () {
                if(this.value) {
                    this.value.selected = true
                }
                // this._selected = true;
            },
            deSelectedActivity: function () {
                console.log('UnSelected')
                if(this.value) {
                    this.value.selected = false
                    if(this.value.drawer) {
                        this.value.drawer = false
                    }
                }
            },
            showProperty: function () {
                console.log('Property' + this.value.drawer)
                this.value.drawer = true;
            },
            uuid: function () {
                function s4() {
                    return Math.floor((1 + Math.random()) * 0x10000)
                        .toString(16)
                        .substring(1);
                }

                return s4() + s4() + '-' + s4() + '-' + s4() + '-' +
                    s4() + '-' + s4() + s4() + s4();
            },
            onAddedToGroup: function (groupOpengraphComponent, opengraphComponent, eventOffset) {
                console.log('onAddedToGroup!!');
                var me = this;

                console.log("groupOpengraphComponent: " , groupOpengraphComponent)


                if(groupOpengraphComponent.tagName) {
                    console.log('ROOT')
                } else {
                    var designer = this.getComponent('modeling-designer')
                    designer.value.some(function (definitionTmp, definitionIndex) {
                        var copyTmp = JSON.parse(JSON.stringify(definitionTmp))
                        if(definitionTmp.elementView.id == opengraphComponent.element.id) {
                            designer.value.some(function (boundedTmp, boundedIndex) {
                                if(boundedTmp.elementView.id == groupOpengraphComponent.element.id) {
                                    designer.value[boundedIndex].dataList.push(copyTmp.elementView.id)

                                    designer.value = designer.value.filter(n => n)
                                    return;
                                }
                            })
                        }
                    })
                }

            },
            getComponent(componentName) {
                let component = null
                let parent = this.$parent
                while (parent && !component) {
                    if (parent.$options.name === componentName) {
                        component = parent
                    }
                    parent = parent.$parent
                }
                return component
            },
            onRemoveShape: function () {
                console.log('remove')
                var parent = this.$parent;

                while (parent.$options._componentTag != "modeling-designer") {

                    parent = parent.$parent;
                }

                var elementIndex = -1;
                try {
                    elementIndex = parent.value[parent.relationListBeanPath][1].indexOf(this.value);
                } catch (e) {
                }

                if (elementIndex > -1) {
                    parent.value[parent.relationListBeanPath][1].splice(elementIndex, 1);
                } else {
                    try {
                        elementIndex = parent.value[parent.elementListBeanPath][1].indexOf(this.value);
                    } catch (e) {
                    }

                    if (elementIndex > -1) {
                        parent.value[parent.elementListBeanPath][1].splice(elementIndex, 1);
                    }

                }

            }
        }
    }
</script>


<style scoped lang="scss" rel="stylesheet/scss">

</style>
