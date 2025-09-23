<script setup lang="ts">
    import type {MenuItem} from 'primevue/menuitem';
    import {useRouter} from 'vue-router';

    // const user = ref<string>("");

    const router = useRouter();

    // await authApi.getCurrentUser(); todo(fix): this not being json for some reason?

    const items: MenuItem[] = [{
        label: 'Home',
        icon: 'pi pi-link',
        command: () => {
            router.push('/');
        }
    }]

    // function logout() {
    //     localStorage.removeItem("jwt");
    //     router.push("/login");
    // }
</script>

<template>
    <Menubar :model="items">
        <template #start>
            <!--todo: add logo here-->
        </template>
        <template #item="{ item, props, hasSubmenu, root }">
            <a v-ripple class="flex items-center" v-bind="props.action">
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
            <div class="flex items-center gap-2">
                <InputText placeholder="Search" type="text" class="w-32 sm:w-auto"/>
                <Avatar image="/img.png" shape="circle"/>
            </div>
        </template>
    </Menubar>
</template>

<style scoped>

</style>
