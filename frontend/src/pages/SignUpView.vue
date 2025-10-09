<script setup lang="ts">
import { useRouter } from 'vue-router'
import InputText from 'primevue/inputtext'
import Password from 'primevue/password'
import Button from 'primevue/button'

const router = useRouter()
</script>

<template>
    <div class="bg-surface-50 dark:bg-surface-950 px-6 py-20 md:px-20 lg:px-80">
        <div class="bg-surface-0 dark:bg-surface-900 p-8 md:p-12 shadow-sm rounded-2xl w-full max-w-sm mx-auto flex flex-col gap-8">
            <h2 class="text-2xl font-semibold text-center">Create Account</h2>
            <div class="flex flex-col gap-4">
                <div>
                    <label for="email" class="font-medium">Email</label>
                    <InputText id="email" type="email" class="w-full" placeholder="example@email.com" />
                </div>
                <div>
                    <label for="password" class="font-medium">Password</label>
                    <Password id="password" toggleMask :feedback="false" class="w-full" />
                </div>

                <Button label="Sign Up" icon="pi pi-user-plus" class="w-full" />
            </div>

            <p class="text-center text-gray-500 text-sm">
                Already have an account?
                <a @click="router.push('/login')" class="text-primary cursor-pointer font-medium hover:underline">Sign In</a>
            </p>
        </div>
    </div>
</template>

<style scoped>
</style>
