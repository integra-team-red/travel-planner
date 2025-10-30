<script setup lang="ts">
    import type {RestaurantDTO} from "../../typescript-client";

    defineProps<{
      restaurant: RestaurantDTO,
      selected: boolean
    }>()

    const emits = defineEmits(["checkbox-clicked","card-clicked"])
    const PLACEHOLDER_IMAGE = "https://dummyimage.com/600x400/ADCDE2&text=+"
</script>

<template>
    <Card class="w-full my-8" @click="emits('card-clicked')">
        <template #header>
            <img alt="" :src="restaurant.image ? restaurant.image : PLACEHOLDER_IMAGE" class="card-img h-32 w-full object-cover bg-linear-to-w"/>
        </template>
        <template #title>{{restaurant.name}}</template>
        <template #content>
            <div class="flex justify-between">
                <span @click.stop>
                    <Checkbox :value="selected" binary @click="emits('checkbox-clicked')"/>
                </span>
            </div>
        </template>
    </Card>
</template>

<style scoped>
.card-img{
    mask-image: linear-gradient(to bottom, rgba(0,0,0,1) 60%, rgba(0,0,0,0));
}
</style>