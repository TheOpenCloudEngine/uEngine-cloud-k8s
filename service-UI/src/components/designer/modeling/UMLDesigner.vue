<template xmlns:v-on="http://www.w3.org/1999/xhtml">
    <div class="canvas-panel">
        <v-layout right>
            <opengraph ref="opengraph" focus-canvas-on-select wheelScalable :labelEditable="true"
                       :dragPageMovable="dragPageMovable" :enableContextmenu="false" :enableRootContextmenu="false"
                       :enableHotkeyCtrlC="false" :enableHotkeyCtrlV="false"
                       :enableHotkeyDelete="false" :enableHotkeyCtrlZ="false" :enableHotkeyCtrlD="false"
                       :enableHotkeyCtrlG="false" :slider="true" :movable="true" :resizable="true" :selectable="true"
                       :connectable="true" v-if="value" v-on:canvasReady="bindEvents" :autoSliderUpdate="true"
                       v-on:connectShape="onConnectShape" :imageBase="imageBase">
                <!--엘리먼트-->
                <div v-for="(element, index) in value.definition">
                    <component :is="getComponentByClassName(element._type)"
                               v-model="value.definition[index]"></component>
                </div>
                <div v-for="(element, index) in value.relation">
                    <component :is="getComponentByClassName(element._type)" v-model="value.relation[index]"></component>
                </div>
            </opengraph>

            <v-layout right>
                <v-divider></v-divider>
                <text-reader :importType="'json'" @load="value = $event" style="display: inline-block;"
                             :fileName.sync="projectName"></text-reader>
                <v-btn color="info" v-on:click.native="download"
                       style="margin-top: 16px; margin-left: 5px; margin-right: 10px;">SAVE
                </v-btn>
            </v-layout>


            <v-card class="tools" style="top:100px; text-align: center;">

                <span class="bpmn-icon-hand-tool" v-bind:class="{ icons : !dragPageMovable, hands : dragPageMovable }"
                      _width="30"
                      _height="30" v-on:click="toggleGrip">
                     <v-tooltip md-direction="right">Hands</v-tooltip>
                </span>
                <v-tooltip right v-for="(item, key) in elementTypes" :key="key">
                    <template v-slot:activator="{ on }">
                        <span
                                class="icons draggable"
                                align="center"
                                :_component="item.component"
                                :_width="item.width"
                                :_height="item.height">
                        <img height="30px" width="30px" :src="item.src" v-on="on">
                        </span>
                    </template>
                    <span>{{item.label}}</span>
                </v-tooltip>

            </v-card>
        </v-layout>
        <modeler-image-generator ref="modeler-image-generator"></modeler-image-generator>
    </div>
</template>

