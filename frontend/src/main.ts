import {createApp} from 'vue'
import {createPinia} from 'pinia'
import PrimeVue from 'primevue/config';
import Aura from '@primeuix/themes/aura';

import App from './App.vue'
import router from './router'

import './style.css'

import {createI18n} from "vue-i18n";
import translations from "../translations.json"
import {ToastService} from 'primevue';
import ConfirmationService from 'primevue/confirmationservice';

const app = createApp(App)

app.use(createI18n({
    locale: 'en-US',
    messages: translations
}));

app.use(createPinia())
app.use(router)
app.use(PrimeVue, {
    theme: {
        preset: Aura,
        options: {
            prefix: 'p',
            darkModeSelector: '.my-app-dark',
            cssLayer: false
        }
    }
})

app.mount('#app')
app.use(ToastService);
app.use(ConfirmationService);
