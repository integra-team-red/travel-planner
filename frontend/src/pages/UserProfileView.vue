<script setup lang="ts">
import Card from 'primevue/card'
import Avatar from 'primevue/avatar'
import Button from 'primevue/button'
import {useRouter} from 'vue-router'
import {useUserStore} from "@/stores/user.ts";
import {authApi} from "@/api.ts";

const userStore = useUserStore()
const user = await authApi.getCurrentUser();
userStore.set(user);
const router = useRouter()

</script>
<template>
    <div class="flex flex-col items-center mt-6">
        <Card class="w-full max-w-3xl shadow-md overflow-hidden">
            <template #header>
                <div class="relative">
                    <img src="/images/noLocation.png" alt="profile banner" class="w-full h-56 object-cover"/>
                    <div class="absolute left-1/2 bottom-[-3rem] transform -translate-x-1/2">
                        <Avatar image="/images/noOne.png" shape="circle" size="xlarge" class="border-4 border-white shadow-lg"/>
                    </div>
                </div>
            </template>

            <template #content>
                <div class="flex flex-col items-center text-center mt-12 pb-6">
                    <template v-if="userStore.isLoggedIn">
                        <p class="text-gray-600 mt-1">{{ userStore.user?.email }}</p>
                        <p class="text-sm mt-2">role: <strong>{{ userStore.user?.roles?.[0] }}</strong></p>
                    </template>

                    <template v-else>
                        <h2 class="text-2xl font-semibold text-gray-700">
                            Not logged in
                        </h2>
                        <p class="text-gray-500 mt-1">
                            Please log in to view your profile.
                        </p>
                        <Button label="Go to Login" icon="pi pi-sign-in" class="mt-5" severity="primary"
                                @click="router.push('/login')"/>
                    </template>
                </div>
            </template>
        </Card>
    </div>
</template>
