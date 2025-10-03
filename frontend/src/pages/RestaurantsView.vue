<script setup lang="ts">
    import {ref} from "vue";
    import type {RestaurantDTO, CityDTO} from "../../typescript-client";
    import {restaurantApi, cityApi} from "@/api.ts";
    import RestaurantCard from "@/components/RestaurantCard.vue";
    import {useI18n} from 'vue-i18n';
    import {useToast} from "primevue";
    import type {FormSubmitEvent} from "@primevue/forms";
    import type {FormResolverOptions} from "@primevue/forms/form";
    import type {FormErrors} from "@/types/forms.types.ts";
    import Select from "primevue/select";
    import {showToast} from "@/utils/toast.utils.ts";

    const {t} = useI18n();
    const toast = useToast();

    enum EditMode {
        NONE,
        ADD,
        UPDATE
    }
    const inEditingMode = ref<EditMode>(EditMode.NONE);

    const restaurants = ref<RestaurantDTO[]>([]);
    const cities = ref<CityDTO[]>([]);
    const selectedRestaurants = ref<number[]>([]);
    const currentRestaurant = ref<number>();

    const initialValues = ref<RestaurantDTO>({
        name: '',
        cityId: 0,
        averagePrice: 0,
        cuisineType: ''
    });

    const formKey = ref<number>(0);

    await fetchRestaurants();
    await fetchCities();

    async function fetchRestaurants(){
        restaurants.value = await restaurantApi.getRestaurants();
    }

    async function fetchCities() {
        cities.value = await cityApi.getCities();
    }

    function select(restaurant: RestaurantDTO){
        const restaurantAlreadySelected = selectedRestaurants.value.includes(restaurant.id!);
        if (restaurantAlreadySelected){
            selectedRestaurants.value = selectedRestaurants.value.filter((restaurantId) => restaurantId !== restaurant.id);
            return;
        }
        selectedRestaurants.value.push(restaurant.id!);
    }

    async function deleteRestaurant(){
        for (const id of selectedRestaurants.value){
            await restaurantApi.deleteRestaurant({id});
        }
        showToast('success', t('restaurants.deleted'), toast);
        selectedRestaurants.value = [];
        await fetchRestaurants();
        inEditingMode.value = EditMode.NONE;
        initialValues.value = {};
        formKey.value++;
    }

    function resolver(submitEvent: FormResolverOptions){
        const errors: FormErrors = {};

        if(submitEvent.values.name === "" || submitEvent.values.name === null || submitEvent.values.name === undefined){
            errors.name = [{message: t('field-is-required')}];
        }
        if(submitEvent.values.cityId === "" || submitEvent.values.cityId === null || submitEvent.values.cityId === undefined){
            errors.cityId = [{message: t('field-is-required')}];
        }
        if(submitEvent.values.averagePrice === "" || submitEvent.values.averagePrice === null || submitEvent.values.averagePrice === undefined){
            errors.averagePrice = [{message: t('field-is-required')}];
        }
        if(submitEvent.values.cuisineType === "" || submitEvent.values.cuisineType === null || submitEvent.values.cuisineType === undefined){
            errors.cuisineType = [{message: t('field-is-required')}];
        }

        return{
            values: submitEvent.values,
            errors
        };
    }

    function onCardClick(restaurant: RestaurantDTO) {
        inEditingMode.value = EditMode.UPDATE;
        initialValues.value = restaurant;
        currentRestaurant.value = restaurant.id;
        formKey.value++;
    }

    function onAddClick(){
        inEditingMode.value = EditMode.ADD;
        initialValues.value = {};
        formKey.value++;
    }

    async function onFormSubmit(submitEvent: FormSubmitEvent){
        if(submitEvent.valid){
            if (inEditingMode.value == EditMode.ADD){
            await restaurantApi.addRestaurant({restaurantDTO: submitEvent.values});
            showToast('success', t('restaurants.added'), toast);
            await fetchRestaurants();}
        else if (inEditingMode.value==EditMode.UPDATE){
            await  restaurantApi.updateRestaurant({restaurantDTO: submitEvent.values, id:currentRestaurant.value ?? 0});
            showToast('success', t('restaurants.updated'), toast);
            await fetchRestaurants();
            }
        inEditingMode.value = EditMode.NONE;
        }
    }

</script>

<template>
    <div class="flex lg:flex-row flex-col place-content-between gap-8">
        <div class="lg:w-1/2 w-full">
            <h2 class="text-3xl">{{t('restaurants.header')}}</h2>

            <div v-for="restaurant in restaurants" :key="restaurant.id">
                <restaurant-card :restaurant="restaurant"
                                 :selected="selectedRestaurants.includes(restaurant.id!)"
                                 @checkbox-clicked="select(restaurant)"
                                 @card-clicked="onCardClick(restaurant)"
                />
            </div>

            <div class="flex flex-row justify-between">
                <Button :label="t('delete')" class="w-3/7" severity="danger" @click="deleteRestaurant"/>
                <Button :label="t('add')" class="w-3/7" @click="onAddClick"/>
            </div>
        </div>
        <div class="lg:w-1/2 w-full flex flex-col justify-start gap-8" v-if="inEditingMode!=EditMode.NONE">
            <h2 class="text-3xl" v-if="inEditingMode==EditMode.ADD">{{t('restaurants.add')}}</h2>
            <h2 class="text-3xl" v-if="inEditingMode==EditMode.UPDATE">{{t('restaurants.update')}}</h2>

            <Form :key="formKey" ref="form" v-slot="$form" :initialValues="initialValues" :resolver="resolver" @submit="onFormSubmit"
                 class="flex flex-col gap-4">

                <div class="flex flex-col gap-1">
                        <InputText name="name" type="text" placeholder="Name" fluid/>
                        <Message v-if="$form.name?.invalid" severity="error" size="small" variant="simple">
                            {{$form.name.error?.message}}
                        </Message>
                        <Select name="cityId" :options="cities" optionLabel="name" optionValue="id" placeholder="City" fluid/>
                        <Message v-if="$form.cityId?.invalid" severity="error" size="small" variant="simple">
                            {{$form.cityId.error?.message}}
                        </Message>
                        <InputText name="averagePrice" type="text" placeholder="Price" fluid/>
                        <Message v-if="$form.averagePrice?.invalid" severity="error" size="small" variant="simple">
                            {{$form.averagePrice.error?.message}}
                        </Message>
                        <InputText name="cuisineType" type="text" placeholder="Cuisine" fluid/>
                        <Message v-if="$form.cuisineType?.invalid" severity="error" size="small" variant="simple">
                            {{$form.cuisineType.error?.message}}
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
