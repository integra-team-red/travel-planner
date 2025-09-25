<script setup lang="ts">
    import Navbar from '@/components/Navbar.vue';
    import PageLayout from '@/components/PageLayout.vue';
</script>

<template>
    <suspense>
        <div>
            <navbar/>
            <toast />
            <page-layout>
                <router-view/>
            </page-layout>
        </div>
    </suspense>
</template>

<style scoped>
</style>
