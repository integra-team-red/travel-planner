<script setup lang="ts">
    import {useUserStore} from "@/stores/user.ts";
    import {authApi} from "@/api.ts";

    const userStore = useUserStore()
    const email = await authApi.getCurrentUser();
    const userObj = {
        email: email,
        roles: []
    };
    userStore.set(userObj);
</script>

<template>
    <div></div>
</template>


<style scoped>

</style>
