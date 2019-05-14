<!--
       * Todo: Property 작성 하는 부분 *
-->
<template v-else>
    <v-layout row justify-center>
        <v-dialog v-model="navigationDrawer" max-width="600px">
            <!-- Bounded Context Setting Start -->
            <v-card v-if="value.name == 'Bounded Context'">
                <v-card-title>
                    <span class="headline">{{titleName}} 내용 입력 </span>
                </v-card-title>
                <v-card-text>
                    <v-autocomplete
                            v-model="input"
                            :items="aggregateList"
                            label="Aggregate"
                            persistent-hint
                            prepend-icon="mdi-city"
                    >
                    </v-autocomplete>
                </v-card-text>
            </v-card>
            <!-- Bounded Context Setting End -->
            <!-- Other Component Setting Start -->
            <v-card v-else>
                <v-card-title>
                    <span class="headline">{{titleName}} 내용 입력 </span>
                </v-card-title>
                <v-card-text>
                    <v-container fluid grid-list-md>
                        <v-textarea
                                name="input-7-1"
                                outline
                                :label="titleName"
                                auto-grow
                                v-model="input"
                        ></v-textarea>
                    </v-container>
                </v-card-text>
                <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn color="blue darken-1" flat @click="navigationDrawer = false">확인</v-btn>
                </v-card-actions>
                <!-- Other Component Setting Stop -->
            </v-card>
        </v-dialog>
    </v-layout>
</template>

<script>
    export default {
        name: 'modeling-property-panel',
        props: {
            drawer: Boolean,
            value: Object,
            titleName: String,
            inputText: String,
            aggregateList: Array
        },
        computed: {},
        data: function () {
            var me = this;
            return {
                navigationDrawer: false,
                _item: this.value,
                preventWatch: false,
                x: null,
                y: null,
                width: null,
                height: null,
                style: [],
                active: null,
                tracingTag: null,
                input: ''
            }
        },
        created: function () {

        },
        mounted: function () {
            console.log(value)
        },
        watch: {
            input: function (newVal) {
                this.$emit('update:inputText', newVal)
            },
            drawer: function (val) {
                this.navigationDrawer = val;
            },
            value: {
                handler: function () {
                    this.$emit("input", this.value);
                },
                deep: true
            },
            //프로퍼티 창이 오픈되었을 때 모델값을 새로 반영한다.
            navigationDrawer: {
                handler: function (val, oldval) {
                    if (val == true) {
                        this._item = this.value;

                        if (this.value.elementView) {
                            this.x = this.value.elementView.x;
                            this.y = this.value.elementView.y;
                            this.width = this.value.elementView.width;
                            this.height = this.value.elementView.height;
                        }
                        this.$emit('update:drawer', val);
                    } else {
                        //프로퍼티 에디팅 해제.
                        this.$emit('update:drawer', false);

                    }
                }
            },
            x: function (val) {
                this._item.elementView.x = val;
                this.$emit('update:value', this._item);
            },
            y: function (val) {
                this._item.elementView.y = val;
                this.$emit('update:value', this._item);
            },
            width: function (val) {
                this._item.elementView.width = val;
                this.$emit('update:value', this._item);
            },
            height: function (val) {
                this._item.elementView.height = val;
                this.$emit('update:value', this._item);
            },
            style: {
                handler: function (newVal, oldVal) {
                    var style = {};
                    if (newVal && newVal.length) {
                        $.each(newVal, function (i, item) {
                            style[item.key] = item.value;
                        });
                    }
                    var view = this._item.elementView || this._item.relationView;
                    view.style = JSON.stringify(style);
                    this.$emit('update:value', this._item);
                },
                deep: true
            }
        },
        mounted: function () {

        },
        methods: {

        }
    }
</script>


<style lang="scss" rel="stylesheet/scss">
    .md-sidenav .md-sidenav-content {
        width: 400px
    }

    .md-sidenav.md-right .md-sidenav-content {
        width: 600px;
    }

</style>

