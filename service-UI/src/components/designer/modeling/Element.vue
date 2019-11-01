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
                tmpHeight: 0,
                loopcheck: true,
            }
        },
        computed: {
            type() {
                return ''
            },
            code() {
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

        },
        watch: {
            'value.drawer': function (newValue, oldValue) {
                var designer = this.getComponent('modeling-designer')

                var me = this
                // designer.syncOthers(this.value);

                this.aggregateList = []
                if (newValue == true) {
                    designer.value.definition.forEach(function (temp) {
                        if (temp._type == "org.uengine.uml.model.Aggregate") {
                            console.log(temp)
                            me.aggregateList.push(temp.inputText);
                        }
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
                    }
                }
            },
            "value.elementView.height": {
                handler: function (newVal, oldVal) {
                    var me = this
                    if (me.rotateMove == true) {
                        me.tmpHeight = oldVal
                    }
                }
            },
        },
        mounted: function () {

        },
        methods: {
            difference: function (object, base) {
                function changes(object, base) {
                    return _.transform(object, function (result, value, key) {
                        if (!_.isEqual(value, base[key])) {
                            result[key] = (_.isObject(value) && _.isObject(base[key])) ? changes(value, base[key]) : value;
                        }
                    });
                }

                return changes(object, base);
            },
            onLabelChanged: function (be, af) {
                console.log("onLabelChanged", be, af);
            },
            onRotateShape: function (me, angle) {
                this.value.elementView.angle = angle
            },
            selectedActivity: function () {
                if (this.value) {
                    // var designer = this.getComponent('modeling-designer')
                    // designer.syncOthers(this.value);
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
                var designer = this.getComponent('modeling-designer')
                if (!this.value.editing) {
                    this.value.drawer = true;
                } else {
                    designer.snackbar = true
                }

            },
            onAddToGroup: function (groupElement, elements, eventOffset ) {

                elements.forEach(function (element) {
                    var inner = false
                    groupElement.$parent.value.dataList.some(function (tmp) {
                        if(tmp.elementView.id == element.$parent.value.elementView.id) {
                            return inner = true;
                        }
                    })

                    if(inner == false) {
                        groupElement.$parent.value.dataList.push(element.$parent.value)
                    }

                })
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
                //
                if(groupOpengraphComponent.tagName) {
                    // Canvas로 나가는 경우

                    designer.value.definition.forEach(function (tmp) {
                        if(tmp._type == 'org.uengine.uml.model.bounded' && tmp.inputText == opengraphComponent.$parent.value.boundedContext) {
                            tmp.dataList.some(function (boundedTmp, idx) {
                                if(boundedTmp.elementView.id == opengraphComponent.$parent.value.elementView.id) {
                                    tmp.dataList = [
                                            ...tmp.dataList.slice(0, idx),
                                            ...tmp.dataList.slice(idx +1)
                                    ]
                                }
                            })
                        }
                    })

                    opengraphComponent.$parent.value.boundedContext = ""

                } else {
                    // Bounded Context로 들어가는 경우
                    // 초기화 하고 새로운 값으로 변경
                    opengraphComponent.$parent.value.boundedContext = ""
                    opengraphComponent.$parent.value.boundedContext = groupOpengraphComponent.$parent.value.inputText

                }
                console.log(eventOffset)

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


<style scoped lang="scss" rel="stylesheet/scss"></style>
