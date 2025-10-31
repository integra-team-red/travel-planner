package cloudflight.integra.backend.trip;

import cloudflight.integra.backend.coffeeShop.CoffeeShop;
import cloudflight.integra.backend.restaurant.Restaurant;

public class DailyItinerary {
    Restaurant restaurant;
    CoffeeShop coffeeShop;
    Object morningActivity;
    Object afternoonActivity;
    Object eveningActivity;

    public DailyItinerary(
            Restaurant restaurant,
            CoffeeShop coffeeShop,
            Object morningActivity,
            Object afternoonActivity,
            Object eveningActivity) {
        this.restaurant = restaurant;
        this.coffeeShop = coffeeShop;
        this.morningActivity = morningActivity;
        this.afternoonActivity = afternoonActivity;
        this.eveningActivity = eveningActivity;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public DailyItinerary setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
        return this;
    }

    public CoffeeShop getCoffeeShop() {
        return coffeeShop;
    }

    public DailyItinerary setCoffeeShop(CoffeeShop coffeeShop) {
        this.coffeeShop = coffeeShop;
        return this;
    }

    public Object getMorningActivity() {
        return morningActivity;
    }

    public DailyItinerary setMorningActivity(Object morningActivity) {
        this.morningActivity = morningActivity;
        return this;
    }

    public Object getAfternoonActivity() {
        return afternoonActivity;
    }

    public DailyItinerary setAfternoonActivity(Object afternoonActivity) {
        this.afternoonActivity = afternoonActivity;
        return this;
    }

    public Object getEveningActivity() {
        return eveningActivity;
    }

    public DailyItinerary setEveningActivity(Object eveningActivity) {
        this.eveningActivity = eveningActivity;
        return this;
    }
}
