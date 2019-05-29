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
                rotateMove: false,
                tmpWidth: 0,
                tmpHeight: 0
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
            },
            closedAggregate() {
                console.log("a")
                if (this._type == 'org.uengine.uml.model.Command' && this._type == 'org.uengine.uml.model.View' && this._type == 'org.uengine.uml.model.Domain') {
                    console.log("b")
                    var dis = this.value.elementView.x + this.value.elementView.y

                    var designer = this.getComponent('modeling-designer')
                    let aggregateTmpList = []
                    var minDis = 4000;
                    var aggregate;
                    designer.value.definition.forEach(function (tmp) {
                        if (tmp._type == 'org.uengine.uml.model.Aggregate') {
                            aggregateTmpList.push(tmp)
                        }
                    })
                    aggregateTmpList.forEach(function (agTmp) {
                        if (Math.abs(dis - (agTmp.elementView.x + agTmp.elementView.y)) < minDis) {
                            minDis = Math.abs(dis - (agTmp.elementView.x + agTmp.elementView.y))
                            aggregate = agTmp
                        }
                    })
                    return aggregate

                } else {
                    return null
                }

            },
        },
        watch: {
            "value.aggregate": {
                handler: function (newVal) {
                    var me = this
                    var designer = this.getComponent('modeling-designer')
                    if (me.type == 'Domain' || me.type == 'Command' || me.type == 'View') {
                        designer.value.definition.forEach(function (tmp) {
                            if (tmp.name == 'Aggregate' && tmp.inputText == newVal) {
                                tmp.innerAggregate[me.type.toLowerCase()].push(me.value)
                            }
                        })
                    }
                }
            },
            'value.drawer': function (newValue, oldValue) {
                var designer = this.getComponent('modeling-designer')

                var me = this
                this.aggregateList = []
                if (newValue == true) {
                    designer.value.definition.forEach(function (temp) {
                        if (temp._type == "org.uengine.uml.model.Aggregate")
                            me.aggregateList.push(temp.inputText);
                    })
                }
            },
            "value.inputText": {
                handler: function (newVal, oldVal) {
                    var me = this
                    var designer = this.getComponent('modeling-designer')

                    me.rotateMove = true
                    me.value.elementView.x = me.value.elementView.x + 1
                    me.$nextTick(function () {
                        me.value.elementView.width = me.tmpWidth
                        me.value.elementView.height = me.tmpHeight
                        me.rotateMove = true
                        me.value.elementView.x = me.value.elementView.x - 1
                        me.$nextTick(function () {
                            me.value.elementView.width = me.tmpWidth
                            me.value.elementView.height = me.tmpHeight
                        })
                    })

                }
            },
            "value.elementView.width": {
                handler: function (newVal, oldVal) {
                    var me = this
                    if (me.rotateMove == true) {
                        me.tmpWidth = oldVal
                        // console.log(newVal, oldVal)

                    }
                }

            },
            "value.elementView.height": {
                handler: function (newVal, oldVal) {
                    var me = this
                    if (me.rotateMove == true) {
                        me.tmpHeight = oldVal
                        // console.log(newVal, oldVal)
                    }
                }
            }
        },
        mounted: function () {
        },
        methods: {
            onRotateShape: function (me, angle) {
                this.value.elementView.angle = angle
            },
            selectedActivity: function () {
                if (this.value) {
                    this.value.selected = true
                }
                // this._selected = true;
            },
            deSelectedActivity: function () {
                // console.log('UnSelected')
                if (this.value) {
                    this.value.selected = false
                    if (this.value.drawer) {
                        this.value.drawer = false
                    }
                }
            },
            showProperty: function () {
                // console.log('Property' + this.value.drawer)
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
                // console.log('onAddedToGroup!!');
                var me = this;
                var designer = this.getComponent('modeling-designer')

                // console.log("groupOpengraphComponent: " , groupOpengraphComponent)

                if (groupOpengraphComponent.tagName) {
                    designer.value.definition.some(function (definitionTmp, definitionIndex) {
                        if (definitionTmp.name == 'Bounded Context') {
                            // console.log('ROOT')
                            definitionTmp.dataList.some(function (deleteTmp, index) {
                                if (deleteTmp == opengraphComponent.element.id) {
                                    definitionTmp.dataList[index] = null
                                    definitionTmp.dataList = definitionTmp.dataList.filter(n => n)
                                    return;
                                }
                            })
                        }
                    })
                } else {
                    designer.value.definition.some(function (definitionTmp, definitionIndex) {
                        var copyTmp = JSON.parse(JSON.stringify(definitionTmp))
                        if (definitionTmp.elementView) {
                            if (definitionTmp.elementView.id == opengraphComponent.element.id) {
                                designer.value.definition.some(function (boundedTmp, boundedIndex) {
                                    if (boundedTmp.elementView) {
                                        if (boundedTmp.elementView.id == groupOpengraphComponent.element.id) {
                                            designer.value.definition[boundedIndex].dataList.push(copyTmp.elementView.id)
                                            designer.value.definition = designer.value.definition.filter(n => n)
                                            return;
                                        }
                                    }
                                })
                            }
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
            },

        }
    }
</script>


<style scoped lang="scss" rel="stylesheet/scss">

</style>
