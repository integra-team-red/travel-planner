<script setup lang="ts">
import type {SpaDTO} from "../../typescript-client";

defineProps<{
    spa: SpaDTO,
    cityName: string
    isSelected: boolean;
}>();

const emits = defineEmits(["card-clicked"]);
</script>

<template>
    <Card :class="'!bg-[#EDF7FC] !border-[#ADCDE2] border-' + (+isSelected * 2)" @click="emits('card-clicked')" pt:content:class="!flex !flex-row justify-between">
        <template #title>{{spa.name}}</template>
        <template #content>
            <p>{{ cityName }}</p>
        </template>
    </Card>
</template>

<style scoped>
</style>