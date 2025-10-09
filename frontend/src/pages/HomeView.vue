<script setup lang="ts">
    import {useUserStore} from "@/stores/user.ts";
    import {authApi} from "@/api.ts";

    const userStore = useUserStore()
    const user = await authApi.getCurrentUser();
    userStore.set(user);
</script>

<template>
    <div></div>
</template>


<style scoped>

</style>
