import { createRouter, createWebHistory } from 'vue-router'
import HomeView from "@/components/HomeView.vue";
import CitiesView from '@/components/CitiesView.vue';
import RestaurantsView from '@/components/RestaurantsView.vue';
import LoginView from '@/components/LoginView.vue';
import '@/interceptor/interceptor.ts'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/home',
            name: 'home',
            component: HomeView
        },
        {
            path: '/cities',
            name: 'cities',
            component: CitiesView
        },
        {
            path: '/restaurants',
            name: 'restaurants',
            component: RestaurantsView
        },
        {
            path: '/login',
            name: 'login',
            component: LoginView
        }
    ],
})

export default router
