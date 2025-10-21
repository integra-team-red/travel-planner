<script setup lang="ts">
    import type {MenuItem} from 'primevue/menuitem';
    import {useRoute, useRouter} from 'vue-router';
    import LanguageSelectBox from '@/components/LanguageSelectBox.vue';
    import {useI18n} from "vue-i18n";
    import {computed} from "vue";
    import {useUserStore} from '@/stores/user.ts';
    import {authApi} from '@/api.ts';

    const {t} = useI18n();
    const router = useRouter();
    const route = useRoute();

    const userStore = useUserStore()
    if (!window.location.toString().includes('login')) {
        authApi.getCurrentUser().then(user => userStore.set(user));
    }

    const items = computed<(MenuItem & { path: string })[]>(() => [
        {
            label: t('home'),
            icon: 'pi pi-link',
            path: '/',
            command: () => {
                router.push('/');
            }
        },
        {
            label: t('cities.header'),
            icon: 'pi pi-building',
            path: '/cities',
            command: () => {
                router.push('/cities');
            }
        },
        {
            label: t('trips.header'),
            icon: 'pi pi-briefcase',
            path: '/trips',
            command: () => {
                router.push('/trips')
            }
        },
        {
            label: t('restaurants.header'),
            icon: 'pi pi-link',
            path: '/restaurants',
            command: () => {
                router.push('/restaurants');
            }
        },
        {
            label: t('pois.header'),
            icon: 'pi pi-link',
            path: '/points-of-interest',
            command: () => {
                router.push('/points-of-interest');
            }
        },
        {
            label: t('logout') ,
            icon: 'pi pi-sign-out',
            path: '/login',
            command: () => {
                localStorage.removeItem("jwt");
                router.push('/login');
            }
        },
        {
            label: t('proposals.header'),
            icon: 'pi pi-send',
            path: '/proposals',
            command: () => {
                router.push('/proposals');
            }
        },
        {
            label: t('event.header'),
            icon: 'pi pi-send',
            path: '/events',
            command: () => {
                router.push('/events');
            }
        }
    ])
</script>

<template>
    <Menubar v-if="userStore.isLoggedIn" :model="items" class="lg:max-w-6xl mx-auto mt-2">
        <template #start>
            <img src="/images/logo.png" alt="Logo" class="h-10 w-10" @click="router.push('/')"/>
        </template>
        <template #item="{ item, props, hasSubmenu, root }">
            <a v-ripple class="flex items-center" v-bind="props.action"
               :class="{'bg-gray-400 text-white': item.path === '/' ? route.path === '/' : route.path.startsWith(item.path)}">
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
                    <RouterLink to="/profile">
                        <Avatar image="/img.png" shape="circle"/>
                    </RouterLink>
                </div>
            </div>
        </template>
    </Menubar>
</template>

<style scoped>

</style>
