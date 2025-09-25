<script setup lang="ts">
    import {reactive, ref} from 'vue';
    import type {CityDTO} from '../../typescript-client';
    import {cityApi} from '@/api.ts';
    import CityCard from '@/components/CityCard.vue';
    import {useI18n} from 'vue-i18n';
    import {useToast} from 'primevue';
    import type {FormSubmitEvent} from '@primevue/forms';
    import type {FormResolverOptions} from '@primevue/forms/form';
    import type {FormErrors} from '@/types/forms.types.ts';

    const {t} = useI18n();
    const toast = useToast();

    const inAddingMode = ref(false);

    const cities = ref<CityDTO[]>([]);
    const selectedCities = ref<number[]>([])

    const initialValues = reactive({
        name: ''
    });

    await fetchCities();

    async function fetchCities() {
        cities.value = await cityApi.getCities();
    }

    function select(city: CityDTO) {
        const cityAlreadySelected = selectedCities.value.includes(city.id!);
        if (cityAlreadySelected) {
            selectedCities.value = selectedCities.value.filter((cityId) => cityId !== city.id);
            return;
        }
        selectedCities.value.push(city.id!);
    }

    async function deleteCity() {
        for (const id of selectedCities.value) {
            await cityApi.deleteCity({id});
        }
        selectedCities.value = [];
        await fetchCities()
    }

    function resolver(submitEvent: FormResolverOptions) {
        const errors: FormErrors = {};

        if (submitEvent.values.name == "") {
            errors.name = [{message: t('field-is-required')}];
        }

        return {
            values: submitEvent.values,
            errors
        };
    }

    async function onFormSubmit(submitEvent: FormSubmitEvent) {
        if (submitEvent.valid) {
            await cityApi.addCity({cityDTO: submitEvent.values});
            toast.add({
                severity: 'success',
                summary: t('cities.added'),
                life: 3000
            });
            await fetchCities();
        }
    }

    function toggleInAddingMode() {
        inAddingMode.value = !inAddingMode.value
    }

</script>

<template>
    <div class="flex lg:flex-row flex-col place-content-between gap-8">
        <div class="lg:w-1/2 w-full">
            <h2 class="text-3xl">{{t('cities.header')}}</h2>

            <div v-for="city in cities" :key="city.id">
                <city-card :city="city"
                           :selected="selectedCities.includes(city.id!)"
                           @checkbox-clicked="select(city)"
                />
            </div>

            <div class="flex flex-row justify-between">
                <Button :label="t('delete')" class="w-3/7" severity="danger" @click="deleteCity"/>
                <Button :label="t('add')" class="w-3/7" @click="toggleInAddingMode"/>
            </div>
        </div>
        <div class="lg:w-1/2 w-full flex flex-col justify-start gap-8" v-if="inAddingMode">
            <h2 class="text-3xl">{{t('cities.add')}}</h2>

            <Form v-slot="$form" :initialValues="initialValues" :resolver="resolver" @submit="onFormSubmit"
                  class="flex flex-col gap-4">

                <div class="flex flex-col gap-1">
                    <InputText name="name" type="text" placeholder="Name" fluid/>
                    <Message v-if="$form.name?.invalid" severity="error" size="small" variant="simple">
                        {{$form.name.error?.message}}
                    </Message>
                </div>
                <div class="flex flex-row justify-between">
                    <Button severity="secondary" class="w-3/7" :label="t('cancel')" @click="toggleInAddingMode"/>
                    <Button type="submit" class="w-3/7" severity="primary" :label="t('confirm')"/>
                </div>
            </Form>
        </div>
    </div>
</template>

<style scoped>
</style>
