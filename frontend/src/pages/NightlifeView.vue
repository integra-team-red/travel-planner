<script setup lang="ts">
import {ref} from "vue";
import {cityApi, nightlifeApi} from "@/api.ts";
import {
    type CityDTO,
    type NightlifeDTO,
    NightlifeDTOTypeEnum
} from "../../typescript-client";
import type {FormSubmitEvent} from "@primevue/forms";
import {useConfirm} from "primevue";
import {promptConfirm} from "@/utils/confirm.utils.ts";
import {showToast} from "@/utils/toast.utils.ts";
import {useToast} from "primevue";
import {toCamelCase, toCapitalCaseFromAllCaps} from "@/utils/text.utils.ts";
import {useI18n} from "vue-i18n";
import {type FormField, FormFieldBuilder} from "@/utils/form.utils.ts";
import NightlifeCard from "@/components/NightlifeCard.vue";

const { t } = useI18n();
const confirm = useConfirm();
const toast = useToast();
const initialValues = ref<Record<string, unknown>>({});
const formKey = ref<number>(0);
const fields = ref<FormField[]>([]);
const selectedNightlifes = ref<NightlifeDTO[]>([]);
const cities: CityDTO[] = await cityApi.getCities();
const nightlifes = ref<NightlifeDTO[]>();
const priceUpperBound = ref<string>();
const NIGHTLIFE_TYPES: string[] = Object.values(NightlifeDTOTypeEnum);
initFormFields();
fetchNightlifes();

async function fetchNightlifes() {
    nightlifes.value = await nightlifeApi.getNightlifes();
}

function sendNightlifeCreationRequest(nightlife: NightlifeDTO) {
    nightlifeApi.addNightlife({nightlifeDTO: nightlife})
        .then(() => {
            showToast("success", t('nightlifes.add.success'), toast);
            fetchNightlifes();
        })
        .catch(() => {
            showToast("error", t('nightlifes.add.failure'), toast);
        });
}

function sendNightlifeUpdateRequest(nightlife: NightlifeDTO, updatedNightlife: NightlifeDTO) {
    nightlifeApi.updateNightlife({id: updatedNightlife.id!, nightlifeDTO: nightlife})
        .then(() => {
            showToast("success", t('nightlifes.update.success'), toast);
            fetchNightlifes();
            clearSelection();
        })
        .catch(() => {
            showToast("error", t('nightlifes.update.failure'), toast);
        });
}

function onFormSubmit({values, valid}: FormSubmitEvent) {
    if (!valid) {
        return;
    }
    const nightlife: NightlifeDTO = {cityId: values.city, ...values};
    const lastSelectedNightlife: NightlifeDTO | undefined = getLastSelectedNightlife();
    if (lastSelectedNightlife) {
        sendNightlifeUpdateRequest(nightlife, lastSelectedNightlife)
    }
    else {
        sendNightlifeCreationRequest(nightlife);
    }
}

function initFormFields() {
    fields.value = [
        new FormFieldBuilder('Name').required(t('formFieldError.fieldRequired')).build(),
        new FormFieldBuilder('City').required(t('formFieldError.fieldRequired')).build(),
        new FormFieldBuilder('Type').required(t('formFieldError.fieldRequired')).build(),
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

function getLastSelectedNightlife(): NightlifeDTO | undefined {
    return selectedNightlifes.value.length > 0 ? selectedNightlifes.value[selectedNightlifes.value.length - 1] : undefined;
}

function resetForm() {
    Object.keys(initialValues.value).forEach((key: string) => delete initialValues.value[key]);
    priceUpperBound.value = undefined;
    ++formKey.value;
}

function overwriteFormData(nightlife: NightlifeDTO) {
    resetForm();
    priceUpperBound.value = String(nightlife.priceUpperBound);
    for (const field of fields.value) {
        const camelCasedFieldName = toCamelCase(field.name);
        initialValues.value[camelCasedFieldName] = nightlife[(camelCasedFieldName == 'city' ? 'cityId' : camelCasedFieldName) as keyof NightlifeDTO];
    }
}

function onCardClick(nightlife: NightlifeDTO) {
    const nightlifeIndex = selectedNightlifes.value.findIndex(_nightlife => _nightlife.id == nightlife.id);
    if (nightlifeIndex >= 0) {
        selectedNightlifes.value.splice(nightlifeIndex, 1);
        if (nightlifeIndex == selectedNightlifes.value.length) {
            const lastNightlife = getLastSelectedNightlife();
            if (lastNightlife) {
                overwriteFormData(lastNightlife);
            }
            else {
                resetForm();
            }
        }
    }
    else {
        selectedNightlifes.value.push(nightlife);
        overwriteFormData(nightlife);
    }
}

function clearSelection() {
    resetForm();
    selectedNightlifes.value = [];
}

async function deleteSelection() {
    let errorOccurred = false;
    for (const nightlife of selectedNightlifes.value) {
        await nightlifeApi.deleteNightlife({id: nightlife.id!})
            .catch(() => errorOccurred = true);
        if (errorOccurred) {
            showToast("error", t('nightlifes.delete.failure'), toast);
            return;
        }
    }
    showToast("success", t('nightlifes.delete.success'), toast);
    clearSelection();
    fetchNightlifes();
}

</script>

<template>
    <div class="flex flex-col sm:flex-row gap-8">
        <div class="w-full sm:w-1/2 flex flex-col gap-4">
            <nightlife-card v-for="nightlife in nightlifes" :key="nightlife.id" :nightlife="nightlife" :isSelected="!!selectedNightlifes.find(_nightlife => _nightlife.id == nightlife.id)"
                      :cityName="cities.find(city => city.id == nightlife.cityId)!.name!"
                      @card-clicked="onCardClick(nightlife)"/>
            <div class="flex flex-row justify-between">
                <Button :label="t('delete')" class="w-3/7" severity="danger" @click="promptConfirm(confirm, deleteSelection, undefined)"/>
                <Button :label="t('clear')" class="w-3/7" severity="secondary" @click="clearSelection()"/>
            </div>
        </div>
        <div class="w-full sm:w-1/2 flex flex-col pl-8 border-l-2 gap-4">
            <h2 v-if="!getLastSelectedNightlife()" class="text-3xl">
                {{t('nightlifes.crudHeader.add')}}
            </h2>
            <h2 v-else class="text-3xl">
                {{t('nightlifes.crudHeader.update')}}
            </h2>
            <Form :initialValues :key="formKey" @submit="onFormSubmit" class="flex flex-col gap-4">
                <FormField v-for="{ name, resolver } in fields" :key="name" v-slot="$field" :resolver="resolver" class="flex flex-col">
                    <Select v-if="toCamelCase(name) == 'city'" :placeholder="t('fields.' + toCamelCase(name))" :id="name" :name="toCamelCase(name)" option-label="name" option-value="id" :options="cities" :model-value="initialValues[toCamelCase(name)]" fluid />
                    <Select v-else-if="toCamelCase(name) == 'type'" :placeholder="t('fields.' + toCamelCase(name))" :id="name" :name="toCamelCase(name)" :optionLabel="(entry) => toCapitalCaseFromAllCaps(entry)" :options="NIGHTLIFE_TYPES" />
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
