<script setup lang="ts">
import {ref} from 'vue';
import type {EventDTO, POIDTO} from '../../typescript-client';
import {eventApi, poiApi} from '@/api.ts';
import EventCard from '@/components/EventCard.vue';
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

const events = ref<EventDTO[]>([]);
const pois = ref<POIDTO[]>([]);
const selectedEvents = ref<number[]>([])
const currentEvent = ref<number>();
const DEFAULT_DATE = new Date();

const initialValues = ref<EventDTO>({
    name: '',
    description: '',
    startTime: DEFAULT_DATE,
    endTime: DEFAULT_DATE,
    poiId: 0,
    price: 0,
    audience: ''
});

const audiences = ref<string[]>([]);
const formKey = ref<number>(0);

await fetchEvents();
await fetchPOIs();
await fetchAudiences();

async function fetchEvents() {
    events.value = await eventApi.getEvents();
}

async function fetchPOIs() {
    pois.value = await poiApi.getPointsOfInterest();
}

async function fetchAudiences() {
    audiences.value = await eventApi.getAllEventAudiences();
}

function select(event: EventDTO) {
    const eventAlreadySelected = selectedEvents.value.includes(event.id!);
    if (eventAlreadySelected) {
        selectedEvents.value = selectedEvents.value.filter((eventId) => eventId !== event.id);
        return;
    }
    selectedEvents.value.push(event.id!);
}

async function deleteEvent() {
    for (const id of selectedEvents.value) {
        await eventApi.deleteEvent({id});
    }
    showToast('success', t('event.deleted'), toast);
    selectedEvents.value = [];
    await fetchEvents()
    inEditingMode.value = EditMode.NONE;
    initialValues.value = {};
    formKey.value++;
}

function changeEditingMode(mode: EditMode, event?: EventDTO) {
    inEditingMode.value = mode;
    if (mode == EditMode.UPDATE && event) {
        initialValues.value = event;
        currentEvent.value = event.id
    }
    else {
        initialValues.value = {};
    }
    formKey.value++;
}

function resolver(submitEvent: FormResolverOptions) {
    const errors: FormErrors = {};

    if (submitEvent.values.name.trim().length < 2) {
        errors.name = [{message: t('formFieldError.fieldLength.min', {lowerBound: 2})}];
    }
    if(submitEvent.values.startTime > submitEvent.values.endTime){
        errors.endTime = [{message: t('formFieldError.fieldOrder', {low: t('fields.startTime'), high: t('fields.endTime')})}]
    }
    for(const key in submitEvent.values){
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

async function sendEventCreationRequest(event: EventDTO) {
    await eventApi.addEvent({eventDTO: event});
    showToast('success', t('event.added'), toast);
    await fetchEvents();
}

async function sendEventUpdateRequest(event: EventDTO) {
    await eventApi.updateEvent({eventDTO: event, id: currentEvent.value ?? 0});
    showToast('success', t('event.added'), toast);
    await fetchEvents();
}

async function onFormSubmit(submitEvent: FormSubmitEvent) {
    if (submitEvent.valid) {
        if(inEditingMode.value==EditMode.ADD) {
            await sendEventCreationRequest(submitEvent.values)
        } else if (inEditingMode.value==EditMode.UPDATE){
            await sendEventUpdateRequest(submitEvent.values)
        }
        inEditingMode.value = EditMode.NONE;
    }
}

</script>

<template>
    <div class="flex lg:flex-row flex-col place-content-between gap-8">
        <div class="lg:w-1/2 w-full">
            <h2 class="text-3xl">{{t('event.header')}}</h2>

            <div v-for="event in events" :key="event.id" >
                <event-card :event="event"
                            :selected="selectedEvents.includes(event.id!)"
                            @checkbox-clicked="select(event)"
                            @card-clicked="changeEditingMode(EditMode.ADD, event);"
                />
            </div>

            <div class="flex flex-row justify-between">
                <Button :label="t('delete')" class="w-3/7" severity="danger" @click="deleteEvent"/>
                <Button :label="t('add')" class="w-3/7" @click="changeEditingMode(EditMode.ADD);"/>
            </div>
        </div>
        <div class="lg:w-1/2 w-full flex flex-col justify-start gap-8" v-if="inEditingMode!=EditMode.NONE">
            <h2 class="text-3xl" v-if="inEditingMode==EditMode.ADD">{{t('event.add')}}</h2>
            <h2 class="text-3xl" v-if="inEditingMode==EditMode.UPDATE">{{t('event.update')}}</h2>

            <Form :key="formKey" ref="form" v-slot="$form" :initialValues="initialValues" :resolver="resolver" @submit="onFormSubmit"
                  class="flex flex-col gap-4">

                <div class="flex flex-col gap-1">
                    <InputText name="name" :placeholder="t('fields.name')" fluid/>
                    <Message v-if="$form.name?.invalid" severity="error" size="small" variant="simple">
                        {{$form.name.error?.message}}
                    </Message>
                    <InputText name="description" :placeholder="t('fields.description')" fluid/>
                    <Message v-if="$form.description?.invalid" severity="error" size="small" variant="simple">
                        {{$form.description.error?.message}}
                    </Message>
                    <Select name="poiId" :options="pois" optionLabel="name" optionValue="id" :placeholder="t('fields.poi')" fluid />
                    <Message v-if="$form.poiId?.invalid" severity="error" size="small" variant="simple">
                        {{$form.poiId.error?.message}}
                    </Message>
                    <DatePicker name="startTime" :placeholder="t('fields.startTime')" showTime fluid />
                    <Message v-if="$form.startTime?.invalid" severity="error" size="small" variant="simple">
                        {{$form.startTime.error?.message}}
                    </Message>
                    <DatePicker name="endTime" :placeholder="t('fields.endTime')" showTime fluid />
                    <Message v-if="$form.endTime?.invalid" severity="error" size="small" variant="simple">
                        {{$form.endTime.error?.message}}
                    </Message>
                    <InputText name="price" :placeholder="t('fields.price')" fluid/>
                    <Message v-if="$form.price?.invalid" severity="error" size="small" variant="simple">
                        {{$form.price.error?.message}}
                    </Message>
                    <Select name="audience" :options="audiences" :placeholder="t('fields.audience')" fluid />
                    <Message v-if="$form.audience?.invalid" severity="error" size="small" variant="simple">
                        {{$form.audience.error?.message}}
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
