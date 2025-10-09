<script setup lang="ts">
import {reactive, ref} from "vue";
import {cityApi, proposalApi} from "@/api.ts";
import {
    type CityDTO,
    type ProposalDTO,
    ProposalDTOPoiTypeEnum,
    ProposalDTOTypeEnum
} from "../../typescript-client";
import type {FormResolverOptions} from "@primevue/forms/form";
import type {FormFieldResolverOptions, FormSubmitEvent} from "@primevue/forms";
import type {FormErrors} from "@/types/forms.types.ts";
import {useConfirm} from "primevue";
import {promptConfirm} from "@/utils/confirm.utils.ts";
import {showToast} from "@/utils/toast.utils.ts";
import {useToast} from "primevue";
import ProposalCard from "@/components/ProposalCard.vue";
import {toCamelCase, toCapitalCaseFromAllCaps} from "@/utils/text.utils.ts";
import {useI18n} from "vue-i18n";

const { t } = useI18n();
const confirm = useConfirm();
const toast = useToast();
const initialValues = reactive({});
const selectedType = ref<LOCATION_TYPES>();
const formKey = ref<number>(0);
const additionalFields = ref<Field[]>([]);
const selectedProposals = ref<ProposalDTO[]>([]);
const CITIES: CityDTO[] = await cityApi.getCities();
const PROPOSAL_TYPES: string[] = Object.values(ProposalDTOTypeEnum);
const POI_TYPES: string[] = Object.values(ProposalDTOPoiTypeEnum);
const proposals = ref<ProposalDTO[]>();
fetchProposals();

// NOTE(MC): Leaving this here for readability
enum LOCATION_TYPES {
    RESTAURANT = 'RESTAURANT',
    POI = 'POINT_OF_INTEREST'
}

interface Field {
    name: string,
    resolver: ({value}: FormFieldResolverOptions) => { value: {}, errors: {} };
}

