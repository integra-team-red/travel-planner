<script setup lang="ts">
    import {ref} from 'vue';
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

    enum EditMode {
        NONE,
        ADD,
        UPDATE
    }
    const inEditingMode = ref<EditMode>(EditMode.NONE);

    const cities = ref<CityDTO[]>([]);
    const selectedCities = ref<number[]>([])
    const currentCity = ref<number>();

    const formKey = ref<number>(0);

    const initialValues = ref<CityDTO>({
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

    function onCardClick(city: CityDTO) {
        inEditingMode.value = EditMode.UPDATE;
        initialValues.value = city;
        currentCity.value = city.id;
        formKey.value++;
    }

    function onAddClick(){
        inEditingMode.value = EditMode.ADD;
        initialValues.value = {};
        formKey.value++;
    }

    function resolver(submitEvent: FormResolverOptions) {
        const errors: FormErrors = {};

        if (submitEvent.values.name === "" || submitEvent.values.name === null || submitEvent.values.name === undefined) {
            errors.name = [{message: t('field-is-required')}];
        }

        return {
            values: submitEvent.values,
            errors
        };
    }

    async function onFormSubmit(submitEvent: FormSubmitEvent) {
        if (submitEvent.valid) {
            if(inEditingMode.value==EditMode.ADD) {
                await cityApi.addCity({cityDTO: submitEvent.values});
                toast.add({
                    severity: 'success',
                    summary: t('cities.added'),
                    life: 3000
                });
                await fetchCities();
            } else if (inEditingMode.value==EditMode.UPDATE){
                await cityApi.updateCity({cityDTO: submitEvent.values, id: currentCity.value ?? 0});
                toast.add({
                    severity: 'success',
                    summary: t('cities.added'),
                    life: 3000
                });
                await fetchCities();
            }
        }
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
                           @card-clicked="onCardClick(city)"
                />
            </div>

            <div class="flex flex-row justify-between">
                <Button :label="t('delete')" class="w-3/7" severity="danger" @click="deleteCity"/>
                <Button :label="t('add')" class="w-3/7" @click="onAddClick"/>
            </div>
        </div>
        <div class="lg:w-1/2 w-full flex flex-col justify-start gap-8" v-if="inEditingMode!=EditMode.NONE">
            <h2 class="text-3xl" v-if="inEditingMode == EditMode.ADD">{{t('cities.add')}}</h2>
            <h2 class="text-3xl" v-if="inEditingMode == EditMode.UPDATE">{{t('cities.update')}}</h2>

            <Form :key="formKey" v-slot="$form" :initialValues="initialValues" :resolver="resolver" @submit="onFormSubmit"
                  class="flex flex-col gap-4">

                <div class="flex flex-col gap-1">
                    <InputText name="name" type="text" placeholder="Name" fluid/>
                    <Message v-if="$form.name?.invalid" severity="error" size="small" variant="simple">
                        {{$form.name.error?.message}}
                    </Message>
                </div>
                <div class="flex flex-row justify-between">
                    <Button severity="secondary" class="w-3/7" :label="t('cancel')" @click="inEditingMode=EditMode.NONE"/>
                    <Button type="submit" class="w-3/7" severity="primary" :label="t('confirm')"/>
                </div>
            </Form>
        </div>
    </div>
</template>

<style scoped>
</style>
