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
        address: '',
        openingHours: '',
        description: '',
        averagePrice: 0,
        cuisineType: '',
        rating: 0,
        image: ''
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

    function OnSelected(restaurant: RestaurantDTO){
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
        changeEditingMode(EditMode.NONE);
    }

    function resolver(submitEvent: FormResolverOptions) {
        const errors: FormErrors = {};

        if (submitEvent.values.name.trim().length < 2){
            errors.name = [{message: t('formFieldError.fieldLength.min', {lowerBound: 2})}];
        }
        for (const key in submitEvent.values){
            if (key === "image") continue;
            const v = submitEvent.values[key];
            if (v === "" || v === null || v === undefined){
                errors[key] = [{message: t('formFieldError.fieldRequired')}];
            }
        }
        return {
            values: submitEvent.values,
            errors
        };
    }

    async function onFormSubmit(submitEvent: FormSubmitEvent){
        if(submitEvent.valid){
            if(inEditingMode.value == EditMode.ADD){
                await sendRestaurantCreationRequest(submitEvent.values)
            }
            else if (inEditingMode.value == EditMode.UPDATE){
                await sendRestaurantUpdateRequest(submitEvent.values)
            }
            inEditingMode.value = EditMode.NONE;
        }
    }

    async function sendRestaurantCreationRequest(restaurant: RestaurantDTO){
        await restaurantApi.addRestaurant({restaurantDTO: restaurant});
        showToast('success', t('coffee-shops.added'), toast);
        await fetchRestaurants();
    }

    async function sendRestaurantUpdateRequest(restaurant: RestaurantDTO){
        await restaurantApi.updateRestaurant({restaurantDTO: restaurant, id: currentRestaurant.value ?? 0});
        showToast('success', t('coffee-shops.updated'), toast);
        await fetchRestaurants();
    }

    function changeEditingMode(mode: EditMode, restaurant?: RestaurantDTO) {
        inEditingMode.value = mode;
        if (mode == EditMode.UPDATE && restaurant) {
            initialValues.value = restaurant;
            currentRestaurant.value = restaurant.id;
        }
        else {
            initialValues.value = {};
        }
        formKey.value++;
    }

</script>

<template>
    <div class="flex lg:flex-row flex-col place-content-between gap-8">
        <div class="lg:w-1/2 w-full">
            <h2 class="text-3xl">{{t('restaurants.header')}}</h2>

            <div v-for="restaurant in restaurants" :key="restaurant.id">
                <restaurant-card :restaurant="restaurant"
                                 :selected="selectedRestaurants.includes(restaurant.id!)"
                                 @checkbox-clicked="OnSelected(restaurant)"
                                 @card-clicked="changeEditingMode(EditMode.UPDATE, restaurant)"
                />
            </div>

            <div class="flex flex-row justify-between">
                <Button :label="t('delete')" class="w-3/7" severity="danger" @click="deleteRestaurant"/>
                <Button :label="t('add')" class="w-3/7" @click="changeEditingMode(EditMode.ADD)"/>
            </div>
        </div>
        <div class="lg:w-1/2 w-full flex flex-col justify-start gap-8" v-if="inEditingMode!=EditMode.NONE">
            <h2 class="text-3xl" v-if="inEditingMode==EditMode.ADD">{{t('restaurants.add')}}</h2>
            <h2 class="text-3xl" v-if="inEditingMode==EditMode.UPDATE">{{t('restaurants.update')}}</h2>

            <Form :key="formKey" ref="form" v-slot="$form" :initialValues="initialValues" :resolver="resolver" @submit="onFormSubmit"
                 class="flex flex-col gap-4">

                <div class="flex flex-col gap-1">
                        <InputText name="name" :placeholder="t('fields.name')" fluid/>
                        <Message v-if="$form.name?.invalid" severity="error" size="small" variant="simple">
                            {{$form.name.error?.message}}
                        </Message>
                        <Select name="cityId" :options="cities" optionLabel="name" optionValue="id" :placeholder="t('fields.city')" fluid/>
                        <Message v-if="$form.cityId?.invalid" severity="error" size="small" variant="simple">
                            {{$form.cityId.error?.message}}
                        </Message>
                        <InputText name="address" :placeholder="t('fields.address')" fluid/>
                        <Message v-if="$form.address?.invalid" severity="error" size="small" variant="simple">
                            {{$form.address.error?.message}}
                        </Message>
                        <InputText name="openingHours" :placeholder="t('fields.openingHours')" fluid/>
                        <Message v-if="$form.openingHours?.invalid" severity="error" size="small" variant="simple">
                            {{$form.openingHours.error?.message}}
                        </Message>
                        <InputText name="description" :placeholder="t('fields.description')" fluid/>
                        <Message v-if="$form.description?.invalid" severity="error" size="small" variant="simple">
                            {{$form.description.error?.message}}
                        </Message>
                        <InputText name="averagePrice" :placeholder="t('fields.averagePrice')" fluid/>
                        <Message v-if="$form.averagePrice?.invalid" severity="error" size="small" variant="simple">
                            {{$form.averagePrice.error?.message}}
                        </Message>
                        <InputText name="cuisineType" :placeholder="t('fields.cuisineType')" fluid/>
                        <Message v-if="$form.cuisineType?.invalid" severity="error" size="small" variant="simple">
                            {{$form.cuisineType.error?.message}}
                        </Message>
                        <InputText name="rating" :placeholder="t('fields.rating')" fluid/>
                        <Message v-if="$form.rating?.invalid" severity="error" size="small" variant="simple">
                            {{$form.rating.error?.message}}
                        </Message>
                        <InputText name = "image" :placeholder="t('fields.image')" fluid/>
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
