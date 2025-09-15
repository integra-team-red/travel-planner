import { createRouter, createWebHistory } from 'vue-router'
import HomeView from "@/components/HomeView.vue";
import CitiesView from '@/components/CitiesView.vue';
import RestaurantsView from '@/components/RestaurantsView.vue';

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
        }
    ],
})

export default router
