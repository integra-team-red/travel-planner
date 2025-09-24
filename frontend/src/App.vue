<script setup lang="ts">
import ProgressSpinner from 'primevue/progressspinner';
import { isLoading } from "./stores/spinner.ts";
</script>

<template>
    <div v-if="isLoading" id="spinner">
      <ProgressSpinner />
    </div>
    <div v-else>
      <router-view />
    </div>
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
