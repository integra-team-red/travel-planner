<script setup lang="ts">
import type {ProposalDTO} from "../../typescript-client";
import {toCapitalCaseFromAllCaps} from "@/utils/text.utils.ts";

defineProps<{
    proposal: ProposalDTO,
    cityName: string
    isSelected: boolean;
}>();
const emits = defineEmits(["card-clicked"]);

const CARD_COLORS = {
    'PENDING': '!bg-[#EDF7FC]',
    'APPROVED': '!bg-[#DDEDE3]',
    'REJECTED': '!bg-[#FAE3E3]'
};
const CARD_BORDER_COLORS = {
    'PENDING': '!border-[#ADCDE2]',
    'APPROVED': '!border-[#22C55E]',
    'REJECTED': '!border-[#EF4444]'
}

</script>

<template>
    <!-- NOTE(MC): If you find a way to add shadows to cards then please do so. Overriding pt:root:class, :class has been previously attempted -->
    <Card :class="CARD_COLORS[proposal.status!] + ' border-2 ' + (isSelected ? CARD_BORDER_COLORS[proposal.status!] : '!border-none')" @click="emits('card-clicked')" pt:content:class="!flex !flex-row justify-between">
        <template #title>{{proposal.name}}</template>
        <template #content>
            <p>{{ cityName }}, {{ toCapitalCaseFromAllCaps(proposal.type!) }}</p>
            <div class="w-20 flex flex-col items-center self-center">
                <i v-if="proposal.status! == 'APPROVED'" class="pi pi-check" />
                <i v-else-if="proposal.status! == 'REJECTED'" class="pi pi-times" />
                <i v-else class="pi pi-hourglass" />
                <span>{{ toCapitalCaseFromAllCaps(proposal.status!) }}</span>
            </div>
        </template>
    </Card>
</template>

<style scoped>

</style>