<script setup lang="ts">
    import type {MenuItem} from 'primevue/menuitem';
    import {useRouter, useRoute} from 'vue-router';
    import LanguageSelectBox from '@/components/LanguageSelectBox.vue';

    const router = useRouter();
    const route = useRoute();

    const items: (MenuItem & { path: string })[] = [
        {
            label: 'Home',
            icon: 'pi pi-link',
            path: '/',
            command: () => {
                router.push('/');
            }
        },
        {
            label: 'Cities',
            icon: 'pi pi-building',
            path: '/cities',
            command: () => {
                router.push('/cities');
            }
        },
        {
            label: 'Trips',
            icon: 'pi pi-briefcase',
            path: '/trips',
            command: () => {
                router.push('/trips')
            }
        },
        {
            label: 'Restaurants',
            icon: 'pi pi-link',
            path: '/restaurants',
            command: () => {
                router.push('/restaurants');
            }
        },
        {
            label: 'Points of Interest',
            icon: 'pi pi-link',
            path: '/points-of-interest',
            command: () => {
                router.push('/points-of-interest');
            }
        }
    ]
</script>

<template>
    <Menubar :model="items" class="lg:max-w-6xl mx-auto mt-2">
        <template #start>
            <img src="/images/logo.png" alt="Logo" class="h-10 w-10" />
        </template>
        <template #item="{ item, props, hasSubmenu, root }">
            <a v-ripple class="flex items-center" v-bind="props.action" :class="{'bg-gray-400 text-white': item.path === '/' ? route.path === '/' : route.path.startsWith(item.path)}">
                <span>{{item.label}}</span>
                <Badge v-if="item.badge" :class="{ 'ml-auto': !root, 'ml-2': root }" :value="item.badge"/>
                <span v-if="item.shortcut"
                      class="ml-auto border border-surface rounded bg-emphasis text-muted-color text-xs p-1">{{
                        item.shortcut
                    }}</span>
                <i v-if="hasSubmenu"
                   :class="['pi pi-angle-down ml-auto', { 'pi-angle-down': root, 'pi-angle-right': !root }]"></i>
            </a>
        </template>
        <template #end>
            <div class="flex">
                <language-select-box class="mr-2"/>
                <div class="flex items-center gap-2">
                    <InputText placeholder="Search" type="text" class="w-32 sm:w-auto"/>
                    <Avatar image="/img.png" shape="circle"/>
                </div>
            </div>
        </template>
    </Menubar>
</template>

<style scoped>

</style>
