<script setup lang="ts">
import {poiApi} from "@/api.ts";
import {useToast} from 'primevue';
import {useI18n} from "vue-i18n";
import {downloadPOIArrayToUserDevice} from "@/utils/file.utils.ts";
import {showToast} from "@/utils/toast.utils.ts";

const toast = useToast();
const { t } = useI18n();

function downloadPOIs() {
    poiApi.exportApprovedPOIsToJson()
        .then(
            value => {
                downloadPOIArrayToUserDevice(value);
                showToast('success', t('pois.download.success'), toast);
            }
        )
        .catch(
            () => {
                showToast('error', t('pois.download.failure'), toast);

            }
        );
}
async function uploadPOIs(file: File, clearCallback: () => void) {
    try {
        await poiApi.importApprovedPOIsFromJson({
            pOIDTO: JSON.parse(await file.text())
        });
        showToast('success', t('pois.upload.success'), toast);
    } catch {
        showToast('error', t('pois.upload.failure'), toast);
    }
    clearCallback();
}
</script>

<template>
    <div class="flex gap-20 flex-col not-sm:px-14">
        <h2 class="pt-18 font-semibold text-3xl">{{t('pois.download.header')}}:</h2>
        <div @click="downloadPOIs" class="flex flex-col self-center justify-center items-center bg-[#BBD1EA] p-5 w-full sm:w-[50%] cursor-pointer rounded-2xl text-gray-600">
            <i class="pi pi-download" style="font-size: 2.5rem"></i>
            <div class="font-medium text-center">
                {{t('pois.download.prompt')}}
            </div>
        </div>
        <h2 class="font-semibold text-3xl">{{t('pois.upload.header')}}:</h2>
        <FileUpload :multiple="true" :maxFileSize="100000" accept=".json" pt:root:class="!border-0" pt:header:class="justify-self-center w-full sm:w-[50%] !p-0">
            <template #header="{files, chooseCallback, clearCallback}">
                <div class="flex flex-col justify-center items-center bg-[#BBD1EA] p-5 cursor-pointer w-full rounded-2xl text-gray-600" @click="chooseCallback" v-if="files.length == 0">
                    <i class="pi pi-upload" style="font-size: 2.5rem"></i>
                    <div class="font-medium text-center">
                        {{t('pois.upload.prompt')}}
                    </div>
                </div>
                <div v-else class="w-full flex flex-col items-center">
                    <Button @click="clearCallback" icon="pi pi-times" severity="danger" rounded size="small" class="ml-20 self-center" />
                    <i class="pi pi-file" style="font-size: 2.5rem"></i>
                    <div>{{files[0].name}}</div>
                    <Button @click="uploadPOIs(files[0], clearCallback)" severity="success" class="rounded-[50%]">
                        {{t('pois.upload.confirm')}}
                    </Button>
                </div>
            </template>
            <template #content><div></div></template>
        </FileUpload>
    </div>
</template>
<style scoped>
</style>