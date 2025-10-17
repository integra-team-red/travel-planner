package cloudflight.integra.backend.restaurant;

import java.util.List;

public interface RestaurantService {
    Restaurant addRestaurant(Restaurant restaurant);

    List<Restaurant> addRestaurants(List<Restaurant> restaurants);

    Restaurant updateRestaurant(Long id, Restaurant restaurant);

    void deleteRestaurant(Long id);

    Restaurant getRestaurant(Long id);

    List<Restaurant> getAllRestaurants();

    List<Restaurant> getAllRestaurantsSortedByName(int pageNumber, int pageSize, boolean isDescending);

    List<Restaurant> getAllRestaurantsSortedByAveragePrice(int pageNumber, int pageSize, boolean isDescending);

    List<Restaurant> getAllRestaurantsByCuisine(int pageNumber, int pageSize, String cuisineType);

    List<Restaurant> getRestaurantsByCity(Long id, String name);
}
