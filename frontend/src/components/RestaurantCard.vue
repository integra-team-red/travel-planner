<script setup lang="ts">
    import type {RestaurantDTO} from "../../typescript-client";

    defineProps<{
      restaurant: RestaurantDTO,
      selected: boolean
    }>()

    const emits = defineEmits(["checkbox-clicked","card-clicked"])
</script>

<template>
    <Card class="w-full my-8" @click="emits('card-clicked')">
        <template #title>{{restaurant.name}}</template>
        <template #content>
            <div class="flex justify-between">
                    <Checkbox :value="selected" binary @click="emits('checkbox-clicked')"/>
            </div>
        </template>
    </Card>
</template>

<style scoped>

</style>