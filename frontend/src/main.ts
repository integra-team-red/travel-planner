import {createApp} from 'vue'
import {createPinia} from 'pinia'
import PrimeVue from 'primevue/config';
import Aura from '@primeuix/themes/aura';

import App from './App.vue'
import router from './router'
import {Button, InputText} from 'primevue';

import InputGroup from 'primevue/inputgroup';
import InputGroupAddon from 'primevue/inputgroupaddon';

import './style.css'

const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(PrimeVue, {
    theme: {
        preset: Aura
    }
})

app.component('Button', Button);
app.component('InputText', InputText);
app.component('InputGroup', InputGroup);
app.component('InputGroupAddon', InputGroupAddon);


app.mount('#app')
