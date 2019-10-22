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
        </v-row>

    </v-container>
</template>

<script>
    export default {
        name: "EventStormingListPage",
        data() {
            return {
                stormingList: [{id: 1, name: 'initData'},{id: 2, name: 'testData'},{id: 3, name: 'testData2'},{id: 4, name: 'testData3'}]
            }
        }
    }
</script>

<style scoped>

</style>
