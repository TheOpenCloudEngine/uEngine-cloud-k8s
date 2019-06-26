<template>

</template>

<script>

    export default {
        name: 'modeling-element-base',
        props: {
            value: Object,
            definition: Object,
        },
        created: function() {

        },
        data: function() {
            return {
                _id: null,
                rotateMove: false,
                tmpWidth: 0,
                tmpHeight: 0,
                connectAggregateName: '',
                loopcheck: true,
            }
        },
        computed: {
            type() {
                return ''
            },
            connectAggregate: {
                get: function() {
                    var me = this
                    if (this.value._type == 'org.uengine.uml.model.Command' || this.value._type == 'org.uengine.uml.model.View' || this.value._type == 'org.uengine.uml.model.Domain') {
                        // console.log(this.value);


                        var designer = this.getComponent('modeling-designer');
                        var select = {}; //
                        // var selectAggregate = {};
                        var shortdis = 8000;

                        designer.value.definition.forEach(function(element) {
                            if (element._type == 'org.uengine.uml.model.Aggregate') {
                                var newdisX = Math.abs(me.value.elementView.x - element.elementView.x);
                                var newdisY = Math.abs(me.value.elementView.y - element.elementView.y);
                                var newdis = Math.sqrt(Math.pow(newdisX, 2) + Math.pow(newdisY, 2))

                                if (newdis < shortdis) {
                                    shortdis = newdis;
                                    // select = JSON.parse(JSON.stringify(element));
                                    select = element;
                                    // selectAggregate = element;
                                }

                            }
                        })

                        // me.loopcheck=false;
                        // this.connectAggregateName = select.inputText
                        if (Object.keys(select).length != 0) {
                            return select
                        } else {
                            return {};
                        }


                    }


                }
            },
            style: {
                get: function() {
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
                set: function(val) {
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
            'value.drawer': function(newValue, oldValue) {
                var designer = this.getComponent('modeling-designer')

                var me = this
                designer.syncOthers(this.value);

                this.aggregateList = []
                if (newValue == true) {
                    designer.value.definition.forEach(function(temp) {
                        if (temp._type == "org.uengine.uml.model.Aggregate")
                            me.aggregateList.push(temp.inputText);
                    })
                }
            },
            "value.inputText": {
                handler: function(newVal, oldVal) {
                    var me = this
                    var designer = this.getComponent('modeling-designer')

                    me.rotateMove = true
                    me.value.elementView.x = me.value.elementView.x + 1
                    me.$nextTick(function() {
                        me.value.elementView.width = me.tmpWidth
                        me.value.elementView.height = me.tmpHeight
                        me.rotateMove = true
                        me.value.elementView.x = me.value.elementView.x - 1
                        me.$nextTick(function() {
                            me.value.elementView.width = me.tmpWidth
                            me.value.elementView.height = me.tmpHeight
                        })
                    })

                }
            },
            connectAggregate: {
                handler: function() {
                    var me = this
                    if (this.value._type == 'org.uengine.uml.model.Command' || this.value._type == 'org.uengine.uml.model.View' || this.value._type == 'org.uengine.uml.model.Domain') {

                        if(this.connectAggregate != null){
                            this.connectAggregate;
                            this.connectAggregateName = this.connectAggregate.inputText;
                        }
                        // me.loopcheck=true;
                    }
                },
                deep: true
            },

            "value.elementView.width": {
                handler: function(newVal, oldVal) {
                    var me = this
                    if (me.rotateMove == true) {
                        me.tmpWidth = oldVal
                    }
                }

            },
            "value.elementView.height": {
                handler: function(newVal, oldVal) {
                    var me = this
                    if (me.rotateMove == true) {
                        me.tmpHeight = oldVal
                    }
                }
            }
        },
        mounted: function() {

        },
        methods: {
            difference: function(object, base) {
                function changes(object, base) {
                    return _.transform(object, function(result, value, key) {
                        if (!_.isEqual(value, base[key])) {
                            result[key] = (_.isObject(value) && _.isObject(base[key])) ? changes(value, base[key]) : value;
                        }
                    });
                }
                return changes(object, base);
            },
            onLabelChanged: function(be, af) {
                console.log("onLabelChanged", be, af);
            },
            onRotateShape: function(me, angle) {
                this.value.elementView.angle = angle
            },
            selectedActivity: function() {
                if (this.value) {
                    // var designer = this.getComponent('modeling-designer')
                    // designer.syncOthers(this.value);
                    this.value.selected = true
                }
                // this._selected = true;
            },
            deSelectedActivity: function() {
                // console.log('UnSelected')
                if (this.value) {
                    this.value.selected = false
                    if (this.value.drawer) {
                        this.value.drawer = false
                    }
                }
            },
            showProperty: function() {
                var designer = this.getComponent('modeling-designer')
                if(!this.value.editing){
                    this.value.drawer = true;
                } else {
                    designer.snackbar = true
                }

            },
            uuid: function() {
                function s4() {
                    return Math.floor((1 + Math.random()) * 0x10000)
                        .toString(16)
                        .substring(1);
                }

                return s4() + s4() + '-' + s4() + '-' + s4() + '-' +
                    s4() + '-' + s4() + s4() + s4();
            },
            onAddedToGroup: function(groupOpengraphComponent, opengraphComponent, eventOffset) {
                // console.log('onAddedToGroup!!');
                var me = this;
                var designer = this.getComponent('modeling-designer')

                // console.log("groupOpengraphComponent: " , groupOpengraphComponent)

                if (groupOpengraphComponent.tagName) {
                    //바운더리 삭제
                    designer.value.definition.some(function(definitionTmp, definitionIndex) {
                        if (definitionTmp.name == 'Bounded Context') {

                            definitionTmp.dataList.some(function(deleteTmp, index) {
                                // console.log(deleteTmp.elementView.id)
                                // console.log(opengraphComponent)
                                if (deleteTmp.elementView.id == opengraphComponent.id) {

                                    definitionTmp.dataList[index] = null
                                    definitionTmp.dataList = definitionTmp.dataList.filter(n => n)
                                    return;
                                }

                            })
                        }
                    })
                } else {
                    //바운더리 추가
                    designer.value.definition.some(function(definitionTmp, definitionIndex) {

                        var copyTmp = definitionTmp;
                        if (definitionTmp.elementView) {
                            if (definitionTmp.elementView.id == opengraphComponent.id) {
                                designer.value.definition.some(function(boundedTmp, boundedIndex) {
                                    if (boundedTmp.elementView) {
                                        if (boundedTmp.elementView.id == groupOpengraphComponent.id) {

                                            if(designer.value.definition[boundedIndex].dataList.length > 0) {
                                                designer.value.definition[boundedIndex].dataList.some(function (innerTmp, innerIndex) {
                                                    if(innerTmp.elementView.id == copyTmp.elementView.id) {
                                                        return;
                                                    }
                                                    designer.value.definition[boundedIndex].dataList.push(copyTmp)
                                                    designer.value.definition = designer.value.definition.filter(n => n)
                                                    return;
                                                })
                                            }

                                            else {
                                                designer.value.definition[boundedIndex].dataList.push(copyTmp)
                                                designer.value.definition = designer.value.definition.filter(n => n)
                                                return;
                                            }
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
            onRemoveShape: function() {},

        }
    }
</script>


<style scoped lang="scss" rel="stylesheet/scss"></style>