async function fetchProposals() {
    proposals.value = await proposalApi.getAllProposals();
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

function onFormSubmit({values, valid}: FormSubmitEvent) {
    if (valid) {
        values.type = selectedType.value;
        const proposal: ProposalDTO = {cityId: values.city.id, poiType: values.pointOfInterestType, ...values};
        if (!getLastSelectedPendingProposal()) {
            proposalApi.createProposal({proposalDTO: proposal})
                .then(() => {
                    showToast("success", t('proposals.add.success'), toast);
                    fetchProposals();
                })
                .catch(() => {
                    showToast("error", t('proposals.add.failure'), toast);
                });
        }
        else {
            proposalApi.updateProposal({id: getLastSelectedPendingProposal()!.id!, proposalDTO: proposal})
                .then(() => {
                    showToast("success", t('proposals.update.success'), toast);
                    fetchProposals();
                    clearSelection();
                })
                .catch(() => {
                    showToast("error", t('proposals.update.failure.generic'), toast);
                })
        }
    }
}

// NOTE(MC): To get rid of these ugly field getters, start using resolvers like zod
function getRestaurantFields() {
    return [
        {
            name: 'Average Price',
            resolver: ({value}: FormFieldResolverOptions) => {
                const errors: FormErrors = {};
                if (!value || isNaN(Number(value))) {
                    errors.averagePrice = [{message: t('formFieldError.fieldType', {type: t('number')})}];
                }
                return {value, errors};
            }
        },
        {
            name: 'Cuisine Type',
            resolver: ({value}: FormFieldResolverOptions) => {
                const errors: FormErrors = {};
                if (!value || value.trim().length < 2) {
                    errors.cuisineType = [{message: t('formFieldError.fieldLength.min', {lowerBound: 2})}];
                }
                return {value, errors};
            }
        }
    ];
}

function getPoiFields() {
    return [
        {
            name: 'Price',
            resolver: ({value}: FormFieldResolverOptions) => {
                const errors: FormErrors = {};
                if (!value || isNaN(Number(value))) {
                    errors.price = [{message: t('formFieldError.fieldType', {type: t('number')})}];
                }
                return {value, errors};
            }
        },
        {
            name: 'Point of Interest Type',
            resolver: ({value}: FormFieldResolverOptions) => {
                const errors: FormErrors = {};
                if (!value) {
                    errors.pointOfInterestType = [{message: t('formFieldError.fieldRequired')}];
                }
                return {value, errors};
            }
        },
        {
            name: 'Description',
            resolver: ({value}: FormFieldResolverOptions) => {
                return {value, errors: {}}
            }
        }
    ];
}

// NOTE(MC): Maybe find a better way to do this; I didn't know how to iterate through type keys at runtime.
function changeAdditionalFields(value?: string) {
    debugger;
    switch (value) {
        case LOCATION_TYPES.RESTAURANT: {
            additionalFields.value = getRestaurantFields();
        } break;
        case LOCATION_TYPES.POI: {
            additionalFields.value = getPoiFields();
        } break;
        default: {
            additionalFields.value = [];
        }
    }
    ++formKey.value;
}

function getLastSelectedPendingProposal(): ProposalDTO | undefined {
    for (let i = selectedProposals.value.length - 1; i >= 0; --i) {
        let proposal = selectedProposals.value[i];
        if (proposal!.status == 'PENDING') {
            return proposal;
        }
    }
}

function resetForm() {
    Object.keys(initialValues).forEach(key => delete initialValues[key]);
    selectedType.value = undefined;
    changeAdditionalFields();
}

function overwriteFormData(proposal: ProposalDTO) {
    resetForm();
    changeAdditionalFields(proposal.type);
    initialValues.name = proposal.name;
    initialValues.type = proposal.type;
    selectedType.value = proposal.type as LOCATION_TYPES;
    initialValues.city = CITIES.find(city => city.id == proposal.cityId)!;
    for (let field of additionalFields.value) {
        let camelCasedFieldName = toCamelCase(field.name);
        initialValues[camelCasedFieldName] = proposal[camelCasedFieldName == 'pointOfInterestType' ? 'poiType' : camelCasedFieldName as keyof ProposalDTO];
    }
}

function onCardClick(proposal: ProposalDTO) {
    let proposalIndex = selectedProposals.value.findIndex(prop => prop.id == proposal.id);
    if (proposalIndex >= 0) {
        selectedProposals.value.splice(proposalIndex, 1);
        if (proposalIndex == selectedProposals.value.length) {
            let lastProposal = getLastSelectedPendingProposal();
            lastProposal ? overwriteFormData(lastProposal) : resetForm();
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
    for (let proposal of selectedProposals.value) {
        if (proposal.status == 'APPROVED') {
            showToast("error", t('proposals.delete.failure.generic') + t('proposals.delete.failure.disallowApproved'), toast);
            return;
        }

    }
    let errorOccurred = false;
    for (let proposal of selectedProposals.value) {
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
            <proposal-card v-for="proposal in proposals" :proposal="proposal" :isSelected="!!selectedProposals.find(prop => prop.id == proposal.id)"
                           :cityName="CITIES.find(city => city.id == proposal.cityId)!.name!"
                           @card-clicked="onCardClick(proposal)"/>
            <div class="flex flex-row justify-between">
                <Button :label="t('delete')" class="w-3/7" severity="danger" @click="promptConfirm(confirm, deleteSelection, undefined)"/>
                <Button :label="t('clear')" class="w-3/7" severity="info" @click="clearSelection()"/>
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
                <InputText placeholder="Name" name="name" maxlength="30" fluid />
                <Message v-if="$form.name?.invalid" severity="error" size="small" variant="simple">
                    {{ $form.name.error.message }}
                </Message>
                <Select placeholder="Type" v-model="selectedType" name="type" :optionLabel="(entry) => toCapitalCaseFromAllCaps(entry)" :options="PROPOSAL_TYPES" @update:model-value="changeAdditionalFields" fluid />
                <Message v-if="$form.type?.invalid" severity="error" size="small" variant="simple">
                    {{ $form.type.error.message }}
                </Message>
                <Select placeholder="City" name="city" optionLabel="name" :options="CITIES" fluid />
                <Message v-if="$form.city?.invalid" severity="error" size="small" variant="simple">
                    {{ $form.city.error.message }}
                </Message>
                <!-- NOTE(MC): Maybe instead of if else if branching, do this all generically? Good luck to the one up to that -->
                <div v-for="{ name, resolver } in additionalFields" :key="name">
                    <FormField v-slot="$field" :resolver="resolver" class="flex flex-col" v-if="toCamelCase(name) == 'description'">
                        <Textarea :placeholder="name" :name="toCamelCase(name)" rows="5" maxlength="250" />
                        <Message v-if="$field?.invalid" severity="error" size="small" variant="simple">
                            {{ $field.error?.message }}
                        </Message>
                    </FormField>
                    <FormField v-slot="$field" :resolver="resolver" v-else-if="toCamelCase(name) == 'pointOfInterestType'">
                        <Select :placeholder="name" :id="name" :name="toCamelCase(name)" :options="POI_TYPES" fluid />
                        <Message v-if="$field?.invalid" severity="error" size="small" variant="simple">
                            {{ $field.error?.message }}
                        </Message>
                    </FormField>
                    <FormField v-slot="$field" :resolver="resolver" v-else>
                        <InputText :placeholder="name" :name="toCamelCase(name)" :id="name" maxlength="30" fluid />
                        <Message v-if="$field?.invalid" severity="error" size="small" variant="simple">
                            {{ $field.error?.message }}
                        </Message>
                    </FormField>
                </div>

                <div class="flex justify-around">
                    <Button class="w-4/9" severity="info" @click="resetForm" :label="t('clear')"/>
                    <Button class="w-4/9" type="submit" severity="success" :label="t('confirm')"/>
                </div>
            </Form>
        </div>
    </div>
</template>

<style scoped>

</style>
