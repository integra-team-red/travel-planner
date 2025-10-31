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
        .then((createdProposal) => {
            showToast("success", t('proposals.add.success'), toast);
            if (proposals.value) {
                proposals.value.push(createdProposal);
            } else {
                proposals.value = [createdProposal];
            }
        })
        .catch(() => {
            showToast("error", t('proposals.add.failure'), toast);
        });
}


function sendProposalUpdateRequest(proposal: ProposalDTO, updatedProposal: ProposalDTO) {
    proposalApi.updateProposal({id: updatedProposal.id!, proposalDTO: proposal})
        .then((savedProposal) => {
            showToast("success", t('proposals.update.success'), toast);
            const index = proposals.value?.findIndex(p => p.id === updatedProposal.id);
            if (index !== undefined && index !== -1 && proposals.value) {
                proposals.value[index] = savedProposal;
            }
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
    const proposal: ProposalDTO = {
        id: undefined,
        name: values.name?.trim(),
        type: selectedType.value as ProposalDTOTypeEnum,
        cityId: (values.city as CityDTO)?.id,
        poiType: values.pointOfInterestType as ProposalDTOPoiTypeEnum,
        description: values.description ?? undefined,
        address: values.address ?? undefined,
        price: values.price ? Number(values.price) : undefined,
        averagePrice: values.averagePrice ? Number(values.averagePrice) : undefined,
        cuisineType: values.cuisineType ?? undefined,
        image: values.image ?? undefined,
    };

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
        new FormFieldBuilder('Address').required(t('formFieldError.fieldRequired')).build(),
        new FormFieldBuilder('Description').required(t('formFieldError.fieldRequired')).build()
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
}

function getLastSelectedPendingProposal(): ProposalDTO | undefined {
    return selectedProposals.value.slice().reverse().find((proposal: ProposalDTO) => proposal.status == 'PENDING');
}

function resetForm() {
    Object.keys(initialValues.value).forEach((key: string) => delete initialValues.value[key]);
    selectedType.value = undefined;
    changeAdditionalFields();
}

function overwriteFormData(proposal: ProposalDTO) {
    resetForm();
    changeAdditionalFields(proposal.type);
    initialValues.value = {
        name: proposal.name,
        type: proposal.type,
        city: cities.find(city => city.id === proposal.cityId),
        description: proposal.description,
        address: proposal.address,
        price: proposal.price,
        averagePrice: proposal.averagePrice,
        cuisineType: proposal.cuisineType,
        pointOfInterestType: proposal.poiType,
        image: proposal.image
    };
    selectedType.value = proposal.type as ProposalDTOPoiTypeEnum;
    formKey.value++;
}


function onCardClick(proposal: ProposalDTO) {
    if (selectedProposals.value.find(p => p.id === proposal.id)) {
        selectedProposals.value = selectedProposals.value.filter(p => p.id !== proposal.id);
        resetForm();
        return;
    }
    selectedProposals.value = [proposal];
    overwriteFormData(proposal);
}


function clearSelection() {
    initialValues.value = {};
    selectedType.value = undefined;
    changeAdditionalFields();
    formKey.value++;
}

async function deleteSelection() {
    if (selectedProposals.value.some(p => p.status === 'APPROVED')) {
        showToast("error", t('proposals.delete.failure.generic') + t('proposals.delete.failure.disallowApproved'), toast);
        return;
    }

    let errorOccurred = false;

    for (const proposal of selectedProposals.value) {
        try {
            await proposalApi.deleteProposal({ id: proposal.id! });
        } catch (e) {
            console.error("Delete failed for proposal:", proposal.id, e);
            errorOccurred = true;
        }
    }

    proposals.value = proposals.value?.filter(
        (p) => !selectedProposals.value.some(sel => sel.id === p.id)
    );

    if (errorOccurred) {
        showToast("warn", t('proposals.delete.partial'), toast);
    } else {
        showToast("success", t('proposals.delete.success'), toast);
    }

    clearSelection();
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
            <Form :initialValues="initialValues" :key="formKey" v-slot="$form" :resolver="formResolver" @submit="onFormSubmit" class="flex flex-col gap-4">
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
                    <Button type="submit" severity="success" :label="t('confirm')" fluid/>
                </div>
            </Form>
        </div>
    </div>
</template>

<style scoped>

</style>
