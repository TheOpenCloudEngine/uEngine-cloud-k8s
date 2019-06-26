<template>
    <div>
        <geometry-element
                selectable
                :movable="!value.editing"
                :resizable="!value.editing"
                connectable
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
                :label.sync="value.inputText + '\n\nConnect:: '+this.connectAggregateName"
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
                        :text="value.classReference ? value.classReference : '<< ' + value.name + ' >>'">
                </text-element>
            </sub-elements>
        </geometry-element>


        <modeling-property-panel
                :drawer.sync="value.drawer"
                :titleName.sync="value.name"
                :inputText.sync="value.inputText"
                :img="'https://raw.githubusercontent.com/kimsanghoon1/k8s-UI/master/public/static/image/event/view.png'"
                :aggregate.sync="value.aggregate"
                :aggregateList.sync="aggregateList"
                :aggregateText.sync="value.aggregateText"
                :connectAggregateName.sync="this.connectAggregateName"
                :restApi.sync="value.restAPI"
                v-model="value"
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
            type() {
                return 'View'
            },
            className() {
                return 'org.uengine.uml.model.View'
            },
            createNew(elementId, x, y, width, height) {
                return {
                    _type: this.className(),
                    name: 'View',
                    elementView: {
                        '_type': 'org.uengine.modeling.View',
                        'id': elementId,
                        'x': x,
                        'y': y,
                        'width': width,
                        'height': height,
                        'style': JSON.stringify({})
                    },
                    drawer: false,
                    selected: false,
                    inputText: '',
                    restApi: '',
                    editing: false
                }
            }
        },
        data: function () {
            return {
                itemH: 20,
                titleH: (this.value.classReference ? 60 : 30),
                reference: this.value.classReference != null,
                referenceClassName: this.value.classReference,
                aggregateList: []
            };
        },
        created: function () {

        },
        watch: {
          connectAggregateName: function (newVal) {
            if(newVal != null) {
              this.connectAggregateName = JSON.parse(JSON.stringify(newVal));

              //좌표이동으로  동기화
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
          connectAggregate:{
              handler: function (newVal,oldVal) {
                var tmp = this.value;


                if($.isEmptyObject(oldVal)){
                 //oldVal 없는 경우
                                    //newVal 에 추가
                                    if(!$.isEmptyObject(newVal)){
                                      console.log("NewVal Add");
                                      newVal.innerAggregate[tmp.name.toLowerCase()].push(tmp);
                                    }else{
                                      console.log("No exist Aggregate");
                                    }
                }else{
                  //oldVal 있는 경우

                    if(!$.isEmptyObject(newVal)){
                            // 다른 어그리게이트 인지 파악
                              if( newVal.elementView.id != oldVal.elementView.id)
                              {
                                      //이전 값 삭제
                                        oldVal.innerAggregate[tmp.name.toLowerCase()].forEach(function(element,idx){
                                          if(element.elementView.id == tmp.elementView.id){
                                            oldVal.innerAggregate[tmp.name.toLowerCase()][idx] = null;
                                            oldVal.innerAggregate[tmp.name.toLowerCase()] = oldVal.innerAggregate[tmp.name.toLowerCase()].filter(n => n)
                                          }
                                        })
                                      //새로운 값 추가
                                      console.log("oldVal Delete NewVal Add")
                                      newVal.innerAggregate[tmp.name.toLowerCase()].push(tmp);

                              }
                              else
                              {
                                    // 같은 어그리게이트
                                    var is = false
                                    newVal.innerAggregate[tmp.name.toLowerCase()].forEach(function(element){
                                      if(element.elementView.id == tmp.elementView.id){
                                        //값은 값 존재시
                                        is = true
                                      }
                                    })

                                    if(!is){
                                      console.log("notEqual Aggregate");
                                      newVal.innerAggregate[tmp.name.toLowerCase()].push(tmp);
                                    }
                              }
                            }
                }
              }
          }

        },
        mounted: function () {

        },
        methods: {}
    }
</script>


<style scoped lang="scss" rel="stylesheet/scss">

</style>
