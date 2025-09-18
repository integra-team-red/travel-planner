package cloudflight.integra.backend.service;

import cloudflight.integra.backend.model.Restaurant;
import cloudflight.integra.backend.repository.DBCityRepository;
import cloudflight.integra.backend.repository.DBRestaurantRepository;
import cloudflight.integra.backend.repository.RestaurantRepository;
import java.util.List;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RestaurantServiceImpl implements RestaurantService {
    private DBRestaurantRepository restaurantRepository;
    private DBCityRepository cityRepository;

    @Autowired
    public RestaurantServiceImpl(DBRestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public Restaurant addRestaurant(Restaurant restaurant) {

        return restaurantRepository.save(restaurant);
    }

    @Override
    public List<Restaurant> getAllRestaurants() { return restaurantRepository.findAll(); }

    @Override
    public void deleteRestaurant(Long id) {
        restaurantRepository.deleteById(id);

    }

    @Override
    public Restaurant updateRestaurant(Long id, Restaurant newRestaurant) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant with id " + id + " not found"));
        restaurant.setName(newRestaurant.getName());
        restaurant.setCity(newRestaurant.getCity());
        restaurant.setAveragePrice(newRestaurant.getAveragePrice());
        restaurant.setCuisineType(newRestaurant.getCuisineType());
        return restaurantRepository.save(restaurant);
    }

    @Override
    public List<Restaurant> getRestaurantsByCity(Long id, String name) {
        if (id != null)
            return restaurantRepository.findByCity_Id(id);
        else if (name != null)
            return restaurantRepository.findByCity_Name(name);
        else
            return List.of();
    }

}
