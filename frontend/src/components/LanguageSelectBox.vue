<script setup lang="ts">
import { ref } from 'vue';
import { ToggleButton } from "primevue";
import { useI18n } from "vue-i18n";
const { t, locale } = useI18n({ useScope: "global" });
const isUsingRomanian = ref(false);

function onClick() {
  locale.value = isUsingRomanian.value ? 'ro-RO' : 'en-US';
}
</script>

<template>
  <div class="card flex justify-center">
    <ToggleButton v-model="isUsingRomanian" class="w-24" :onLabel="t('language.english')" :offLabel="t('language.romanian')" @click="onClick()" />
  </div>
</template>

<style scoped>

</style>