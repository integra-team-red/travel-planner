package cloudflight.integra.backend.restaurant;

import java.util.List;

public interface RestaurantRepository {
    Restaurant addRestaurant(Restaurant restaurant);

    List<Restaurant> getAllRestaurants();

    Restaurant deleteRestaurant(Long id);

    Restaurant updateRestaurant(Long id, Restaurant restaurant);

    Restaurant findRestaurant(Long id);
}
