<script setup lang="ts">
    import type {CoffeeShopDTO} from "../../typescript-client";

    defineProps<{
        coffee_shop: CoffeeShopDTO,
        selected: boolean
    }>()

    const emits = defineEmits(["checkbox-clicked", "card-clicked"])
    const PLACEHOLDER_IMAGE = "https://dummyimage.com/600x400/ADCDE2&text=+"

</script>

<template>
    <Card class="w-full my-8 overflow-hidden" @click="emits('card-clicked')">
        <template #header>
            <img alt="" :src="coffee_shop.image ? coffee_shop.image : PLACEHOLDER_IMAGE" class="card-img h-32 w-full object-cover bg-linear-to-w"/>
        </template>
        <template #title>{{coffee_shop.name}}</template>
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
.card-img {
    mask-image: linear-gradient(to bottom, rgba(0,0,0,1) 60%, rgba(0,0,0,0));
}

</style>