package cloudflight.integra.backend.restaurant;

import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryRestaurantRepository implements RestaurantRepository {
    private HashMap<Long, Restaurant> restaurants;
    private Long lastid = 1L;

    private Long generateId() {
        return lastid++;
    }

    public InMemoryRestaurantRepository() {
        restaurants = new HashMap<>();
    }

    @Override
    public Restaurant addRestaurant(Restaurant restaurant) {
        restaurant.setId(generateId());
        restaurants.put(restaurant.getId(), restaurant);
        return restaurant;
    }

    @Override
    public List<Restaurant> getAllRestaurants() {
        return restaurants.values().stream().toList();
    }

    @Override
    public Restaurant deleteRestaurant(Long id) {
        return restaurants.remove(id);
    }

    @Override
    public Restaurant updateRestaurant(Long id, Restaurant restaurant) {
        restaurant.setId(id);
        return restaurants.put(id, restaurant);
    }

    @Override
    public Restaurant findRestaurant(Long id) {
        return restaurants.get(id);
    }
}
