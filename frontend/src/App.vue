<script setup lang="ts">
import { HollowDotsSpinner } from "epic-spinners";
import { isLoading } from "./stores/spinner.ts";
</script>

<template>
    <div v-if="isLoading" id="spinner">
        <hollow-dots-spinner
            :animation-duration="1000"
            :dot-size="15"
            :dots-num="3"
            color="#BFA181"
        />
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
