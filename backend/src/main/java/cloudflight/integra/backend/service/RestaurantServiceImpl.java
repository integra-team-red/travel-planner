package cloudflight.integra.backend.service;

import cloudflight.integra.backend.model.Restaurant;
import cloudflight.integra.backend.repository.RestaurantRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RestaurantServiceImpl implements RestaurantService {
    private RestaurantRepository restaurantRepository;

    @Autowired
    public RestaurantServiceImpl(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public Restaurant addRestaurant(Restaurant restaurant) {

        return restaurantRepository.addRestaurant(restaurant);
    }

    @Override
    public List<Restaurant> getAllRestaurants() { return restaurantRepository.getAllRestaurants(); }

    @Override
    public Restaurant deleteRestaurant(Long id) {
        return restaurantRepository.deleteRestaurant(id);
    }

    @Override
    public Restaurant updateRestaurant(Long id, Restaurant restaurant) {
        return restaurantRepository.updateRestaurant(id, restaurant);
    }
}
