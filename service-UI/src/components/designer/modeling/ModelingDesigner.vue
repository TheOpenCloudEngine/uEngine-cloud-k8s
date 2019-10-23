<template xmlns:v-on="http://www.w3.org/1999/xhtml">
    <div class="canvas-panel">
        <v-layout right>
            <modal name="uml-modal" :height='"80%"' :width="'80%'">
                <class-modeler></class-modeler>
            </modal>

            <modal name="code-modal" scrollable :height='"auto"' :width="'80%'">
                <v-card>
                    <v-card-title>
                        <span class="headline">Code View</span>
                        <v-btn text>
                            <v-icon middle>info</v-icon>
                        </v-btn>
                    </v-card-title>
                    <v-card-text>
                        <!-- 형태:
                        Item: [ {name: fileName, file:'fileType'}, {name: folderName, children: [{name: fileName, file:'fileType'}]} ] -->
                        <v-row
                                class="mb-6"
                                no-gutters
                        >
                            <v-col :lg="3"
                                   :md="6"
                                   :sm="2"
                                   style="margin-right: 15px; border-right: 1px solid black"
                            >
                                <v-treeview
                                        :items="codeList"
                                        :active.sync="active"
                                        activatable
                                        item-key="name"
                                        open-on-click
                                        return-object
                                        open-all
                                >
                                    <template v-slot:prepend="{ item, open }">
                                        <v-icon v-if="!item.file">
                                            {{ open ? 'mdi-folder-open' : 'mdi-folder' }}
                                        </v-icon>
                                        <v-icon v-else>
                                            {{ files[item.file] }}
                                        </v-icon>
                                    </template>
                                </v-treeview>
                            </v-col>
                            <v-col :lg="8"
                                   :md="8"
                                   :sm="9"
                            >
                                <!--<highlight-code lang="javascript">-->
                                <!--let str = 'Hello, World!';-->
                                <!--console.log(str);-->
                                <!--</highlight-code>-->

                                <code-viewer v-model="definitionSet"></code-viewer>
                            </v-col>
                        </v-row>
                    </v-card-text>
                </v-card>
            </modal>

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
                <!--<v-badge overlap>-->
                <!--<template v-slot:badge>-->
                <!--<span>{{ connectCount }}</span>-->
                <!--</template>-->

                <!--<v-avatar>-->
                <!--<v-layout justify-end row v-if="show">-->
                <!--<v-tooltip v-for="item in connectInfo" bottom>-->
                <!--<template v-slot:activator="{ on }">-->
                <!--<v-avatar v-on="on">-->
                <!--<img :src='item.img'>-->
                <!--</v-avatar>-->
                <!--</template>-->
                <!--<span>{{ item.name }}</span>-->
                <!--</v-tooltip>-->
                <!--</v-layout>-->
                <!--<v-btn-->
                <!--fab-->
                <!--small-->
                <!--@click="connectshow()">-->
                <!--</v-btn>-->
                <!--</v-avatar>-->
                <!--</v-badge>-->

                <!--<v-btn color="info" v-on:click.native="addNewMember">addNewMember-->
                <!--</v-btn>-->
                <!--                <v-btn color="info" v-on:click.native="restApiPush"-->
                <!--                       style="margin-top: 16px; margin-left: 5px; margin-right: 10px;">BUILD-->
                <!--                </v-btn>-->
                <v-btn color="info" v-on:click.native="codeModalShow"
                       style="margin-top: 16px; margin-left: 5px; margin-right: 10px;">Generate
                </v-btn>
                <v-btn color="info" v-on:click.native="generateZip"
                       style="margin-top: 16px; margin-left: 5px; margin-right: 10px;">Download Archive
                </v-btn>
                <v-flex xs12 sm6 style="display: inline-block; max-width:400px;">
                    <v-text-field label="Project Name" v-model="projectName" single-line
                                  @click="unselectedAll"></v-text-field>
                </v-flex>
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

        <v-snackbar v-model="snackbar" :color="color" :multi-line="mode === 'multi-line'" :timeout="timeout"
                    :vertical="mode === 'vertical'">
            {{ text }}
            <v-btn dark flat @click="snackbar = false">
                Close
            </v-btn>
        </v-snackbar>

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
        name: 'modeling-designer',
        components: {
            TextReader,
            saveAs,
            Pusher,
            CodeViewer
        },
        props: {
            elementTypes: Array
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
            codeList: {
                get: function () {
                    var me = this
                    let tmpList = JSON.parse(JSON.stringify(me.items));
                    // console.log(me.items)

                    me.value.definition.forEach(function (item) {
                        var event = {
                            name: '',
                            file: 'txt',
                            type: '',
                            code: ''
                        }
                        // console.log(item)
                        if (item._type == 'org.uengine.uml.model.bounded') {
                            // console.log(item)

                            var boundedItems = [
                                {
                                    name: '.mvn',
                                    children: [
                                        {
                                            name: 'wrapper',
                                            children: [
                                                {
                                                    name: 'maven-wrapper.properties',
                                                    file: 'txt',
                                                    code: "distributionUrl=https://repo1.maven.org/maven2/org/apache/maven/apache-maven/3.3.9/apache-maven-3.3.9-bin.zip"
                                                }
                                            ],
                                        },
                                    ],
                                },
                                {
                                    name: 'src',
                                    children: [
                                        {
                                            name: 'main',
                                            children: [
                                                {
                                                    name: 'resources',
                                                    children: [
                                                        {
                                                            name: 'application.yml',
                                                            file: 'txt',
                                                            code: "server:\n" +
                                                                "  port: 8080\n" +
                                                                "\n" +
                                                                "\n" +
                                                                "eventTopic: eventTopic\n" +
                                                                "\n" +
                                                                "---\n" +
                                                                "spring:\n" +
                                                                "  profiles: default\n" +
                                                                "  kafka:\n" +
                                                                "#    bootstrap-servers: http://35.200.47.242:31090\n" +
                                                                "    bootstrap-servers: localhost:9092\n" +
                                                                "    consumer:\n" +
                                                                "      enable-auto-commit: true\n" +
                                                                "  jpa:\n" +
                                                                "    properties:\n" +
                                                                "      hibernate:\n" +
                                                                "        show_sql: true\n" +
                                                                "        format_sql: true\n" +
                                                                "\n" +
                                                                "logging:\n" +
                                                                "  level:\n" +
                                                                "    org:\n" +
                                                                "      hibernate:\n" +
                                                                "        type: trace  \n" +
                                                                "server:\n" +
                                                                "  port: 8085\n" +
                                                                "---\n" +
                                                                "spring:\n" +
                                                                "  profiles: docker\n" +
                                                                "  kafka:\n" +
                                                                "    bootstrap-servers: my-kafka.kafka.svc.cluster.local:9092\n" +
                                                                "    consumer:\n" +
                                                                "      enable-auto-commit: true\n" +
                                                                "eventTopic: eventTopicDocker"
                                                        },
                                                    ]
                                                },
                                                {
                                                    name: 'java',
                                                    children: [
                                                        {
                                                            name: 'com',
                                                            children: [
                                                                {
                                                                    name: 'example',
                                                                    children: [
                                                                        {
                                                                            name: 'template',
                                                                            children: [
                                                                                {
                                                                                    name: 'config',
                                                                                },
                                                                                {
                                                                                    name: 'Application.java',
                                                                                    file: 'java',
                                                                                    code: 'package com.example.template;\n' +
                                                                                        '\n' +
                                                                                        'import org.springframework.beans.factory.annotation.Autowired;\n' +
                                                                                        'import org.springframework.boot.SpringApplication;\n' +
                                                                                        'import org.springframework.boot.autoconfigure.SpringBootApplication;\n' +
                                                                                        'import org.springframework.context.ApplicationContext;\n' +
                                                                                        'import org.springframework.context.annotation.Lazy;\n' +
                                                                                        '\n' +
                                                                                        'import javax.annotation.PostConstruct;\n' +
                                                                                        '\n' +
                                                                                        '@SpringBootApplication\n' +
                                                                                        'public class Application {\n' +
                                                                                        '\n' +
                                                                                        '    protected static ApplicationContext applicationContext;\n' +
                                                                                        '    public static void main(String[] args) {\n' +
                                                                                        '        applicationContext = SpringApplication.run(Application.class, args);\n' +
                                                                                        '\n' +
                                                                                        '        }\n' +
                                                                                        '  }\n'

                                                                                },
                                                                                {
                                                                                    name: 'AbstractEvent.java',
                                                                                    file: 'java',
                                                                                    code: 'package com.example.template;\n' +
                                                                                        '\n' +
                                                                                        'import org.springframework.stereotype.Service;\n' +
                                                                                        '\n' +
                                                                                        'public class AbstractEvent {\n' +
                                                                                        '\n' +
                                                                                        '    String eventType;\n' +
                                                                                        '    String timestamp;\n' +
                                                                                        '\n' +
                                                                                        '    public String getEventType() {\n' +
                                                                                        '        return eventType;\n' +
                                                                                        '    }\n' +
                                                                                        '\n' +
                                                                                        '    public void setEventType(String eventType) {\n' +
                                                                                        '        this.eventType = eventType;\n' +
                                                                                        '    }\n' +
                                                                                        '\n' +
                                                                                        '    public String getTimestamp() {\n' +
                                                                                        '        return timestamp;\n' +
                                                                                        '    }\n' +
                                                                                        '\n' +
                                                                                        '    public void setTimestamp(String timestamp) {\n' +
                                                                                        '        this.timestamp = timestamp;\n' +
                                                                                        '    }\n' +
                                                                                        '\n' +
                                                                                        '}'
                                                                                }
                                                                            ]

                                                                        }
                                                                    ]
                                                                }]
                                                        }
                                                    ]
                                                }
                                            ]
                                        }
                                    ]
                                },
                                {
                                    name: 'Dockerfile',
                                    file: 'docker',
                                    code: "FROM openjdk:8u212-jdk-alpine\n" +
                                        "COPY target/*SNAPSHOT.jar app.jar\n" +
                                        "EXPOSE 8080\n" +
                                        "ENTRYPOINT [\"java\",\"-Xmx400M\",\"-Djava.security.egd=file:/dev/./urandom\",\"-jar\",\"/app.jar\",\"--spring.profiles.active=docker\"]"
                                },
                                {
                                    name: 'README.md',
                                    file: 'md',
                                    code: ''
                                },
                                {
                                    name: 'cloudbuild.yaml',
                                    file: 'txt',
                                    code: "steps:\n" +
                                        "  ### Test\n" +
                                        "#  - id: 'test'\n" +
                                        "#    name: 'gcr.io/cloud-builders/mvn'\n" +
                                        "#    args: [\n" +
                                        "#      'test',\n" +
                                        "#      '-Dspring.profiles.active=test'\n" +
                                        "#    ]\n" +
                                        "  ### Build\n" +
                                        "  - id: 'build'\n" +
                                        "    name: 'gcr.io/cloud-builders/mvn'\n" +
                                        "    args: [\n" +
                                        "      'clean',\n" +
                                        "      'package',\n" +
                                        "      '-Dmaven.test.skip=true'\n" +
                                        "    ]\n" +
                                        "#    waitFor: ['test']\n" +
                                        "  ### docker Build\n" +
                                        "  - id: 'docker build'\n" +
                                        "    name: 'gcr.io/cloud-builders/docker'\n" +
                                        "    args:\n" +
                                        "      - 'build'\n" +
                                        "      - '--tag=gcr.io/$PROJECT_ID/$_PROJECT_NAME:$COMMIT_SHA'\n" +
                                        "      - '.'\n" +
                                        "  ### Publish\n" +
                                        "  - id: 'publish'\n" +
                                        "    name: 'gcr.io/cloud-builders/docker'\n" +
                                        "    entrypoint: 'bash'\n" +
                                        "    args:\n" +
                                        "      - '-c'\n" +
                                        "      - |\n" +
                                        "        docker push gcr.io/$PROJECT_ID/$_PROJECT_NAME:$COMMIT_SHA\n" +
                                        "  ### deploy\n" +
                                        "  - id: 'deploy'\n" +
                                        "    name: 'gcr.io/cloud-builders/gcloud'\n" +
                                        "    entrypoint: 'bash'\n" +
                                        "    args:\n" +
                                        "      - '-c'\n" +
                                        "      - |\n" +
                                        "        PROJECT=$$(gcloud config get-value core/project)\n" +
                                        "        gcloud container clusters get-credentials \"$${CLOUDSDK_CONTAINER_CLUSTER}\" \\\n" +
                                        "          --project \"$${PROJECT}\" \\\n" +
                                        "          --zone \"$${CLOUDSDK_COMPUTE_ZONE}\"\n" +
                                        "        cat <<EOF | kubectl apply -f -\n" +
                                        "        apiVersion: v1\n" +
                                        "        kind: Service\n" +
                                        "        metadata:\n" +
                                        "          name: $_PROJECT_NAME\n" +
                                        "          labels:\n" +
                                        "            app: $_PROJECT_NAME\n" +
                                        "        spec:\n" +
                                        "          ports:\n" +
                                        "            - port: 8080\n" +
                                        "              targetPort: 8080\n" +
                                        "          selector:\n" +
                                        "            app: $_PROJECT_NAME\n" +
                                        "        EOF\n" +
                                        "        cat <<EOF | kubectl apply -f -\n" +
                                        "        apiVersion: apps/v1\n" +
                                        "        kind: Deployment\n" +
                                        "        metadata:\n" +
                                        "          name: $_PROJECT_NAME\n" +
                                        "          labels:\n" +
                                        "            app: $_PROJECT_NAME\n" +
                                        "        spec:\n" +
                                        "          replicas: 1\n" +
                                        "          selector:\n" +
                                        "            matchLabels:\n" +
                                        "              app: $_PROJECT_NAME\n" +
                                        "          template:\n" +
                                        "            metadata:\n" +
                                        "              labels:\n" +
                                        "                app: $_PROJECT_NAME\n" +
                                        "            spec:\n" +
                                        "              containers:\n" +
                                        "                - name: $_PROJECT_NAME\n" +
                                        "                  image: gcr.io/$PROJECT_ID/$_PROJECT_NAME:$COMMIT_SHA\n" +
                                        "                  ports:\n" +
                                        "                    - containerPort: 8080\n" +
                                        "        EOF\n" +
                                        "options:\n" +
                                        "  env:\n" +
                                        "    #    # location/name of GKE cluster (used by all kubectl commands)\n" +
                                        "    - CLOUDSDK_COMPUTE_ZONE=asia-northeast1-a\n" +
                                        "    - CLOUDSDK_CONTAINER_CLUSTER=standard-cluster-1"
                                },
                                {
                                    name: 'mvnw',
                                    file: 'shell',
                                    code: "#!/bin/sh\n" +
                                        "# ----------------------------------------------------------------------------\n" +
                                        "# Licensed to the Apache Software Foundation (ASF) under one\n" +
                                        "# or more contributor license agreements.  See the NOTICE file\n" +
                                        "# distributed with this work for additional information\n" +
                                        "# regarding copyright ownership.  The ASF licenses this file\n" +
                                        "# to you under the Apache License, Version 2.0 (the\n" +
                                        "# \"License\"); you may not use this file except in compliance\n" +
                                        "# with the License.  You may obtain a copy of the License at\n" +
                                        "#\n" +
                                        "#    http://www.apache.org/licenses/LICENSE-2.0\n" +
                                        "#\n" +
                                        "# Unless required by applicable law or agreed to in writing,\n" +
                                        "# software distributed under the License is distributed on an\n" +
                                        "# \"AS IS\" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY\n" +
                                        "# KIND, either express or implied.  See the License for the\n" +
                                        "# specific language governing permissions and limitations\n" +
                                        "# under the License.\n" +
                                        "# ----------------------------------------------------------------------------\n" +
                                        "\n" +
                                        "# ----------------------------------------------------------------------------\n" +
                                        "# Maven2 Start Up Batch script\n" +
                                        "#\n" +
                                        "# Required ENV vars:\n" +
                                        "# ------------------\n" +
                                        "#   JAVA_HOME - location of a JDK home dir\n" +
                                        "#\n" +
                                        "# Optional ENV vars\n" +
                                        "# -----------------\n" +
                                        "#   M2_HOME - location of maven2's installed home dir\n" +
                                        "#   MAVEN_OPTS - parameters passed to the Java VM when running Maven\n" +
                                        "#     e.g. to debug Maven itself, use\n" +
                                        "#       set MAVEN_OPTS=-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000\n" +
                                        "#   MAVEN_SKIP_RC - flag to disable loading of mavenrc files\n" +
                                        "# ----------------------------------------------------------------------------\n" +
                                        "\n" +
                                        "if [ -z \"$MAVEN_SKIP_RC\" ] ; then\n" +
                                        "\n" +
                                        "  if [ -f /etc/mavenrc ] ; then\n" +
                                        "    . /etc/mavenrc\n" +
                                        "  fi\n" +
                                        "\n" +
                                        "  if [ -f \"$HOME/.mavenrc\" ] ; then\n" +
                                        "    . \"$HOME/.mavenrc\"\n" +
                                        "  fi\n" +
                                        "\n" +
                                        "fi\n" +
                                        "\n" +
                                        "# OS specific support.  $var _must_ be set to either true or false.\n" +
                                        "cygwin=false;\n" +
                                        "darwin=false;\n" +
                                        "mingw=false\n" +
                                        "case \"`uname`\" in\n" +
                                        "  CYGWIN*) cygwin=true ;;\n" +
                                        "  MINGW*) mingw=true;;\n" +
                                        "  Darwin*) darwin=true\n" +
                                        "    # Use /usr/libexec/java_home if available, otherwise fall back to /Library/Java/Home\n" +
                                        "    # See https://developer.apple.com/library/mac/qa/qa1170/_index.html\n" +
                                        "    if [ -z \"$JAVA_HOME\" ]; then\n" +
                                        "      if [ -x \"/usr/libexec/java_home\" ]; then\n" +
                                        "        export JAVA_HOME=\"`/usr/libexec/java_home`\"\n" +
                                        "      else\n" +
                                        "        export JAVA_HOME=\"/Library/Java/Home\"\n" +
                                        "      fi\n" +
                                        "    fi\n" +
                                        "    ;;\n" +
                                        "esac\n" +
                                        "\n" +
                                        "if [ -z \"$JAVA_HOME\" ] ; then\n" +
                                        "  if [ -r /etc/gentoo-release ] ; then\n" +
                                        "    JAVA_HOME=`java-config --jre-home`\n" +
                                        "  fi\n" +
                                        "fi\n" +
                                        "\n" +
                                        "if [ -z \"$M2_HOME\" ] ; then\n" +
                                        "  ## resolve links - $0 may be a link to maven's home\n" +
                                        "  PRG=\"$0\"\n" +
                                        "\n" +
                                        "  # need this for relative symlinks\n" +
                                        "  while [ -h \"$PRG\" ] ; do\n" +
                                        "    ls=`ls -ld \"$PRG\"`\n" +
                                        "    link=`expr \"$ls\" : '.*-> \\(.*\\)$'`\n" +
                                        "    if expr \"$link\" : '/.*' > /dev/null; then\n" +
                                        "      PRG=\"$link\"\n" +
                                        "    else\n" +
                                        "      PRG=\"`dirname \"$PRG\"`/$link\"\n" +
                                        "    fi\n" +
                                        "  done\n" +
                                        "\n" +
                                        "  saveddir=`pwd`\n" +
                                        "\n" +
                                        "  M2_HOME=`dirname \"$PRG\"`/..\n" +
                                        "\n" +
                                        "  # make it fully qualified\n" +
                                        "  M2_HOME=`cd \"$M2_HOME\" && pwd`\n" +
                                        "\n" +
                                        "  cd \"$saveddir\"\n" +
                                        "  # echo Using m2 at $M2_HOME\n" +
                                        "fi\n" +
                                        "\n" +
                                        "# For Cygwin, ensure paths are in UNIX format before anything is touched\n" +
                                        "if $cygwin ; then\n" +
                                        "  [ -n \"$M2_HOME\" ] &&\n" +
                                        "    M2_HOME=`cygpath --unix \"$M2_HOME\"`\n" +
                                        "  [ -n \"$JAVA_HOME\" ] &&\n" +
                                        "    JAVA_HOME=`cygpath --unix \"$JAVA_HOME\"`\n" +
                                        "  [ -n \"$CLASSPATH\" ] &&\n" +
                                        "    CLASSPATH=`cygpath --path --unix \"$CLASSPATH\"`\n" +
                                        "fi\n" +
                                        "\n" +
                                        "# For Mingw, ensure paths are in UNIX format before anything is touched\n" +
                                        "if $mingw ; then\n" +
                                        "  [ -n \"$M2_HOME\" ] &&\n" +
                                        "    M2_HOME=\"`(cd \"$M2_HOME\"; pwd)`\"\n" +
                                        "  [ -n \"$JAVA_HOME\" ] &&\n" +
                                        "    JAVA_HOME=\"`(cd \"$JAVA_HOME\"; pwd)`\"\n" +
                                        "  # TODO classpath?\n" +
                                        "fi\n" +
                                        "\n" +
                                        "if [ -z \"$JAVA_HOME\" ]; then\n" +
                                        "  javaExecutable=\"`which javac`\"\n" +
                                        "  if [ -n \"$javaExecutable\" ] && ! [ \"`expr \\" +
                                        "$javaExecutable\\ '\\([^ ]*\\)\'\" = \"no\" ]; then\n" +
                                        "    # readlink(1) is not available as standard on Solaris 10.\n" +
                                        "    readLink= \`which readlink\`\n" +
                                        "    if [ ! \`expr \"$readLink\" : '\\([^ ]*\\)'\` = \"no\" ]; then\n" +
                                        "      if $darwin ; then\n" +
                                        "        javaHome=\"\`dirname $javaExecutable\\\`" +
                                        "        javaExecutable=\"\`cd $javaHome\\ && pwd -P \`/javac\"\n" +
                                        "      else\n" +
                                        "        javaExecutable=\"\`readlink -f $javaExecutable \`\"\n" +
                                        "      fi\n" +
                                        "      javaHome=\"\`dirname $javaExecutable\\ \`\"\n" +
                                        "      javaHome=\`expr \"$javaHome\" : '\\(.*\\)/bin'\`\n" +
                                        "      JAVA_HOME=\"$javaHome\"\n" +
                                        "      export JAVA_HOME\n" +
                                        "    fi\n" +
                                        "  fi\n" +
                                        "fi\n" +
                                        "\n" +
                                        "if [ -z \"$JAVACMD\" ] ; then\n" +
                                        "  if [ -n \"$JAVA_HOME\"  ] ; then\n" +
                                        "    if [ -x \"$JAVA_HOME/jre/sh/java\" ] ; then\n" +
                                        "      # IBM's JDK on AIX uses strange locations for the executables\n" +
                                        "      JAVACMD=\"$JAVA_HOME/jre/sh/java\"\n" +
                                        "    else\n" +
                                        "      JAVACMD=\"$JAVA_HOME/bin/java\"\n" +
                                        "    fi\n" +
                                        "  else\n" +
                                        "    JAVACMD=\"\`which java\`\"\n" +
                                        "  fi\n" +
                                        "fi\n" +
                                        "\n" +
                                        "if [ ! -x \"$JAVACMD\" ] ; then\n" +
                                        "  echo \"Error: JAVA_HOME is not defined correctly.\" >&2\n" +
                                        "  echo \"  We cannot execute $JAVACMD\" >&2\n" +
                                        "  exit 1\n" +
                                        "fi\n" +
                                        "\n" +
                                        "if [ -z \"$JAVA_HOME\" ] ; then\n" +
                                        "  echo \"Warning: JAVA_HOME environment variable is not set.\"\n" +
                                        "fi\n" +
                                        "\n" +
                                        "CLASSWORLDS_LAUNCHER=org.codehaus.plexus.classworlds.launcher.Launcher\n" +
                                        "\n" +
                                        "# traverses directory structure from process work directory to filesystem root\n" +
                                        "# first directory with .mvn subdirectory is considered project base directory\n" +
                                        "find_maven_basedir() {\n" +
                                        "\n" +
                                        "  if [ -z \"$1\" ]\n" +
                                        "  then\n" +
                                        "    echo \"Path not specified to find_maven_basedir\"\n" +
                                        "    return 1\n" +
                                        "  fi\n" +
                                        "\n" +
                                        "  basedir=\"$1\"\n" +
                                        "  wdir=\"$1\"\n" +
                                        "  while [ \"$wdir\" != '/' ] ; do\n" +
                                        "    if [ -d \"$wdir\"/.mvn ] ; then\n" +
                                        "      basedir=$wdir\n" +
                                        "      break\n" +
                                        "    fi\n" +
                                        "    # workaround for JBEAP-8937 (on Solaris 10/Sparc)\n" +
                                        "    if [ -d \"\${wdir}\" ]; then\n" +
                                        "      wdir=\`cd \"$wdir/..\"; pwd\`\n" +
                                        "    fi\n" +
                                        "    # end of workaround\n" +
                                        "  done\n" +
                                        "  echo \"\${basedir}\"\n" +
                                        "}\n" +
                                        "\n" +
                                        "# concatenates all lines of a file\n" +
                                        "concat_lines() {\n" +
                                        "  if [ -f \"$1\" ]; then\n" +
                                        "    echo \"$(tr -s '\\n' ' ' < \"$1\")\"\n" +
                                        "  fi\n" +
                                        "}\n" +
                                        "\n" +
                                        "BASE_DIR=\`find_maven_basedir \"$(pwd)\"\`\n" +
                                        "if [ -z \"$BASE_DIR\" ]; then\n" +
                                        "  exit 1;\n" +
                                        "fi\n" +
                                        "\n" +
                                        "##########################################################################################\n" +
                                        "# Extension to allow automatically downloading the maven-wrapper.jar from Maven-central\n" +
                                        "# This allows using the maven wrapper in projects that prohibit checking in binary data.\n" +
                                        "##########################################################################################\n" +
                                        "if [ -r \"$BASE_DIR/.mvn/wrapper/maven-wrapper.jar\" ]; then\n" +
                                        "    if [ \"$MVNW_VERBOSE\" = true ]; then\n" +
                                        "      echo \"Found .mvn/wrapper/maven-wrapper.jar\"\n" +
                                        "    fi\n" +
                                        "else\n" +
                                        "    if [ \"$MVNW_VERBOSE\" = true ]; then\n" +
                                        "      echo \"Couldn't find .mvn/wrapper/maven-wrapper.jar, downloading it ...\"\n" +
                                        "    fi\n" +
                                        "    jarUrl=\"https://repo.maven.apache.org/maven2/io/takari/maven-wrapper/0.4.2/maven-wrapper-0.4.2.jar\"\n" +
                                        "    while IFS=\"=\" read key value; do\n" +
                                        "      case \"$key\" in (wrapperUrl) jarUrl=\"$value\"; break ;;\n" +
                                        "      esac\n" +
                                        "    done < \"$BASE_DIR/.mvn/wrapper/maven-wrapper.properties\"\n" +
                                        "    if [ \"$MVNW_VERBOSE\" = true ]; then\n" +
                                        "      echo \"Downloading from: $jarUrl\"\n" +
                                        "    fi\n" +
                                        "    wrapperJarPath=\"$BASE_DIR/.mvn/wrapper/maven-wrapper.jar\"\n" +
                                        "\n" +
                                        "    if command -v wget > /dev/null; then\n" +
                                        "        if [ \"$MVNW_VERBOSE\" = true ]; then\n" +
                                        "          echo \"Found wget ... using wget\"\n" +
                                        "        fi\n" +
                                        "        wget \"$jarUrl\" -O \"$wrapperJarPath\"\n" +
                                        "    elif command -v curl > /dev/null; then\n" +
                                        "        if [ \"$MVNW_VERBOSE\" = true ]; then\n" +
                                        "          echo \"Found curl ... using curl\"\n" +
                                        "        fi\n" +
                                        "        curl -o \"$wrapperJarPath\" \"$jarUrl\"\n" +
                                        "    else\n" +
                                        "        if [ \"$MVNW_VERBOSE\" = true ]; then\n" +
                                        "          echo \"Falling back to using Java to download\"\n" +
                                        "        fi\n" +
                                        "        javaClass=\"$BASE_DIR/.mvn/wrapper/MavenWrapperDownloader.java\"\n" +
                                        "        if [ -e \"$javaClass\" ]; then\n" +
                                        "            if [ ! -e \"$BASE_DIR/.mvn/wrapper/MavenWrapperDownloader.class\" ]; then\n" +
                                        "                if [ \"$MVNW_VERBOSE\" = true ]; then\n" +
                                        "                  echo \" - Compiling MavenWrapperDownloader.java ...\"\n" +
                                        "                fi\n" +
                                        "                # Compiling the Java class\n" +
                                        "                (\"$JAVA_HOME/bin/javac\" \"$javaClass\")\n" +
                                        "            fi\n" +
                                        "            if [ -e \"$BASE_DIR/.mvn/wrapper/MavenWrapperDownloader.class\" ]; then\n" +
                                        "                # Running the downloader\n" +
                                        "                if [ \"$MVNW_VERBOSE\" = true ]; then\n" +
                                        "                  echo \" - Running MavenWrapperDownloader.java ...\"\n" +
                                        "                fi\n" +
                                        "                (\"$JAVA_HOME/bin/java\" -cp .mvn/wrapper MavenWrapperDownloader \"$MAVEN_PROJECTBASEDIR\")\n" +
                                        "            fi\n" +
                                        "        fi\n" +
                                        "    fi\n" +
                                        "fi\n" +
                                        "##########################################################################################\n" +
                                        "# End of extension\n" +
                                        "##########################################################################################\n" +
                                        "\n" +
                                        "export MAVEN_PROJECTBASEDIR=\${MAVEN_BASEDIR:-\"$BASE_DIR\"}\n" +
                                        "if [ \"$MVNW_VERBOSE\" = true ]; then\n" +
                                        "  echo $MAVEN_PROJECTBASEDIR\n" +
                                        "fi\n" +
                                        "MAVEN_OPTS=\"$(concat_lines \"$MAVEN_PROJECTBASEDIR/.mvn/jvm.config\") $MAVEN_OPTS\"\n" +
                                        "\n" +
                                        "# For Cygwin, switch paths to Windows format before running java\n" +
                                        "if $cygwin; then\n" +
                                        "  [ -n \"$M2_HOME\" ] &&\n" +
                                        "    M2_HOME=\`cygpath --path --windows \"$M2_HOME\"\`\n" +
                                        "  [ -n \"$JAVA_HOME\" ] &&\n" +
                                        "    JAVA_HOME=\`cygpath --path --windows \"$JAVA_HOME\"\`\n" +
                                        "  [ -n \"$CLASSPATH\" ] &&\n" +
                                        "    CLASSPATH=\`cygpath --path --windows \"$CLASSPATH\"\`\n" +
                                        "  [ -n \"$MAVEN_PROJECTBASEDIR\" ] &&\n" +
                                        "    MAVEN_PROJECTBASEDIR=\`cygpath --path --windows \"$MAVEN_PROJECTBASEDIR\"\`\n" +
                                        "fi\n" +
                                        "\n" +
                                        "WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain\n" +
                                        "\n" +
                                        "exec \"$JAVACMD\" \\\n" +
                                        "  $MAVEN_OPTS \\\n" +
                                        "  -classpath \"$MAVEN_PROJECTBASEDIR/.mvn/wrapper/maven-wrapper.jar\" \\\n" +
                                        "  \"-Dmaven.home=\${M2_HOME}\" \"-Dmaven.multiModuleProjectDirectory=\${MAVEN_PROJECTBASEDIR}\" \\\n" +
                                        "  \${WRAPPER_LAUNCHER} $MAVEN_CONFIG \"$@\""
                                },
                                {
                                    name: 'mvnw.cmd',
                                    file: 'shell',
                                    code: "@REM ----------------------------------------------------------------------------\n" +
                                        "@REM Licensed to the Apache Software Foundation (ASF) under one\n" +
                                        "@REM or more contributor license agreements.  See the NOTICE file\n" +
                                        "@REM distributed with this work for additional information\n" +
                                        "@REM regarding copyright ownership.  The ASF licenses this file\n" +
                                        "@REM to you under the Apache License, Version 2.0 (the\n" +
                                        "@REM \"License\"); you may not use this file except in compliance\n" +
                                        "@REM with the License.  You may obtain a copy of the License at\n" +
                                        "@REM\n" +
                                        "@REM    http://www.apache.org/licenses/LICENSE-2.0\n" +
                                        "@REM\n" +
                                        "@REM Unless required by applicable law or agreed to in writing,\n" +
                                        "@REM software distributed under the License is distributed on an\n" +
                                        "@REM \"AS IS\" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY\n" +
                                        "@REM KIND, either express or implied.  See the License for the\n" +
                                        "@REM specific language governing permissions and limitations\n" +
                                        "@REM under the License.\n" +
                                        "@REM ----------------------------------------------------------------------------\n" +
                                        "\n" +
                                        "@REM ----------------------------------------------------------------------------\n" +
                                        "@REM Maven2 Start Up Batch script\n" +
                                        "@REM\n" +
                                        "@REM Required ENV vars:\n" +
                                        "@REM JAVA_HOME - location of a JDK home dir\n" +
                                        "@REM\n" +
                                        "@REM Optional ENV vars\n" +
                                        "@REM M2_HOME - location of maven2's installed home dir\n" +
                                        "@REM MAVEN_BATCH_ECHO - set to 'on' to enable the echoing of the batch commands\n" +
                                        "@REM MAVEN_BATCH_PAUSE - set to 'on' to wait for a key stroke before ending\n" +
                                        "@REM MAVEN_OPTS - parameters passed to the Java VM when running Maven\n" +
                                        "@REM     e.g. to debug Maven itself, use\n" +
                                        "@REM set MAVEN_OPTS=-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000\n" +
                                        "@REM MAVEN_SKIP_RC - flag to disable loading of mavenrc files\n" +
                                        "@REM ----------------------------------------------------------------------------\n" +
                                        "\n" +
                                        "@REM Begin all REM lines with '@' in case MAVEN_BATCH_ECHO is 'on'\n" +
                                        "@echo off\n" +
                                        "@REM set title of command window\n" +
                                        "title %0\n" +
                                        "@REM enable echoing my setting MAVEN_BATCH_ECHO to 'on'\n" +
                                        "@if \"%MAVEN_BATCH_ECHO%\" == \"on\"  echo %MAVEN_BATCH_ECHO%\n" +
                                        "\n" +
                                        "@REM set %HOME% to equivalent of $HOME\n" +
                                        "if \"%HOME%\" == \"\" (set \"HOME=%HOMEDRIVE%%HOMEPATH%\")\n" +
                                        "\n" +
                                        "@REM Execute a user defined script before this one\n" +
                                        "if not \"%MAVEN_SKIP_RC%\" == \"\" goto skipRcPre\n" +
                                        "@REM check for pre script, once with legacy .bat ending and once with .cmd ending\n" +
                                        "if exist \"%HOME%\\mavenrc_pre.bat\" call \"%HOME%\\mavenrc_pre.bat\"\n" +
                                        "if exist \"%HOME%\\mavenrc_pre.cmd\" call \"%HOME%\\mavenrc_pre.cmd\"\n" +
                                        ":skipRcPre\n" +
                                        "\n" +
                                        "@setlocal\n" +
                                        "\n" +
                                        "set ERROR_CODE=0\n" +
                                        "\n" +
                                        "@REM To isolate internal variables from possible post scripts, we use another setlocal\n" +
                                        "@setlocal\n" +
                                        "\n" +
                                        "@REM ==== START VALIDATION ====\n" +
                                        "if not \"%JAVA_HOME%\" == \"\" goto OkJHome\n" +
                                        "\n" +
                                        "echo.\n" +
                                        "echo Error: JAVA_HOME not found in your environment. >&2\n" +
                                        "echo Please set the JAVA_HOME variable in your environment to match the >&2\n" +
                                        "echo location of your Java installation. >&2\n" +
                                        "echo.\n" +
                                        "goto error\n" +
                                        "\n" +
                                        ":OkJHome\n" +
                                        "if exist \"%JAVA_HOME%\\bin\\java.exe\" goto init\n" +
                                        "\n" +
                                        "echo.\n" +
                                        "echo Error: JAVA_HOME is set to an invalid directory. >&2\n" +
                                        "echo JAVA_HOME = \"%JAVA_HOME%\" >&2\n" +
                                        "echo Please set the JAVA_HOME variable in your environment to match the >&2\n" +
                                        "echo location of your Java installation. >&2\n" +
                                        "echo.\n" +
                                        "goto error\n" +
                                        "\n" +
                                        "@REM ==== END VALIDATION ====\n" +
                                        "\n" +
                                        ":init\n" +
                                        "\n" +
                                        "@REM Find the project base dir, i.e. the directory that contains the folder \".mvn\".\n" +
                                        "@REM Fallback to current working directory if not found.\n" +
                                        "\n" +
                                        "set MAVEN_PROJECTBASEDIR=%MAVEN_BASEDIR%\n" +
                                        "IF NOT \"%MAVEN_PROJECTBASEDIR%\"==\"\" goto endDetectBaseDir\n" +
                                        "\n" +
                                        "set EXEC_DIR=%CD%\n" +
                                        "set WDIR=%EXEC_DIR%\n" +
                                        ":findBaseDir\n" +
                                        "IF EXIST \"%WDIR%\"\\.mvn goto baseDirFound\n" +
                                        "cd ..\n" +
                                        "IF \"%WDIR%\"==\"%CD%\" goto baseDirNotFound\n" +
                                        "set WDIR=%CD%\n" +
                                        "goto findBaseDir\n" +
                                        "\n" +
                                        ":baseDirFound\n" +
                                        "set MAVEN_PROJECTBASEDIR=%WDIR%\n" +
                                        "cd \"%EXEC_DIR%\"\n" +
                                        "goto endDetectBaseDir\n" +
                                        "\n" +
                                        ":baseDirNotFound\n" +
                                        "set MAVEN_PROJECTBASEDIR=%EXEC_DIR%\n" +
                                        "cd \"%EXEC_DIR%\"\n" +
                                        "\n" +
                                        ":endDetectBaseDir\n" +
                                        "\n" +
                                        "IF NOT EXIST \"%MAVEN_PROJECTBASEDIR%\\.mvn\\jvm.config\" goto endReadAdditionalConfig\n" +
                                        "\n" +
                                        "@setlocal EnableExtensions EnableDelayedExpansion\n" +
                                        "for /F \"usebackq delims=\" %%a in (\"%MAVEN_PROJECTBASEDIR%\\.mvn\\jvm.config\") do set JVM_CONFIG_MAVEN_PROPS=!JVM_CONFIG_MAVEN_PROPS! %%a\n" +
                                        "@endlocal & set JVM_CONFIG_MAVEN_PROPS=%JVM_CONFIG_MAVEN_PROPS%\n" +
                                        "\n" +
                                        ":endReadAdditionalConfig\n" +
                                        "\n" +
                                        "SET MAVEN_JAVA_EXE=\"%JAVA_HOME%\\bin\\java.exe\"\n" +
                                        "set WRAPPER_JAR=\"%MAVEN_PROJECTBASEDIR%\\.mvn\\wrapper\\maven-wrapper.jar\"\n" +
                                        "set WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain\n" +
                                        "\n" +
                                        "set DOWNLOAD_URL=\"https://repo.maven.apache.org/maven2/io/takari/maven-wrapper/0.4.2/maven-wrapper-0.4.2.jar\"\n" +
                                        "FOR /F \"tokens=1,2 delims==\" %%A IN (%MAVEN_PROJECTBASEDIR%\\.mvn\\wrapper\\maven-wrapper.properties) DO (\n" +
                                        "\tIF \"%%A\"==\"wrapperUrl\" SET DOWNLOAD_URL=%%B \n" +
                                        ")\n" +
                                        "\n" +
                                        "@REM Extension to allow automatically downloading the maven-wrapper.jar from Maven-central\n" +
                                        "@REM This allows using the maven wrapper in projects that prohibit checking in binary data.\n" +
                                        "if exist %WRAPPER_JAR% (\n" +
                                        "    echo Found %WRAPPER_JAR%\n" +
                                        ") else (\n" +
                                        "    echo Couldn't find %WRAPPER_JAR%, downloading it ...\n" +
                                        "\techo Downloading from: %DOWNLOAD_URL%\n" +
                                        "    powershell -Command \"(New-Object Net.WebClient).DownloadFile('%DOWNLOAD_URL%', '%WRAPPER_JAR%')\"\n" +
                                        "    echo Finished downloading %WRAPPER_JAR%\n" +
                                        ")\n" +
                                        "@REM End of extension\n" +
                                        "\n" +
                                        "%MAVEN_JAVA_EXE% %JVM_CONFIG_MAVEN_PROPS% %MAVEN_OPTS% %MAVEN_DEBUG_OPTS% -classpath %WRAPPER_JAR% \"-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR%\" %WRAPPER_LAUNCHER% %MAVEN_CONFIG% %*\n" +
                                        "if ERRORLEVEL 1 goto error\n" +
                                        "goto end\n" +
                                        "\n" +
                                        ":error\n" +
                                        "set ERROR_CODE=1\n" +
                                        "\n" +
                                        ":end\n" +
                                        "@endlocal & set ERROR_CODE=%ERROR_CODE%\n" +
                                        "\n" +
                                        "if not \"%MAVEN_SKIP_RC%\" == \"\" goto skipRcPost\n" +
                                        "@REM check for post script, once with legacy .bat ending and once with .cmd ending\n" +
                                        "if exist \"%HOME%\\mavenrc_post.bat\" call \"%HOME%\\mavenrc_post.bat\"\n" +
                                        "if exist \"%HOME%\\mavenrc_post.cmd\" call \"%HOME%\\mavenrc_post.cmd\"\n" +
                                        ":skipRcPost\n" +
                                        "\n" +
                                        "@REM pause the script if MAVEN_BATCH_PAUSE is set to 'on'\n" +
                                        "if \"%MAVEN_BATCH_PAUSE%\" == \"on\" pause\n" +
                                        "\n" +
                                        "if \"%MAVEN_TERMINATE_CMD%\" == \"on\" exit %ERROR_CODE%\n" +
                                        "\n" +
                                        "exit /B %ERROR_CODE%"
                                },
                                {
                                    name: 'pom.xml',
                                    file: 'xml',
                                    code: "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                                        "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                                        "\txsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                                        "\t<modelVersion>4.0.0</modelVersion>\n" +
                                        "\t<parent>\n" +
                                        "\t\t<groupId>org.springframework.boot</groupId>\n" +
                                        "\t\t<artifactId>spring-boot-starter-parent</artifactId>\n" +
                                        "\t\t<version>2.1.1.RELEASE</version>\n" +
                                        "\t\t<relativePath/> <!-- lookup parent from repository -->\n" +
                                        "\t</parent>\n" +
                                        "\t<groupId>com.example</groupId>\n" +
                                        "\t<artifactId>boot-camp-products</artifactId>\n" +
                                        "\t<version>0.0.1.BUILD-SNAPSHOT</version>\n" +
                                        "\t<name>boot-camp-products</name>\n" +
                                        "\t<description>Demo project for Spring Boot</description>\n" +
                                        "\n" +
                                        "\t<properties>\n" +
                                        "\t\t<java.version>1.8</java.version>\n" +
                                        "\t\t<spring-cloud.version>Greenwich.RELEASE</spring-cloud.version>\n" +
                                        "\t</properties>\n" +
                                        "\n" +
                                        "\t<dependencies>\n" +
                                        "\t\t<dependency>\n" +
                                        "\t\t\t<groupId>org.springframework.boot</groupId>\n" +
                                        "\t\t\t<artifactId>spring-boot-starter-web</artifactId>\n" +
                                        "\t\t</dependency>\n" +
                                        "\t\t<dependency>\n" +
                                        "\t\t\t<groupId>org.springframework.boot</groupId>\n" +
                                        "\t\t\t<artifactId>spring-boot-starter-data-rest</artifactId>\n" +
                                        "\t\t</dependency>\n" +
                                        "\t\t<dependency>\n" +
                                        "\t\t\t<groupId>org.springframework.kafka</groupId>\n" +
                                        "\t\t\t<artifactId>spring-kafka</artifactId>\n" +
                                        "\t\t</dependency>\n" +
                                        "\t\t<dependency>\n" +
                                        "\t\t\t<groupId>org.springframework.boot</groupId>\n" +
                                        "\t\t\t<artifactId>spring-boot-starter-data-jpa</artifactId>\n" +
                                        "\t\t</dependency>\n" +
                                        "\t\t<dependency>\n" +
                                        "\t\t\t<groupId>com.h2database</groupId>\n" +
                                        "\t\t\t<artifactId>h2</artifactId>\n" +
                                        "\t\t\t<scope>runtime</scope>\n" +
                                        "\t\t</dependency>\n" +
                                        "\n" +
                                        "\t\t<dependency>\n" +
                                        "\t\t\t<groupId>com.google.code.gson</groupId>\n" +
                                        "\t\t\t<artifactId>gson</artifactId>\n" +
                                        "\t\t\t<version>2.8.5</version>\n" +
                                        "\t\t\t<scope>compile</scope>\n" +
                                        "\t\t</dependency>\n" +
                                        "\t\t\n" +
                                        "\t\t<dependency>\n" +
                                        "\t\t\t<groupId>org.apache.commons</groupId>\n" +
                                        "\t\t\t<artifactId>commons-lang3</artifactId>\n" +
                                        "\t\t\t<version>3.8.1</version>\n" +
                                        "\t\t</dependency>\n" +
                                        "\n" +
                                        "\t</dependencies>\n" +
                                        "\n" +
                                        "\t<dependencyManagement>\n" +
                                        "\t\t<dependencies>\n" +
                                        "\t\t\t<dependency>\n" +
                                        "\t\t\t\t<groupId>org.springframework.cloud</groupId>\n" +
                                        "\t\t\t\t<artifactId>spring-cloud-dependencies</artifactId>\n" +
                                        "\t\t\t\t<version>${spring-cloud.version}</version>\n" +
                                        "\t\t\t\t<type>pom</type>\n" +
                                        "\t\t\t\t<scope>import</scope>\n" +
                                        "\t\t\t</dependency>\n" +
                                        "\t\t</dependencies>\n" +
                                        "\t</dependencyManagement>\n" +
                                        "\n" +
                                        "\t<build>\n" +
                                        "\t\t<plugins>\n" +
                                        "\t\t\t<plugin>\n" +
                                        "\t\t\t\t<groupId>org.springframework.boot</groupId>\n" +
                                        "\t\t\t\t<artifactId>spring-boot-maven-plugin</artifactId>\n" +
                                        "\t\t\t</plugin>\n" +
                                        "\t\t</plugins>\n" +
                                        "\t</build>\n" +
                                        "\n" +
                                        "</project>"
                                },
                            ]
                            var boundedFolder = {
                                name: item.inputText,
                                children: boundedItems
                            }

                            tmpList.push(boundedFolder)
                            console.log(item)

                            item.dataList.forEach(function (tmpItem) {
                                if (tmpItem._type == 'org.uengine.uml.model.Domain' && tmpItem.inputText.length > 0) {
                                    console.log(tmpItem.upName)

                                    event.name = tmpItem.upName + '.java';
                                    event.type = tmpItem._type;
                                    event.code = tmpItem.code;
                                    event.file = 'java'

                                    tmpList.some(function (tmp, index) {
                                        if (tmp.name == tmpItem.boundedContext) {
                                            tmp.children[1].children[0].children[1].children[0].children[0].children[0].children.push(JSON.parse(JSON.stringify(event)))
                                        }
                                    })
                                    // [1].children[0].children[1].children[0].children[0].children[0].children.push(event)
                                } else if (tmpItem._type == 'org.uengine.uml.model.Aggregate' && tmpItem.inputText.length > 0) {
                                    tmpList.some(function (tmp, index) {
                                        if (tmp.name == tmpItem.boundedContext) {
                                            var repositoryTmp = JSON.parse(JSON.stringify(event));
                                            repositoryTmp.name = tmpItem.upName + 'Repository.java';
                                            repositoryTmp.type = tmpItem._type;
                                            repositoryTmp.code = tmpItem.repositoryCode;
                                            repositoryTmp.file = 'java'
                                            tmp.children[1].children[0].children[1].children[0].children[0].children[0].children.push(repositoryTmp);

                                            var aggregateTmp = JSON.parse(JSON.stringify(event));
                                            aggregateTmp.name = tmpItem.upName + '.java';
                                            aggregateTmp.type = tmpItem._type;
                                            aggregateTmp.code = tmpItem.aggregateCode;
                                            aggregateTmp.file = 'java'
                                            tmp.children[1].children[0].children[1].children[0].children[0].children[0].children.push(aggregateTmp);

                                            var eventLisnterTmp = JSON.parse(JSON.stringify(event));
                                            eventLisnterTmp.name = tmpItem.upName + 'EventListener.java';
                                            eventLisnterTmp.type = tmpItem._type;
                                            eventLisnterTmp.code = tmpItem.eventListenerCode;
                                            eventLisnterTmp.file = 'java'
                                            tmp.children[1].children[0].children[1].children[0].children[0].children[0].children.push(eventLisnterTmp);

                                            var controllerTmp = JSON.parse(JSON.stringify(event));
                                            controllerTmp.name = tmpItem.upName + 'Controller.java';
                                            controllerTmp.type = tmpItem._type;
                                            controllerTmp.code = tmpItem.controllerCode;
                                            controllerTmp.file = 'java'
                                            tmp.children[1].children[0].children[1].children[0].children[0].children[0].children.push(controllerTmp);
                                        }
                                    })
                                }
                            })
                        }
                    });

                    return tmpList
                }
            }
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

            me.$ModelingBus.$on('umlDiagram', function () {
                me.umlModalShow()
                console.log("aa")
                me.$nextTick(function () {
                })
            });

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
