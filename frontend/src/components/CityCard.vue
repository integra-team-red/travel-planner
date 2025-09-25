<script setup lang="ts">
    import type {CityDTO} from '../../typescript-client';

    defineProps<{
        city: CityDTO,
        selected: boolean
    }>()

    const emits = defineEmits(["checkbox-clicked"])
</script>

<template>
    <Card class="w-full my-8">
        <template #title>{{city.name}}</template>
        <template #content>
            <div class="flex justify-between">
                <p class="m-0">County, Country</p>
                <Checkbox :value="selected" binary @click="emits('checkbox-clicked')"/>
            </div>
        </template>
    </Card>
</template>

<style scoped>

</style>
