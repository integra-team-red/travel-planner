<script setup lang="ts">
import {toCamelCase} from "@/utils/text.utils.ts";
import {type FormField, FormFieldBuilder} from "@/utils/form.utils.ts";
import {type CityDTO} from "../../typescript-client";
import {useI18n} from "vue-i18n";
import {ref} from "vue";
import {cityApi} from "@/api.ts";
import type {FormSubmitEvent} from "@primevue/forms";
import router from "@/router";
import {type TripDetails, useTripDetailsStore} from "@/stores/trip-details.ts";

const { t } = useI18n();
const tripDetails = useTripDetailsStore();
const formKey = ref<number>(0);
const fields = ref<FormField[]>();
const cities: CityDTO[] = await cityApi.getCities();
const priceUpperBound = ref<string>();

initFormFields();

function initFormFields() {
    fields.value = [
        new FormFieldBuilder('Name')
            .required(t('formFieldError.fieldRequired'))
            .build(),
        new FormFieldBuilder('City')
            .required(t('formFieldError.fieldRequired'))
            .build(),
        new FormFieldBuilder('Duration')
            .required(t('formFieldError.fieldRequired'))
            .isNaturalNumber(t('formFieldError.fieldType', {type: t('number')}))
            .build(),
        new FormFieldBuilder('Price Lower Bound')
            .required(t('formFieldError.fieldRequired'))
            .isNaturalNumber(t('formFieldError.fieldType', {type: t('number')}))
            .lessThan(() => Number(priceUpperBound.value), t('formFieldError.fieldLength.max', {upperBound: priceUpperBound.value}))
            .build(),
        new FormFieldBuilder('Price Upper Bound')
            .required(t('formFieldError.fieldRequired'))
            .isNaturalNumber(t('formFieldError.fieldType', {type: t('number')}))
            .build(),
    ]
}

function onFormSubmit({values, valid}: FormSubmitEvent) {
    if (!valid) {
        return;
    }
    tripDetails.set(values as TripDetails);
    router.push('/trips/generated');
}

function resetForm() {
    ++formKey.value;
}
</script>
<template>
    <div class="bg-cover bg-[url(https://farm6.staticflickr.com/5831/22680635461_f7eed28057_o.jpg)]">
        <div class="backdrop-blur-xs p-8 sm:p-40 sm:px-60">
            <h1 class="justify-self-center pb-12 text-3xl font-semibold text-white">Create New Trip</h1>
            <Form :key="formKey" @submit="onFormSubmit" class="flex flex-col gap-4">
                <FormField v-for="{ name, resolver } in fields" :key="name" v-slot="$field" :resolver="resolver" class="flex flex-col">
                    <Select v-if="toCamelCase(name) == 'city'" :placeholder="t('fields.' + toCamelCase(name))" :id="name" :name="toCamelCase(name)" option-label="name" option-value="id" :options="cities" fluid />
                    <InputText v-else-if="toCamelCase(name) == 'priceUpperBound'" :placeholder="t('fields.' + toCamelCase(name))" :name="toCamelCase(name)" :id="name" maxlength="30" v-model="priceUpperBound" fluid />
                    <InputText v-else :placeholder="t('fields.' + toCamelCase(name))" :name="toCamelCase(name)" :id="name" maxlength="30" fluid />
                    <Message v-if="$field?.invalid" severity="error" size="small" variant="simple">
                        {{ $field.error?.message }}
                    </Message>
                </FormField>
                <div class="flex gap-8">
                    <Button severity="info" @click="resetForm" :label="t('clear')" fluid/>
                    <Button type="submit" severity="success" :label="t('confirm')" fluid/>
                </div>
            </Form>
        </div>
    </div>
</template>

<style scoped>

</style>
