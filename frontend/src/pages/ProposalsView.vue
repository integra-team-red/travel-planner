<script setup lang="ts">
import {ref} from "vue";
import {cityApi, proposalApi} from "@/api.ts";
import {
    type CityDTO,
    type ProposalDTO,
    ProposalDTOPoiTypeEnum,
    ProposalDTOTypeEnum
} from "../../typescript-client";
import type {FormResolverOptions} from "@primevue/forms/form";
import type {FormSubmitEvent} from "@primevue/forms";
import type {FormErrors} from "@/types/forms.types.ts";
import {useConfirm} from "primevue";
import {promptConfirm} from "@/utils/confirm.utils.ts";
import {showToast} from "@/utils/toast.utils.ts";
import {useToast} from "primevue";
import ProposalCard from "@/components/ProposalCard.vue";
import {toCamelCase, toCapitalCaseFromAllCaps} from "@/utils/text.utils.ts";
import {useI18n} from "vue-i18n";
import {type FormField, FormFieldBuilder} from "@/utils/form.utils.ts";

const { t } = useI18n();
const confirm = useConfirm();
const toast = useToast();
const initialValues = ref<Record<string, unknown>>({});
const selectedType = ref<ProposalDTOPoiTypeEnum>();
const formKey = ref<number>(0);
const additionalFields = ref<FormField[]>([]);
const selectedProposals = ref<ProposalDTO[]>([]);
const cities: CityDTO[] = await cityApi.getCities();
const PROPOSAL_TYPES: string[] = Object.values(ProposalDTOTypeEnum);
const POI_TYPES: string[] = Object.values(ProposalDTOPoiTypeEnum);
const proposals = ref<ProposalDTO[]>();
fetchProposals();

async function fetchProposals() {
    proposals.value = await proposalApi.getProposals();
}

function formResolver({values}: FormResolverOptions) {
    const errors: FormErrors = {};

    if (!values.name || values.name.trim().length < 2) {
        errors.name = [{message: t('formFieldError.fieldLength.min', {lowerBound: 2})}];
    }
    if (!selectedType.value) {
        errors.type = [{message: t('formFieldError.fieldRequired')}];
    }
    if (!values.city) {
        errors.city = [{message: t('formFieldError.fieldRequired')}];
    }

    return {values, errors};
}

function sendProposalCreationRequest(proposal: ProposalDTO) {
    proposalApi.addProposal({proposalDTO: proposal})
        .then(() => {
            showToast("success", t('proposals.add.success'), toast);
            fetchProposals();
        })
        .catch(() => {
            showToast("error", t('proposals.add.failure'), toast);
        });
}

function sendProposalUpdateRequest(proposal: ProposalDTO, updatedProposal: ProposalDTO) {
    proposalApi.updateProposal({id: updatedProposal.id!, proposalDTO: proposal})
        .then(() => {
            showToast("success", t('proposals.update.success'), toast);
            fetchProposals();
            clearSelection();
        })
        .catch(() => {
            showToast("error", t('proposals.update.failure.generic'), toast);
        });
}

function onFormSubmit({values, valid}: FormSubmitEvent) {
    if (!valid) {
        return;
    }
    values.type = selectedType.value;
    const proposal: ProposalDTO = {cityId: values.city.id, poiType: values.pointOfInterestType, ...values};
    const lastPendingProposal: ProposalDTO | undefined = getLastSelectedPendingProposal();
    if (lastPendingProposal) {
        sendProposalUpdateRequest(proposal, lastPendingProposal)
    }
    else {
        sendProposalCreationRequest(proposal);
    }
}

function getRestaurantFields() {
    return [
        new FormFieldBuilder('Average Price').isNaturalNumber(t('formFieldError.fieldType')).build(),
        new FormFieldBuilder('Cuisine Type').minLength(2, t('formFieldError.fieldLength.min')).build()
    ];
}

function getPoiFields() {
    return [
        new FormFieldBuilder('Price').isNaturalNumber(t('formFieldError.fieldType')).build(),
        new FormFieldBuilder('Point Of Interest Type').required(t('formFieldError.fieldRequired')).build(),
        new FormFieldBuilder('Image').build(),
        new FormFieldBuilder('Address').build(),
        new FormFieldBuilder('Description').build()
    ];
}

function changeAdditionalFields(value?: string) {
    switch (value) {
        case 'RESTAURANT': {
            additionalFields.value = getRestaurantFields();
        } break;
        case 'POINT_OF_INTEREST': {
            additionalFields.value = getPoiFields();
        } break;
        default: {
            additionalFields.value = [];
        }
    }
    ++formKey.value;
}

function getLastSelectedPendingProposal(): ProposalDTO | undefined {
    return selectedProposals.value.slice().reverse().find((proposal: ProposalDTO) => proposal.status == 'PENDING');
}

function resetForm() {
    Object.keys(initialValues).forEach((key: string) => delete initialValues.value[key]);
    selectedType.value = undefined;
    changeAdditionalFields();
}

