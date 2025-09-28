<script setup lang="ts">
import {cityApi} from "@/api.ts";
import {useToast} from 'primevue';
import {useI18n} from "vue-i18n";
import {downloadCityArrayToUserDevice} from "@/utils/fileutils.ts";

const { t } = useI18n();
const toast = useToast();

function downloadCities() {
    cityApi.exportApprovedCitiesToJson()
        .then(
            value => {
                downloadCityArrayToUserDevice(value);
                toast.add({
                    severity: 'success',
                    summary: t('cities.download.success'),
                    life: 5000
                });
            }
        )
        .catch(
            () => {
                toast.add({
                    severity: 'error',
                    summary: t('cities.download.failure'),
                    life: 5000
                });
            }
        );
}

async function uploadCities(file: File, clearCallback: () => void) {
    try {
        await cityApi.importApprovedCitiesFromJson({
            cityDTO: JSON.parse(await file.text())
        });
        toast.add({
            severity: 'success',
            summary: t('cities.upload.success'),
            life: 5000
        });
    } catch {
        toast.add({
            severity: 'error',
            summary: t('cities.upload.failure'),
            life: 5000
        });
    }
    clearCallback();
}
</script>

<template>
    <toast />
    <div class="flex gap-20 flex-col not-sm:px-14">
        <h2 class="pt-18 font-semibold text-3xl">{{t('cities.download.header')}}:</h2>
        <div @click="downloadCities" class="flex flex-col self-center justify-center items-center bg-[#BBD1EA] p-5 w-full sm:w-[50%] cursor-pointer rounded-2xl text-gray-600">
            <i class="pi pi-download" style="font-size: 2.5rem"></i>
            <div class="font-medium text-center">
                {{t('cities.download.prompt')}}
            </div>
        </div>
        <h2 class="font-semibold text-3xl">{{t('cities.upload.header')}}:</h2>
        <FileUpload :multiple="true" :maxFileSize="100000" accept=".json" pt:root:class="!border-0" pt:header:class="justify-self-center w-full sm:w-[50%] !p-0">
            <template #header="{files, chooseCallback, clearCallback}">
                <div class="flex flex-col justify-center items-center bg-[#BBD1EA] p-5 cursor-pointer w-full rounded-2xl text-gray-600" @click="chooseCallback" v-if="files.length == 0">
                    <i class="pi pi-upload" style="font-size: 2.5rem"></i>
                    <div class="font-medium text-center">
                        {{t('cities.upload.prompt')}}
                    </div>
                </div>
                <div v-else class="w-full flex flex-col items-center">
                    <Button @click="clearCallback" icon="pi pi-times" severity="danger" rounded size="small" class="ml-20 self-center" />
                    <i class="pi pi-file" style="font-size: 2.5rem"></i>
                    <div>{{files[0].name}}</div>
                    <Button @click="uploadCities(files[0], clearCallback)" severity="success" class="rounded-[50%]">
                        {{t('cities.upload.confirm')}}
                    </Button>
                </div>
            </template>
            <template #content><div></div></template>
        </FileUpload>
    </div>
</template>
<style scoped>
</style>