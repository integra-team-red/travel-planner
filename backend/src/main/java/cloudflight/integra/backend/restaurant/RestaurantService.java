package cloudflight.integra.backend.restaurant;

import java.util.List;

public interface RestaurantService {
    Restaurant addRestaurant(Restaurant restaurant);

    List<Restaurant> getAllRestaurants();

    void deleteRestaurant(Long id);

    Restaurant updateRestaurant(Long id, Restaurant restaurant);

    List<Restaurant> getAllRestaurantsSortedByName(int pageNumber, int pageSize, boolean isDescending);

    List<Restaurant> getAllRestaurantsSortedByAveragePrice(int pageNumber, int pageSize, boolean isDescending);

    List<Restaurant> getAllRestaurantsByCuisine(int pageNumber, int pageSize, String cuisineType);
}