<script>
    import TextReader from "@/components/yaml.vue";
    import CodeViewer from "../CodeViewer";
    import {
        v4
    } from 'uuid';
    import Pusher from 'pusher-js';

    var FileSaver = require('file-saver');
    import {
        saveAs
    } from 'file-saver';

    var JSZip = require('jszip')

    export default {
        name: 'uml-designer',
        components: {
            TextReader,
            saveAs,
            Pusher,
            CodeViewer
        },
        props: {
            elementTypes: Array,
        },
        data() {
            return {
                files: {
                    md: 'mdi-markdown',
                    txt: 'mdi-file-document-outline',
                    java: 'mdi-language-java',
                    xml: 'mdi-xml',
                    shell: 'mdi-powershell',
                    docker: 'mdi-docker',

                },
                code: '',
                active: [],
                canvas: null,
                dragPageMovable: false,
                relationVueComponentName: 'modeling-relation',
                value: {
                    'definition': [],
                    'relation': []
                },
                items: [],
                enableHistoryAdd: false,
                undoing: false,
                undoed: false,
                undoIndex: 0,
                tmpValue: [],
                projectName: '',
                noPushUndo: false,
                redoArray: [],
                undoArray: [],
                imageBase: 'https://raw.githubusercontent.com/kimsanghoon1/k8s-UI/master/public/static/image/symbol/',
                userId: '',
                snackbar: false,
                color: 'error',
                mode: 'multi-line',
                timeout: 6000,
                text: '수정중입니다.',
                pusher: {},
                connectCount: 0,
                connectInfo: [],
                show: false,
                channel: {},
                members: [],
                valueTmp: {},
                pathTmp: [],
                maxWidth: 0,
                maxHeight: 0,

            }
        },
        beforeDestroy: function () {
            // console.log("aa")
            this.canvas.removeSlider()
            // this.channel.pusher.unsubscribe('presence-event');
        },
        computed: {
            definitionSet() {
                return this.inputValue(this.active)
            },
            drawer: {
                get: function () {
                    var me = this
                    var temp = false;
                    var tmpArray = JSON.parse(JSON.stringify(me.value.definition))
                    if (tmpArray.length > 0) {
                        tmpArray.some(function (tmp, index) {
                            if (tmp.drawer) {
                                temp = true
                                return;
                            }
                        })
                    }
                    return temp;
                }
            },
            id: {
                get: function () {
                    return this.projectName
                }
            },
        },
        created: function () {

        },
        mounted() {
            var me = this
            this.userId = v4();

            me.$ModelingBus.$on('MoveEvent', function () {
                me.$nextTick(function () {
                    me.undoArray.push(JSON.parse(JSON.stringify(me.value)));
                    me.redoArray = [];
                    me.value.definition.forEach(function (tmp) {
                        if (tmp.selected == true) {
                        }
                    })
                })
            });

            // me.$ModelingBus.$on('umlDiagram', function () {
            //     me.umlModalShow()
            //
            //     me.$nextTick(function () {
            //     })
            // });

            this.userId = v4();
            me.pusher = new Pusher('33169ca8c59c1f7f97cd', {
                cluster: 'ap3',
            });

            const channel = me.pusher.subscribe('paint');
            // channel.bind('draw', function(data) {
            //     console.log(data)
            //     me.value = data.newVal
            // });
            channel.bind('draw', (data) => {
                const {userId: id, newVal} = data;
                if (me.userId !== id) {
                    let used = false;
                    if (newVal.name != 'class-relation') {
                        me.value['definition'].some(function (tmp, index) {
                            if (tmp.elementView.id == newVal.elementView.id) {
                                me.value['definition'] = [
                                    ...me.value['definition'].slice(0, index),
                                    newVal,
                                    ...me.value['definition'].slice(index)
                                ]
                                used = true;
                                return;
                            }
                        })
                        if (used == false) {
                            me.value.definition.push(newVal)
                        }
                        // me.value.definition.push(newVal)
                    } else {
                        me.value['relation'].some(function (tmp, index) {
                            // console.log(tmp, index)
                            if (tmp._type != 'org.uengine.uml.model.bounded') {
                                me.value['relation'] = [
                                    ...me.value['relation'].slice(0, index),
                                    element,
                                    ...me.value['relation'].slice(index)
                                ]
                                return;
                            }
                            if (me.value['relation'].length - 1 == index) {
                                me.value['relation'].push(element);
                            }
                        })
                    }

                }
            });

            this.$nextTick(function () {
                let startTime = new Date().getTime()

                // var count = me.channel.members;

                //$nextTick delays the callback function until Vue has updated the DOM
                // (which usually happens as a result of us changing the data
                //  so make any DOM changes here
                // this.canvas.addSlider({
                //     slider: $("#canvas_slider"),
                //     width: 200,
                //     height: 300,
                //     appendTo: "body"
                // });

                this.canvas._CONFIG.FAST_LOADING = false;

                // this.canvas.updateSlider();
                //timer end
                me.undoArray.push({
                    'definition': [],
                    'relation': []
                })
                this.$refs.opengraph.printTimer(startTime, new Date().getTime());

                $(document).keydown((evt) => {
                    if (evt.keyCode == 67 && (evt.metaKey || evt.ctrlKey)) {
                        this.copy();
                    } else if (evt.keyCode == 86 && (evt.ctrlKey || evt.metaKey)) {
                        this.paste();
                    } else if (evt.keyCode == 46 || evt.keyCode == 8) {
                        this.deleteActivity();
                    } else if (evt.keyCode == 90 && (evt.metaKey || evt.ctrlKey)) {
                        if (evt.shiftKey) {
                            me.redo()
                        } else {
                            me.undo();
                        }
                    }
                });
            });

            // $(window).resize(function (va) {
            //     console.log("Height",va.target.innerHeight)
            //     console.log("WIDTH",va.target.innerWidth)
            // })
        },
        watch: {
            open(newVal) {
                // console.log(newVal)

            }
        },

        methods: {
            inputValue(name) {
                // console.log(name)
                var test = [
                    name
                ]
                return test
            },
            codeModalShow() {
                this.$modal.show('code-modal');
            },
            umlModalShow() {
                console.log("aa")
                this.$modal.show('uml-modal');
            },
            generateZip() {
                var me = this
                me.codeList.forEach(function (list) {
                    if (!list.file) {
                        //Array
                        // console.log(list.children)
                        // console.log(list.name)
                        me.reverse(list.children, list.name)
                    } else {
                        me.pathTmp.push({path: list.name, code: list.code})
                        // var blob = new Blob(["Hello, world!"], {type: "text/plain;charset=utf-8"});
                        // FileSaver.saveAs(blob, list.name);
                        console.log(list.name)
                    }
                })

                console.log(me.pathTmp)
                var zip = new JSZip();

                var parents = [];


                me.pathTmp.forEach(function (generateData) {
                    // if(typeof generateData == String) {
                    //     // zip.file(generateData.path,)
                    // }
                    if (generateData.path.includes('/')) {
                        parents.push(generateData.path.split('/')[0])
                    }
                    zip.file(generateData.path, generateData.code)
                })
                // zip.file("package.json", "...");
                // zip.file("lib/index.js", "...");
                // zip.file("test/index.html", "...");
                // zip.file("test/asserts/file.js", "...");
                // zip.file("test/asserts/generate.js", "...");
                parents.forEach(function (prefix) {
                    zip.folder(prefix).forEach(function (relativePath, file) {
                        console.log("iterating over", relativePath);
                        console.log(file)
                    });
                })

                zip.generateAsync({type: "blob"})
                    .then(function (content) {
                        // Force down of the Zip file
                        saveAs(content, "archive.zip");
                    });

            },
            reverse(item, path) {
                var me = this
                item.forEach(function (list) {
                    if (list.children) {
                        //폴더 생성하기
                        console.log(list.name)
                        var tmpPath = path + '/' + list.name
                        console.log(tmpPath)

                        me.reverse(list.children, tmpPath);
                    } else {
                        //파일생성하
                        console.log(list.name)
                        if (list.code) {
                            me.pathTmp.push({path: path + '/' + list.name, code: list.code})
                            console.log(me.pathTmp)
                        }
                        // else {
                        //     me.pathTmp.push(path+'/'+list.name+'/')
                        // }
                    }
                })
            },
            // makeFiles(List){
            //
            //
            // },
            codeModalhide() {
                this.$modal.hide('code-modal');
            },
            unselectedAll: function () {
                this.value.definition.forEach(function (definition) {
                    console.log(definition)
                    definition.selected = false
                })
                this.value.relation.forEach(function (relation) {
                    relation.selected = false
                })
            },
            ajax: function (url, method, payload, successCallback) {
                var xhr = new XMLHttpRequest();
                xhr.open(method, url, true);
                // xhr.withCredentials = true;
                xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
                xhr.onreadystatechange = function () {
                    if (xhr.readyState != 4 || xhr.status != 200) return;
                    successCallback(xhr.responseText);
                };
                xhr.send(JSON.stringify(payload));
            },
            connectshow: function () {
                var me = this
                if (me.show == true) {
                    me.show = false
                } else {
                    me.show = true
                }
            },
            restApiPush: function () {
                var me = this;
                me.$http.post(`http://localhost:8080/event/${me.projectName}`, me.value, {
                        responseType: "arraybuffer",
                        headers: {
                            'Content-Type': 'application/zip;'
                        }
                    }
                ).then(function (response) {
                    console.log("Trying saving zip ...");
                    console.log(response.data.length);
                    var blob = new Blob([response.data], {type: 'application/zip'});
                    console.log(blob.size);
                    var fileName = me.projectName + ".zip";
                    saveAs(blob, fileName);
                    console.log("saveBlob succeeded");
                })
            },
            //멀티
            syncOthers(elements) {
                var me = this
                let userId = this.userId
                let newVal = elements

                const body = {
                    newVal,
                    userId,
                };
                fetch('http://localhost:4000/paint', {
                    method: 'post',
                    body: JSON.stringify(body),
                    headers: {
                        'content-type': 'application/json',
                    },
                }).then(() => console.log("throw"));
            },
            //복사
            copy: function () {
                var me = this
                if (!me.drawer) {
                    me.tempValue = []
                    me.value.definition.forEach(function (tmp, idx) {
                        if (tmp.selected == true) {
                            me.tempValue.push(tmp)
                        }
                    })
                    me.value.relation.forEach(function (tmp, idx) {
                        if (tmp.selected == true) {
                            me.tempValue.push(tmp)
                        }
                    })
                    // this.syncOthers(tmp);
                }
            },
            b64toBlob: function (b64Data, contentType, sliceSize) {
                contentType = contentType || '';
                sliceSize = sliceSize || 512;

                var byteCharacters = atob(b64Data);
                var byteArrays = [];

                for (var offset = 0; offset < byteCharacters.length; offset += sliceSize) {
                    var slice = byteCharacters.slice(offset, offset + sliceSize);

                    var byteNumbers = new Array(slice.length);
                    for (var i = 0; i < slice.length; i++) {
                        byteNumbers[i] = slice.charCodeAt(i);
                    }

                    var byteArray = new Uint8Array(byteNumbers);

                    byteArrays.push(byteArray);
                }

                var blob = new Blob(byteArrays, {type: contentType});
                return blob;
            },
            //붙여넣기
            paste: function () {
                var me = this
                if (!me.drawer) {
                    var temp = JSON.parse(JSON.stringify(me.tempValue))

                    if (me.tempValue != null) {
                        temp.forEach(function (tmp, idx) {
                            tmp.elementView.id = me.uuid();
                            tmp.elementView.x = tmp.elementView.x + 10
                            tmp.elementView.y = tmp.elementView.y + 10
                            me.value.definition.push(tmp);
                            me.redoArray.push(tmp);
                        })
                        // this.syncOthers(tmp);
                        //초기화
                    } else {
                    }
                }
            },
            download: function () {
                var me = this;
                var text = JSON.stringify(me.value);

                var filename = this.projectName + '.json';

                var file = new File([text], filename, {
                    type: "text/json;charset=utf-8"
                });
                FileSaver.saveAs(file);
            },
            deleteBoundary(definitionArray, deleteItem) {

                //해당 바운더리 찾기
                definitionArray.forEach(function (definitionTmp, index) {
                    if (deleteItem.boundedContext == definitionTmp.inputText && definitionTmp._type == 'org.uengine.uml.model.bounded') {
                        console.log(deleteItem.boundedContext, definitionTmp.inputText)

                        definitionTmp.dataList.forEach(function (item, idx) {
                            if (item.inputText == deleteItem.inputText && item._type == deleteItem._type) {
                                console.log(definitionTmp.dataList[idx])
                                definitionTmp.dataList[idx] = null;

                                definitionTmp.dataList = definitionTmp.dataList.filter(n => n)
                            }
                        })

                    }
                })

            },
            deleteActivity: function () {
                var me = this
                if (!me.drawer) {
                    let selected = []

                    let definitionArray = JSON.parse(JSON.stringify(me.value.definition));
                    let relationArray = JSON.parse(JSON.stringify(me.value.relation));

                    definitionArray.forEach(function (definitionTmp, index) {
                        if (definitionTmp.selected) {
                            if (definitionTmp.boundedContext) {
                                me.deleteBoundary(definitionArray, definitionTmp);

                            }
                            selected.push(definitionTmp.elementView.id)
                            definitionArray[index] = null
                        }
                    })

                    definitionArray = definitionArray.filter(n => n)
                    selected.forEach(function (selectedTmp) {
                        relationArray.forEach(function (relation, index) {
                            if (relation.to == selectedTmp || relation.from == selectedTmp) {
                                relationArray[index] = null
                            }
                        })
                    })

                    relationArray = relationArray.filter(n => n)
                    relationArray.forEach(function (relationTmp, index) {
                        if (relationTmp.selected) {
                            relationArray[index] = null
                        }
                    })
                    relationArray = relationArray.filter(n => n)

                    me.value.definition = definitionArray
                    me.value.relation = relationArray
                    // this.syncOthers();
                }
            },
            toggleGrip: function () {
                this.dragPageMovable = !this.dragPageMovable;

                if (this.dragPageMovable) {
                    this.cursorStyle = 'cursor: url("/static/image/symbol/hands.png"), auto;';
                    this.handsStyle = ' color: #ffc124;';
                } else {
                    this.cursorStyle = null;
                    this.handsStyle = null;
                }
            },
            bindEvents: function (opengraph) {
                var me = this;
                var el = me.$el;
                var canvasEl = $(opengraph.container);
                if (!canvasEl || !canvasEl.length) {
                    return;
                }
                this.canvas = opengraph.canvas;
                //아이콘 드래그 드랍 이벤트 등록
                $(el).find('.draggable').draggable({
                    start: function () {
                        canvasEl.data('DRAG_SHAPE', {
                            'component': $(this).attr('_component'),
                            'width': $(this).attr('_width'),
                            'height': $(this).attr('_height')
                        });
                    },
                    helper: 'clone',
                    appendTo: canvasEl
                });

                canvasEl.droppable({
                    drop: function (event, ui) {
                        var componentInfo = canvasEl.data('DRAG_SHAPE'),
                            shape, element;
                        if (componentInfo) {
                            var dropX = event.pageX - canvasEl.offset().left + canvasEl[0].scrollLeft;
                            var dropY = event.pageY - canvasEl.offset().top + canvasEl[0].scrollTop;

                            dropX = dropX / opengraph.scale;
                            dropY = dropY / opengraph.scale;

                            componentInfo = {
                                component: componentInfo.component,
                                x: dropX,
                                y: dropY,
                                width: parseInt(componentInfo.width, 10),
                                height: parseInt(componentInfo.height, 10),
                                label: ''
                            }
                            me.addElement(componentInfo);
                        }
                        canvasEl.removeData('DRAG_SHAPE');
                    }
                });
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
            onConnectShape: function (edge, from, to) {
                var me = this;
                // console.log(edge)
                //존재하는 릴레이션인 경우 (뷰 컴포넌트), 데이터 매핑에 의해 자동으로 from, to 가 변경되어있기 때문에 따로 로직은 필요없음.
                //=> 바뀌어야 함.
                //신규 릴레이션인 경우에는 릴레이션 생성
                var edgeElement, originalData;
                var isComponent = false;
                if (edge.shape) {
                    edgeElement = edge;
                } else {
                    isComponent = true;
                    edgeElement = edge.element;
                }

                if (edgeElement && from && to) {
                    var vertices = '[' + edgeElement.shape.geom.vertices.toString() + ']';
                    var componentInfo = {
                        component: 'class-relation',
                        sourceElement: from.$parent,
                        targetElement: to.$parent,
                        vertices: vertices,
                        isFilled: true,
                        relationView: {
                            style: JSON.stringify({}),
                            value: vertices,
                        }
                    }

                    from.$parent.value.elementView.id = from.id;
                    to.$parent.value.elementView.id = to.id;

                    if (isComponent) {
                        me.canvas.removeShape(edgeElement, true);
                        //this.removeComponentByOpenGraphComponentId(edgeElement.id);
                        //기존 컴포넌트가 있는 경우 originalData 와 함께 생성
                        this.addElement(componentInfo, null, JSON.parse(JSON.stringify(originalData)));
                    } else {
                        me.canvas.removeShape(edgeElement, true);
                        //기존 컴포넌트가 없는 경우 신규 생성
                        this.addElement(componentInfo);
                    }
                    // this.syncOthers();
                }
            },
            redo: function () {
                var me = this
                if (!me.drawer) {
                    if (me.redoArray.length > 0) {
                        var tmpData = me.redoArray.pop();
                        me.value = JSON.parse(JSON.stringify(tmpData));
                        if (me.undoArray.length == 0 && me.value.length == 0) {
                            me.undoArray.push({
                                'definition': [],
                                'relation': []
                            })
                        }
                        me.undoArray.push(JSON.parse(JSON.stringify(tmpData)));
                        // this.syncOthers(JSON.parse(JSON.stringify(tmpData)));
                    } else {
                    }
                }
            },
            undo: function () {
                var me = this;
                if (!me.drawer) {
                    if (me.undoArray.length > 1) {
                        me.redoArray.push(me.undoArray[me.undoArray.length - 1])
                        me.undoArray.pop()
                        me.value = JSON.parse(JSON.stringify(me.undoArray[me.undoArray.length - 1]))
                    } else if (me.undoArray.length == 1) {
                        me.undoArray.pop();
                        // console.log("undo length 0")
                        me.undoArray.push(JSON.parse(JSON.stringify(me.value)))
                        // this.syncOthers(JSON.parse(JSON.stringify(me.value)));
                    } else {
                    }
                }
            },
            addElement: function (componentInfo, newTracingTag, originalData) {
                this.enableHistoryAdd = true;
                var me = this;
                var additionalData = {};
                console.log(originalData)
                var vueComponent = me.getComponentByName(componentInfo.component);
                // console.log(componentInfo.component , this.relationVueComponentName)
                var element;

                // console.log(componentInfo)
                if (componentInfo.component == 'class-relation') {
                    element = vueComponent.computed.createNew(
                        componentInfo.sourceElement.value,
                        componentInfo.targetElement.value,
                        componentInfo.vertices,
                    );
                } else {
                    element = vueComponent.computed.createNew(
                        this.uuid(),
                        componentInfo.x,
                        componentInfo.y,
                        componentInfo.width,
                        componentInfo.height
                    );
                }
                // console.log(this.value, element.elementView.id)
                if (me.value == null) {
                    me.value = {
                        'definition': [],
                        'relation': []
                    }
                }
                if (element._type == 'org.uengine.uml.model.relation') {
                    me.value['relation'].push(element);
                } else {
                    me.value['definition'].push(element);
                }
                me.undoArray.push(JSON.parse(JSON.stringify(me.value)));
                me.redoArray = [];
                // this.syncOthers(element);
            },

            getComponentByName: function (name) {
                var componentByName;
                $.each(window.Vue._components, function (i, component) {
                    if (component.name == name) {
                        // console.log(component.default.name)
                        componentByName = component;
                    }
                });
                return componentByName;
            },
            getComponentByClassName: function (className) {
                var componentByClassName;

                $.each(window.Vue.classModelingComponents, function (i, component) {
                    if (component.default.computed && component.default.computed.className && component.default.computed.className() == className) {
                        componentByClassName = component.default;
                    }
                });
                return componentByClassName;
            }
        }
    }
</script>

<style scoped lang="scss" rel="stylesheet/scss">
    .canvas-panel {
        top: 0;
        bottom: 0;
        left: 0;
        right: 0;
        position: absolute;
        overflow: hidden;

        .fullcanvas {
            position: absolute;
            width: 100%;
            height: 100%;
            top: 10%;
            left: 0;
            overflow: hidden;
        }

        .fullcanvashands {
            position: absolute;
            width: 100%;
            height: 100%;
            top: 10%;
            left: 0;
            overflow: hidden;
            cursor: url('../../../../public/static/image/symbol/hands.png'), auto;
        }

        .tools {
            position: absolute;
            width: 48px;
            left: 20px;
            top: 20px;
            padding: 4px;
            overflow: hidden;

            .icons {
                margin-top: 5px;
                margin-bottom: 5px;
            }

            .hands {
                margin-top: 5px;
                margin-bottom: 5px;
            }
        }

        .zoom {
            position: absolute;
            width: 42px;
            right: 20px;
            bottom: 120px;

            .icons {
                font-size: 25px;
                margin-left: 10px;
                margin-top: 5px;
                margin-bottom: 5px;
            }

            .hands {
                font-size: 25px;
                margin-left: 10px;
                margin-top: 5px;
                margin-bottom: 5px;
            }
        }

        .icons {
            cursor: pointer;
            font-size: 30px;

            &:hover {
                color: #ffc124;
            }
        }

        .hands {
            cursor: pointer;
            font-size: 30px;
            color: #ffc124;
        }

        .export,
        .history,
        .import,
        .save {
            position: absolute;
            padding: 8px;

            .icons {
                font-size: 25px;
                margin-left: 10px;
            }
        }

        .import {
            left: 80px;
            bottom: 20px;
        }

        .export {
            left: 180px;
            bottom: 20px;
        }

        .history {
            left: 280px;
            bottom: 20px;
        }
    }

    /* The whole thing */
    .custom-menu {
        display: none;
        z-index: 1000;
        position: absolute;
        overflow: hidden;
        border: 1px solid #CCC;
        white-space: nowrap;
        font-family: sans-serif;
        background: #FFF;
        color: #333;
        border-radius: 5px;
        padding: 0;
    }

    /* Each of the items in the list */
    .custom-menu li {
        padding: 8px 12px;
        cursor: pointer;
        list-style-type: none;
        transition: all 0.3s ease;
        user-select: none;
    }

    .custom-menu li:hover {
        background-color: #DEF;
    }
</style>
