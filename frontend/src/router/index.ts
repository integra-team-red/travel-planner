import {createRouter, createWebHistory} from 'vue-router'
import HomeView from '@/pages/HomeView.vue';
import CitiesView from '@/pages/CitiesView.vue';
import RestaurantsView from '@/pages/RestaurantsView.vue';
import LoginView from '@/pages/LoginView.vue';
import PointsOfInterestView from "@/pages/PointsOfInterestView.vue";
import POIImportExport from "@/pages/POIImportExport.vue";
import CitiesImportExport from "@/pages/CitiesImportExport.vue";
import TripsView from '@/pages/TripsView.vue';
import '@/interceptor/interceptor.ts'
import ProposalsView from "@/pages/ProposalsView.vue";
const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/proposals',
            name: 'proposals',
            component: ProposalsView
        },
        {
            path: '/poiImportExport',
            name: 'poiImportExport',
            component: POIImportExport
        },
        {
            path: '/citiesImportExport',
            name: 'citiesImportExport',
            component: CitiesImportExport
        },
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
            path: '/points-of-interest',
            name: 'points-of-interest',
            component: PointsOfInterestView
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
        },
        {
            path: '/trips',
            name: 'trips',
            component: TripsView
        }
    ],
})

export default router
