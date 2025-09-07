package cloudflight.integra.backend.service;

import cloudflight.integra.backend.model.Restaurant;

import java.util.List;

public interface RestaurantService {
    Restaurant addRestaurant(Restaurant restaurant);
    List<Restaurant> getAllRestaurants();
    Restaurant deleteRestaurant(Long id);
    Restaurant updateRestaurant(Long id,Restaurant restaurant);

}
