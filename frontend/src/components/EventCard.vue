<script setup lang="ts">
    import type {EventDTO} from '../../typescript-client';

    defineProps<{
        event: EventDTO,
        selected: boolean
    }>()

    const emits = defineEmits(["checkbox-clicked", "card-clicked"])
</script>

<template>
    <Card class="w-full my-8 overflow-hidden" @click="emits('card-clicked')">
        <template #title>{{event.name}}</template>
        <template #content>
            <div class="flex justify-between">
                <p class="m-0">{{ event.audience }}</p>
                <span @click.stop>
                    <Checkbox :value="selected" binary @click="emits('checkbox-clicked')"/>
                </span>
            </div>
        </template>
    </Card>
</template>

<style scoped>
</style>
