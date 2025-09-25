import {createRouter, createWebHistory} from 'vue-router'
import HomeView from '@/pages/HomeView.vue';
import CitiesView from '@/pages/CitiesView.vue';
import RestaurantsView from '@/pages/RestaurantsView.vue';
import LoginView from '@/pages/LoginView.vue';

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
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
