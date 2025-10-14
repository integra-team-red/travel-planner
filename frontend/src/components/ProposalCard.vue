<script setup lang="ts">
import type {ProposalDTO} from "../../typescript-client";
import {toCapitalCaseFromAllCaps} from "@/utils/text.utils.ts";

defineProps<{
    proposal: ProposalDTO,
    cityName: string
    isSelected: boolean;
}>();

const emits = defineEmits(["card-clicked"]);

function getCardClasses(proposal: ProposalDTO, isSelected: boolean) {
    return [
        {
            pending: proposal.status === 'PENDING',
            approved: proposal.status === 'APPROVED',
            rejected: proposal.status === 'REJECTED',
        },
        isSelected ? 'border-2' : '!border-none',
    ];
}
</script>

<template>
    <!-- NOTE(MC): If you find a way to add shadows to cards then please do so. Overriding pt:root:class, :class has been previously attempted -->
    <Card :class="getCardClasses(proposal, isSelected)" @click="emits('card-clicked')" pt:content:class="!flex !flex-row justify-between">
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
.pending {
    background-color: #EDF7FC!important;
    border-color: #ADCDE2!important;
}
.approved {
    background-color: #DDEDE3!important;
    border-color: #22C55E!important;
}
.rejected {
    background-color: #FAE3E3!important;
    border-color: #EF4444!important;
}
</style>