<script setup lang="ts">
    import type {POIDTO} from '../../typescript-client';

    defineProps<{
        poi: POIDTO,
        selected: boolean
    }>()

    const emits = defineEmits(["checkbox-clicked", "card-clicked"])
    const PLACEHOLDER_IMAGE = "https://dummyimage.com/600x400/ADCDE2&text=+"
</script>

<template>
    <Card class="w-full my-8 overflow-hidden" @click="emits('card-clicked')">
        <template #header>
            <img alt="" :src="poi.image ?? PLACEHOLDER_IMAGE" class="card-img h-32 w-full object-cover bg-linear-to-w" />
        </template>
        <template #title>{{poi.name}}</template>
        <template #content>
            <div class="flex justify-between">
                <p class="m-0">{{ poi.type }}</p>
                <span @click.stop>
                    <Checkbox :value="selected" binary @click="emits('checkbox-clicked')"/>
                </span>
            </div>
        </template>
    </Card>
</template>

<style scoped>
.card-img {
    mask-image: linear-gradient(to bottom, rgba(0,0,0,1) 60%, rgba(0,0,0,0));
}
</style>
