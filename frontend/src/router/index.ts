import {createRouter, createWebHistory} from 'vue-router'
import CitiesView from '@/pages/CitiesView.vue';
import RestaurantsView from '@/pages/RestaurantsView.vue';
import LoginView from '@/pages/LoginView.vue';
import PointsOfInterestView from "@/pages/PointsOfInterestView.vue";
import POIImportExport from "@/pages/POIImportExport.vue";
import CitiesImportExport from "@/pages/CitiesImportExport.vue";
import TripsView from '@/pages/TripsView.vue';
import UserProfileView from '@/pages/UserProfileView.vue';
import SignUpView from '@/pages/SignUpView.vue';
import ProposalReviewView from "@/pages/ProposalReviewView.vue";
import ProposalsView from "@/pages/ProposalsView.vue";
import EventsView from "@/pages/EventsView.vue";
import '@/interceptor/interceptor.ts'
import SpasView from "@/pages/SpasView.vue";
import CoffeeShopView from "@/pages/CoffeeShopView.vue";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/spas',
            name: 'spas',
            component: SpasView
        },
        {
            path: '/proposals',
            name: 'proposals',
            component: ProposalsView
        },
        {
            path: '/proposals/review',
            name: 'proposalsReview',
            component: ProposalReviewView
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
            name: 'trips',
            component: TripsView
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
            path: '/events',
            name: 'events',
            component: EventsView
        },
        {
            path: '/login',
            name: 'login',
            component: LoginView
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
        },
        {
            path: '/coffee-shops',
            name: 'coffee-shops',
            component: CoffeeShopView
        }


    ],
})

export default router
