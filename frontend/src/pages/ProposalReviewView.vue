<script setup lang="ts">
import {ref} from "vue";
import {cityApi, proposalReviewApi} from "@/api.ts";
import {
    type CityDTO,
    type ProposalDTO
} from "../../typescript-client";
import {showToast} from "@/utils/toast.utils.ts";
import {useToast} from "primevue";
import ProposalCard from "@/components/ProposalCard.vue";
import {toCamelCase} from "@/utils/text.utils.ts";
import {useI18n} from "vue-i18n";

const { t } = useI18n();
const toast = useToast();
const fields = ref<string[]>();
const selectedProposal = ref<ProposalDTO>();
const proposals = ref<ProposalDTO[]>(await proposalReviewApi.getPendingApprovals());
const CITIES: CityDTO[] = await cityApi.getCities();
const fieldsRenderedAsDropdownsInCamelCase = ['pointOfInterestType', 'type', 'cityId'];
const fieldsRenderedAsTextboxesInCamelCase = ['description'];

function resetSelectedProposal() {
    selectedProposal.value = undefined;
    fields.value = undefined;
}

function discardSelectedProposal() {
    const idOfProcessedProposal: number = selectedProposal.value!.id!;
    proposals.value = proposals.value.filter(proposal => proposal.id != idOfProcessedProposal);
    resetSelectedProposal();
}

function onCardClick(proposal: ProposalDTO) {
    if (selectedProposal.value?.id == proposal.id) {
        resetSelectedProposal();
        return;
    }
    selectedProposal.value = proposal;
    fields.value = Object.keys(selectedProposal.value).filter(key => selectedProposal.value![key as keyof ProposalDTO] && key != 'id');
}

function rejectProposal() {
    proposalReviewApi.rejectProposal({id: selectedProposal.value!.id!})
        .then(() => {
            showToast("success", t('proposals.review.reject.success') , toast);
            discardSelectedProposal();
        })
        .catch(() => {
            showToast("error", 'proposals.review.reject.failure', toast);
        });
}

function approveProposal() {
    proposalReviewApi.approveProposal({id: selectedProposal.value!.id!})
        .then(() => {
            showToast("success", t('proposals.review.approve.success'), toast);
            discardSelectedProposal();
        })
        .catch(() => {
            showToast("error", t('proposals.review.approve.failure'), toast);
        });
}

</script>

<template>
    <div class="flex flex-col sm:flex-row gap-8">
        <div class="w-full sm:w-1/2 flex flex-col gap-4">
            <proposal-card v-for="proposal in proposals" :proposal="proposal"
                           :isSelected="selectedProposal?.id == proposal.id"
                           :cityName="CITIES.find(city => city.id == proposal.cityId)!.name!"
                           @card-clicked="onCardClick(proposal)"/>
        </div>
        <div class="w-full sm:w-1/2 flex flex-col pl-8 border-l-2 gap-4">
            <h2 class="text-3xl">
                {{t('proposals.review.header')}}
            </h2>
            <div class="flex flex-col gap-4">
                <div v-for="fieldName in fields">
                    <Textarea disabled v-if="fieldsRenderedAsTextboxesInCamelCase.includes(toCamelCase(fieldName))"
                              :name="toCamelCase(fieldName)"
                              :placeholder="selectedProposal![fieldName as keyof ProposalDTO]?.toString()"
                              rows="5" fluid/>
                    <Select disabled v-else-if="fieldsRenderedAsDropdownsInCamelCase.includes(toCamelCase(fieldName))"
                            :placeholder="selectedProposal![fieldName as keyof ProposalDTO]?.toString()" fluid/>
                    <InputText disabled v-else
                               :placeholder="selectedProposal![fieldName as keyof ProposalDTO]?.toString()" fluid/>
                </div>
                <div v-if="selectedProposal" class="flex justify-around">
                    <Button class="w-4/9" severity="danger" @click="rejectProposal" label="Reject"/>
                    <Button class="w-4/9" severity="success" @click="approveProposal" label="Confirm"/>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>

</style>
