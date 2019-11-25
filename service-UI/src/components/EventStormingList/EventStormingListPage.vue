<template>
    <v-container>
        <v-row>
            <v-col
                    v-for="(item, key) in stormingList"
                    :key="item.id"
                    cols="6"
                    md="4"
            >
                <v-card
                        class="mx-auto"
                        max-width="344"

                >
                    <v-img
                            src="https://cdn.vuetifyjs.com/images/cards/sunshine.jpg"
                            height="200px"
                    ></v-img>

                    <v-card-title>
                        {{item.name}}
                    </v-card-title>
                    <v-card-actions>
                        <v-btn text :to="'event/'+item.id.toString()">Edit</v-btn>

                        <v-btn
                                color="purple"
                                text
                        >
                            Explore
                        </v-btn>
                    </v-card-actions>
                </v-card>
            </v-col>
            <v-col
                    cols="6"
                    md="4"
            >
                <v-card
                        class="mx-auto"
                        max-width="344"
                        align="center"
                >
                <v-btn text block @click="addDialog=true"> 추가 </v-btn>
                </v-card>
            </v-col>
        </v-row>

        <v-dialog v-model="addDialog" width="300px">
            <v-card>
                <v-text-field
                        v-model="name"
                        label="Name"
                ></v-text-field>
                <v-btn @click="stormingListAdd"> 추가</v-btn>
            </v-card>
        </v-dialog>

    </v-container>
</template>

<script>
    export default {
        name: "EventStormingListPage",
        data() {
            return {
                stormingList: [
                    {id: 1, name: 'initData'},
                    ],
                addDialog:false,
                name:'Test',
            }
        },
        methods:{
            stormingListAdd(){
                var me =  this
                    let data={
                        'id': me.stormingList.length,
                        'name': me.name
                    }
                me.stormingList.push(data)
                me.addDialog=false
            },
        }
    }
</script>

<style scoped>

</style>
