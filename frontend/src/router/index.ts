import {createRouter, createWebHistory} from 'vue-router'
import HomeView from '@/pages/HomeView.vue';
import CitiesView from '@/pages/CitiesView.vue';
import RestaurantsView from '@/pages/RestaurantsView.vue';
import LoginView from '@/pages/LoginView.vue';
import PointsOfInterestView from "@/pages/PointsOfInterestView.vue";
import POIImportExport from "@/pages/POIImportExport.vue";
import CitiesImportExport from "@/pages/CitiesImportExport.vue";
import TripsView from '@/pages/TripsView.vue';
import UserProfileView from '@/pages/UserProfileView.vue';
import SignUpView from '@/pages/SignUpView.vue';

import '@/interceptor/interceptor.ts'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
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
        },
        {
            path: '/profile',
            name: 'profile',
            component: UserProfileView
        },
        {
            path: '/register',
            name: 'register',
            component: SignUpView
        }


    ],
})

export default router
