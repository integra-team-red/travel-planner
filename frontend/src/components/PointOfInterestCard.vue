<script setup lang="ts">
    import type {POIDTO} from '../../typescript-client';

    defineProps<{
        poi: POIDTO,
        selected: boolean
    }>()

    const emits = defineEmits(["checkbox-clicked", "card-clicked"])
</script>

<template>
    <Card class="w-full my-8" @click="emits('card-clicked')">
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

</style>
