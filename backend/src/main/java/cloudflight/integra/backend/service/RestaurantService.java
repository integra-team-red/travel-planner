package cloudflight.integra.backend.service;

import cloudflight.integra.backend.model.Restaurant;
import java.util.List;

public interface RestaurantService {
    Restaurant addRestaurant(Restaurant restaurant);

    List<Restaurant> getAllRestaurants();

    void deleteRestaurant(Long id);

    Restaurant updateRestaurant(Long id, Restaurant restaurant);
    List<Restaurant> getRestaurantsByCity(Long id,String name);
}
