<script setup lang="ts">
    import Navbar from '@/components/Navbar.vue';
</script>

<template>
    <suspense>
        <div>
            <navbar/>
            <div>
                <router-view/>
            </div>
        </div>
    </suspense>
</template>

<style scoped>
</style>