function overwriteFormData(proposal: ProposalDTO) {
    resetForm();
    changeAdditionalFields(proposal.type);
    initialValues.value.name = proposal.name;
    initialValues.value.type = proposal.type;
    selectedType.value = proposal.type as ProposalDTOPoiTypeEnum;
    initialValues.value.city = cities.find(city => city.id == proposal.cityId)!;
    for (const field of additionalFields.value) {
        const camelCasedFieldName = toCamelCase(field.name);
        // @ts-expect-error -- might just be anything here
        initialValues[camelCasedFieldName] = proposal[camelCasedFieldName == 'pointOfInterestType' ? 'poiType' : camelCasedFieldName as keyof ProposalDTO];
    }
}

function onCardClick(proposal: ProposalDTO) {
    const proposalIndex = selectedProposals.value.findIndex(prop => prop.id == proposal.id);
    if (proposalIndex >= 0) {
        selectedProposals.value.splice(proposalIndex, 1);
        if (proposalIndex == selectedProposals.value.length) {
            const lastProposal = getLastSelectedPendingProposal();
            if (lastProposal) {
                overwriteFormData(lastProposal);
            }
            else {
                resetForm();
            }
        }
    }
    else {
        selectedProposals.value.push(proposal);
        if (proposal.status == 'PENDING') {
            overwriteFormData(proposal);
        }
    }
}

function clearSelection() {
    resetForm();
    selectedProposals.value = [];
}

async function deleteSelection() {
    for (const proposal of selectedProposals.value) {
        if (proposal.status == 'APPROVED') {
            showToast("error", t('proposals.delete.failure.generic') + t('proposals.delete.failure.disallowApproved'), toast);
            return;
        }

    }
    let errorOccurred = false;
    for (const proposal of selectedProposals.value) {
        await proposalApi.deleteProposal({id: proposal.id!})
            .catch(() => errorOccurred = true);
        if (errorOccurred) {
            showToast("error", t('proposals.delete.failure.generic'), toast);
            return;
        }
    }
    showToast("success", t('proposals.delete.success'), toast);
    clearSelection();
    fetchProposals();
}

</script>

<template>
    <div class="flex flex-col sm:flex-row gap-8">
        <div class="w-full sm:w-1/2 flex flex-col gap-4">
            <proposal-card v-for="proposal in proposals" :key="proposal.id" :proposal="proposal" :isSelected="!!selectedProposals.find(prop => prop.id == proposal.id)"
                           :cityName="cities.find(city => city.id == proposal.cityId)!.name!"
                           @card-clicked="onCardClick(proposal)"/>
            <div class="flex flex-row justify-between">
                <Button :label="t('delete')" class="w-3/7" severity="danger" @click="promptConfirm(confirm, deleteSelection, undefined)"/>
                <Button :label="t('clear')" class="w-3/7" severity="secondary" @click="clearSelection()"/>
            </div>
        </div>
        <div class="w-full sm:w-1/2 flex flex-col pl-8 border-l-2 gap-4">
            <h2 v-if="!getLastSelectedPendingProposal()" class="text-3xl">
                {{t('proposals.crudHeader.add')}}
            </h2>
            <h2 v-else class="text-3xl">
                {{t('proposals.crudHeader.update')}}
            </h2>
            <Form :initialValues :key="formKey" v-slot="$form" :resolver="formResolver" @submit="onFormSubmit" class="flex flex-col gap-4">
                <InputText :placeholder="t('fields.name')" name="name" maxlength="30" fluid />
                <Message v-if="$form.name?.invalid" severity="error" size="small" variant="simple">
                    {{ $form.name.error.message }}
                </Message>
                <Select :placeholder="t('fields.type')" v-model="selectedType" name="type" :optionLabel="(entry) => toCapitalCaseFromAllCaps(entry)" :options="PROPOSAL_TYPES" @update:model-value="changeAdditionalFields" fluid />
                <Message v-if="$form.type?.invalid" severity="error" size="small" variant="simple">
                    {{ $form.type.error.message }}
                </Message>
                <Select :placeholder="t('fields.city')" name="city" optionLabel="name" :options="cities" fluid />
                <Message v-if="$form.city?.invalid" severity="error" size="small" variant="simple">
                    {{ $form.city.error.message }}
                </Message>
                <!-- NOTE(MC): Maybe instead of if else if branching, do this all generically? Good luck to the one up to that -->
                <div v-for="{ name, resolver } in additionalFields" :key="name">
                    <FormField v-slot="$field" :resolver="resolver" class="flex flex-col">
                        <Textarea v-if="toCamelCase(name) == 'description'" :placeholder="t('fields.' + toCamelCase(name))" :name="toCamelCase(name)" rows="5" maxlength="250" />
                        <Select v-else-if="toCamelCase(name) == 'pointOfInterestType'" :placeholder="t('fields.' + toCamelCase(name))" :id="name" :name="toCamelCase(name)" :options="POI_TYPES" fluid />
                        <InputText v-else :placeholder="t('fields.' + toCamelCase(name))" :name="toCamelCase(name)" :id="name" maxlength="30" fluid />
                        <Message v-if="$field?.invalid" severity="error" size="small" variant="simple">
                            {{ $field.error?.message }}
                        </Message>
                    </FormField>
                </div>

                <div class="flex justify-between gap-2">
                    <Button severity="secondary" @click="resetForm" :label="t('clear')" fluid/>
                    <Button type="submit" severity="success" :label="t('confirm')" fluid/>
                </div>
            </Form>
        </div>
    </div>
</template>

<style scoped>

</style>
