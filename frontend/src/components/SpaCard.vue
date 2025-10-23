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
    <Card class="!bg-[#EDF7FC] !border-[#ADCDE2]" :class="{'border-2': isSelected}" @click="emits('card-clicked')" pt:content:class="!flex !flex-row justify-between">
        <template #title>{{spa.name}}</template>
        <template #content>
            <p>{{ cityName }}</p>
        </template>
    </Card>
</template>

<style scoped>
</style>