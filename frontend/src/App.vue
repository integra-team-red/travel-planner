<script setup lang="ts">
    import {computed} from "vue";
    import {useSpinnerStore} from "./stores/spinner.ts";
    import ProgressSpinner from 'primevue/progressspinner';
    import Navbar from '@/components/Navbar.vue';
    import PageLayout from '@/components/PageLayout.vue';
    import { useRoute } from "vue-router"

    const route = useRoute()
    const isLoading = computed(() => useSpinnerStore().isLoading);
</script>

<template>
    <suspense>
        <div>
            <Navbar/>
            <div v-if="isLoading" id="spinner">
                <ProgressSpinner/>
            </div>
            <div v-else>
                <Toast/>
                <PageLayout>
                    <router-view/>
                </PageLayout>
            </div>
        </div>
    </suspense>
</template>

<style scoped>
    #spinner {
        position: fixed;
        inset: 0;
        display: flex;
        align-items: center;
        justify-content: center;
    }
</style>
