<script setup lang="ts">
import {ref} from "vue";
import {cityApi, spaApi} from "@/api.ts";
import {
    type CityDTO,
    type SpaDTO
} from "../../typescript-client";
import type {FormSubmitEvent} from "@primevue/forms";
import {useConfirm} from "primevue";
import {promptConfirm} from "@/utils/confirm.utils.ts";
import {showToast} from "@/utils/toast.utils.ts";
import {useToast} from "primevue";
import {toCamelCase} from "@/utils/text.utils.ts";
import {useI18n} from "vue-i18n";
import {type FormField, FormFieldBuilder} from "@/utils/form.utils.ts";
import SpaCard from "@/components/SpaCard.vue";

const { t } = useI18n();
const confirm = useConfirm();
const toast = useToast();
const initialValues = ref<Record<string, unknown>>({});
const formKey = ref<number>(0);
const fields = ref<FormField[]>([]);
const selectedSpas = ref<SpaDTO[]>([]);
const cities: CityDTO[] = await cityApi.getCities();
const spas = ref<SpaDTO[]>();
const priceUpperBound = ref<string>();
initFormFields();
fetchSpas();

async function fetchSpas() {
    spas.value = await spaApi.getSpas();
}

function sendSpaCreationRequest(spa: SpaDTO) {
    spaApi.addSpa({spaDTO: spa})
        .then(() => {
            showToast("success", t('spas.add.success'), toast);
            fetchSpas();
        })
        .catch(() => {
            showToast("error", t('spas.add.failure'), toast);
        });
}

function sendSpaUpdateRequest(spa: SpaDTO, updatedSpa: SpaDTO) {
    spaApi.updateSpa({id: updatedSpa.id!, spaDTO: spa})
        .then(() => {
            showToast("success", t('spas.update.success'), toast);
            fetchSpas();
            clearSelection();
        })
        .catch(() => {
            showToast("error", t('spas.update.failure'), toast);
        });
}

function onFormSubmit({values, valid}: FormSubmitEvent) {
    if (!valid) {
        return;
    }
    const spa: SpaDTO = {cityId: values.city, ...values};
    const lastSelectedSpa: SpaDTO | undefined = getLastSelectedSpa();
    if (lastSelectedSpa) {
        sendSpaUpdateRequest(spa, lastSelectedSpa)
    }
    else {
        sendSpaCreationRequest(spa);
    }
}

function initFormFields() {
    fields.value = [
        new FormFieldBuilder('Name').required(t('formFieldError.fieldRequired')).build(),
        new FormFieldBuilder('City').required(t('formFieldError.fieldRequired')).build(),
        new FormFieldBuilder('Schedule')
            .required(t('formFieldError.fieldRequired'))
            .matches('[0-9]{2}-[0-9]{2}', t('formFieldError.improperFormat')).build(),
        new FormFieldBuilder('Price Lower Bound')
            .required(t('formFieldError.fieldRequired'))
            .isNaturalNumber(t('formFieldError.fieldType', {type: t('number')}))
            .lessThan(() => Number(priceUpperBound.value), t('formFieldError.fieldLength.max', {upperBound: priceUpperBound.value})).build(),
        new FormFieldBuilder('Price Upper Bound')
            .required(t('formFieldError.fieldRequired'))
            .isNaturalNumber(t('formFieldError.fieldType', {type: t('number')})).build(),
        new FormFieldBuilder('Rating')
            .required(t('formFieldError.fieldRequired'))
            .isNaturalNumber(t('formFieldError.fieldRequired')).build(),
        new FormFieldBuilder('Description').build(),
    ];
}

function getLastSelectedSpa(): SpaDTO | undefined {
    return selectedSpas.value.length > 0 ? selectedSpas.value[selectedSpas.value.length - 1] : undefined;
}

function resetForm() {
    Object.keys(initialValues.value).forEach((key: string) => delete initialValues.value[key]);
    ++formKey.value;
}

function overwriteFormData(spa: SpaDTO) {
    resetForm();
    for (const field of fields.value) {
        const camelCasedFieldName = toCamelCase(field.name);
        initialValues.value[camelCasedFieldName] = spa[(camelCasedFieldName == 'city' ? 'cityId' : camelCasedFieldName) as keyof SpaDTO];
    }
}

function onCardClick(spa: SpaDTO) {
    const spaIndex = selectedSpas.value.findIndex(_spa => _spa.id == spa.id);
    if (spaIndex >= 0) {
        selectedSpas.value.splice(spaIndex, 1);
        if (spaIndex == selectedSpas.value.length) {
            const lastSpa = getLastSelectedSpa();
            if (lastSpa) {
                overwriteFormData(lastSpa);
            }
            else {
                resetForm();
            }
        }
    }
    else {
        selectedSpas.value.push(spa);
        overwriteFormData(spa);
    }
}

function clearSelection() {
    resetForm();
    selectedSpas.value = [];
}

async function deleteSelection() {
    let errorOccurred = false;
    for (const spa of selectedSpas.value) {
        await spaApi.deleteSpa({id: spa.id!})
            .catch(() => errorOccurred = true);
        if (errorOccurred) {
            showToast("error", t('spas.delete.failure'), toast);
            return;
        }
    }
    showToast("success", t('spas.delete.success'), toast);
    clearSelection();
    fetchSpas();
}

</script>

<template>
    <div class="flex flex-col sm:flex-row gap-8">
        <div class="w-full sm:w-1/2 flex flex-col gap-4">
            <spa-card v-for="spa in spas" :key="spa.id" :spa="spa" :isSelected="!!selectedSpas.find(_spa => _spa.id == spa.id)"
                           :cityName="cities.find(city => city.id == spa.cityId)!.name!"
                           @card-clicked="onCardClick(spa)"/>
            <div class="flex flex-row justify-between">
                <Button :label="t('delete')" class="w-3/7" severity="danger" @click="promptConfirm(confirm, deleteSelection, undefined)"/>
                <Button :label="t('clear')" class="w-3/7" severity="secondary" @click="clearSelection()"/>
            </div>
        </div>
        <div class="w-full sm:w-1/2 flex flex-col pl-8 border-l-2 gap-4">
            <h2 v-if="!getLastSelectedSpa()" class="text-3xl">
                {{t('spas.crudHeader.add')}}
            </h2>
            <h2 v-else class="text-3xl">
                {{t('spas.crudHeader.update')}}
            </h2>
            <Form :initialValues :key="formKey" @submit="onFormSubmit" class="flex flex-col gap-4">
                <FormField v-for="{ name, resolver } in fields" :key="name" v-slot="$field" :resolver="resolver" class="flex flex-col">
                    <Select v-if="toCamelCase(name) == 'city'" :placeholder="t('fields.' + toCamelCase(name))" :id="name" :name="toCamelCase(name)" option-label="name" option-value="id" :options="cities" :model-value="initialValues[toCamelCase(name)]" fluid />
                    <InputText v-else-if="toCamelCase(name) == 'priceUpperBound'" :placeholder="t('fields.' + toCamelCase(name))" :name="toCamelCase(name)" :id="name" maxlength="30" v-model="priceUpperBound" fluid />
                    <InputText v-else :placeholder="t('fields.' + toCamelCase(name))" :name="toCamelCase(name)" :id="name" maxlength="30" fluid />
                    <Message v-if="$field?.invalid" severity="error" size="small" variant="simple">
                        {{ $field.error?.message }}
                    </Message>
                </FormField>
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
