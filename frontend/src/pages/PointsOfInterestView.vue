<script setup lang="ts">
    import {ref} from 'vue';
    import type {POIDTO, CityDTO} from '../../typescript-client';
    import {poiApi, cityApi} from '@/api.ts';
    import PointOfInterestCard from '@/components/PointOfInterestCard.vue';
    import {useI18n} from 'vue-i18n';
    import {useToast} from 'primevue';
    import {showToast} from '@/utils/toast.utils.ts'
    import type {FormSubmitEvent} from '@primevue/forms';
    import type {FormResolverOptions} from '@primevue/forms/form';
    import type {FormErrors} from '@/types/forms.types.ts';
    import Select from 'primevue/select';


    const {t} = useI18n();
    const toast = useToast();

    enum EditMode {
        NONE,
        ADD,
        UPDATE
    }
    const inEditingMode = ref<EditMode>(EditMode.NONE);

    const pois = ref<POIDTO[]>([]);
    const cities = ref<CityDTO[]>([]);
    const selectedPOIs = ref<number[]>([])
    const currentPOI = ref<number>();

    const initialValues = ref<POIDTO>({
        name: '',
        description: '',
        cityId: 0,
        price: 0,
        type: ''
    });

    const types = ref<string[]>([]);
    const formKey = ref<number>(0);

    await fetchPOIs();
    await fetchCities();
    await fetchTypes();

    async function fetchPOIs() {
        pois.value = await poiApi.getPointsOfInterest();
    }

    async function fetchCities() {
        cities.value = await cityApi.getCities();
    }

    async function fetchTypes() {
        types.value = await poiApi.getAllPointsOfInterestTypes();
    }

    function select(poi: POIDTO) {
        const poiAlreadySelected = selectedPOIs.value.includes(poi.id!);
        if (poiAlreadySelected) {
            selectedPOIs.value = selectedPOIs.value.filter((poiId) => poiId !== poi.id);
            return;
        }
        selectedPOIs.value.push(poi.id!);
    }

    async function deletePOI() {
        for (const id of selectedPOIs.value) {
            await poiApi.deletePointOfInterest({id});
        }
        showToast('success', t('poi.deleted'), toast);
        selectedPOIs.value = [];
        await fetchPOIs()
        inEditingMode.value = EditMode.NONE;
        initialValues.value = {};
        formKey.value++;
    }

    function onCardClick(poi: POIDTO) {
        inEditingMode.value = EditMode.UPDATE;
        initialValues.value = poi;
        currentPOI.value = poi.id;
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
        if (submitEvent.values.description === "" || submitEvent.values.description === null || submitEvent.values.description === undefined) {
            errors.description = [{message: t('field-is-required')}];
        }
        if (submitEvent.values.cityId === "" || submitEvent.values.cityId === null || submitEvent.values.cityId === undefined) {
            errors.cityId = [{message: t('field-is-required')}];
        }
        if (submitEvent.values.price === "" || submitEvent.values.price === null || submitEvent.values.price === undefined) {
            errors.price = [{message: t('field-is-required')}];
        }
        if (submitEvent.values.type === "" || submitEvent.values.type === null || submitEvent.values.type === undefined) {
            errors.type = [{message: t('field-is-required')}];
        }

        return {
            values: submitEvent.values,
            errors
        };
    }

    async function onFormSubmit(submitEvent: FormSubmitEvent) {
        if (submitEvent.valid) {
            if(inEditingMode.value==EditMode.ADD) {
                await poiApi.addPointOfInterest({pOIDTO: submitEvent.values});
                showToast('success', t('poi.added'), toast);
                await fetchPOIs();
            } else if (inEditingMode.value==EditMode.UPDATE){
                await poiApi.updatePointOfInterest({pOIDTO: submitEvent.values, id: currentPOI.value ?? 0});
                showToast('success', t('poi.updated'), toast);
                await fetchPOIs();
            }
            inEditingMode.value = EditMode.NONE;
        }
    }

</script>

<template>
    <div class="flex lg:flex-row flex-col place-content-between gap-8">
        <div class="lg:w-1/2 w-full">
            <h2 class="text-3xl">{{t('poi.header')}}</h2>

            <div v-for="poi in pois" :key="poi.id" >
                <point-of-interest-card :poi="poi"
                           :selected="selectedPOIs.includes(poi.id!)"
                           @checkbox-clicked="select(poi)"
                                        @card-clicked="onCardClick(poi)"
                />
            </div>

            <div class="flex flex-row justify-between">
                <Button :label="t('delete')" class="w-3/7" severity="danger" @click="deletePOI"/>
                <Button :label="t('add')" class="w-3/7" @click="onAddClick"/>
            </div>
        </div>
        <div class="lg:w-1/2 w-full flex flex-col justify-start gap-8" v-if="inEditingMode!=EditMode.NONE">
            <h2 class="text-3xl" v-if="inEditingMode==EditMode.ADD">{{t('poi.add')}}</h2>
            <h2 class="text-3xl" v-if="inEditingMode==EditMode.UPDATE">{{t('poi.update')}}</h2>

            <Form :key="formKey" ref="form" v-slot="$form" :initialValues="initialValues" :resolver="resolver" @submit="onFormSubmit"
                  class="flex flex-col gap-4">

                <div class="flex flex-col gap-1">
                    <InputText name="name" type="text" placeholder="Name" fluid/>
                    <Message v-if="$form.name?.invalid" severity="error" size="small" variant="simple">
                        {{$form.name.error?.message}}
                    </Message>
                    <InputText name="description" type="text" placeholder="Description" fluid/>
                    <Message v-if="$form.description?.invalid" severity="error" size="small" variant="simple">
                        {{$form.description.error?.message}}
                    </Message>
                    <Select name="cityId" :options="cities" optionLabel="name" optionValue="id" placeholder="City" fluid />
                    <Message v-if="$form.cityId?.invalid" severity="error" size="small" variant="simple">
                        {{$form.cityId.error?.message}}
                    </Message>
                    <InputText name="price" type="text" placeholder="Price" fluid/>
                    <Message v-if="$form.price?.invalid" severity="error" size="small" variant="simple">
                        {{$form.price.error?.message}}
                    </Message>
                    <Select name="type" :options="types" placeholder="Type" fluid />
                    <Message v-if="$form.type?.invalid" severity="error" size="small" variant="simple">
                        {{$form.type.error?.message}}
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
