import {defineStore} from "pinia";
import type {CityDTO} from "../../typescript-client/";

export type TripDetails = {
    name: string,
    city: CityDTO,
    duration: string,
    priceLowerBound: number,
    priceUpperBound: number
}

export const useTripDetailsStore = defineStore('trip-details', () => {
    let tripDetails: TripDetails | undefined;

    function set(newTripDetails: TripDetails) {
        tripDetails = newTripDetails;
    }

    function get(): TripDetails | undefined {
        return tripDetails;
    }

    return { set, get };
});