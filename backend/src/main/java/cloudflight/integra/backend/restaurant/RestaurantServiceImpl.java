package cloudflight.integra.backend.restaurant;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantServiceImpl implements RestaurantService {
    private final DBRestaurantRepository repository;

    @Autowired
    public RestaurantServiceImpl(DBRestaurantRepository repository) {
        this.repository = repository;
    }

    @Override
    public Restaurant addRestaurant(Restaurant restaurant) {

        return repository.save(restaurant);
    }

    @Override
    public List<Restaurant> getAllRestaurants() { return repository.findAll(); }

    @Override
    public void deleteRestaurant(Long id) {
        repository.deleteById(id);

    }

    @Override
    public Restaurant updateRestaurant(Long id, Restaurant newRestaurant) {
        Restaurant restaurant = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant with id " + id + " not found"));
        restaurant.setName(newRestaurant.getName());
        restaurant.setCity(newRestaurant.getCity());
        restaurant.setAveragePrice(newRestaurant.getAveragePrice());
        restaurant.setCuisineType(newRestaurant.getCuisineType());
        return repository.save(restaurant);
    }

    @Override
    public List<Restaurant> getAllRestaurantsSortedByName(int pageNumber, int pageSize, boolean isDescending) {
        var sortingDirection = isDescending ? Sort.Direction.DESC : Sort.Direction.ASC;
        return repository.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(sortingDirection, "name")))
                .toList();
    }

    @Override
    public List<Restaurant> getAllRestaurantsSortedByAveragePrice(int pageNumber, int pageSize, boolean isDescending) {
        var sortingDirection = isDescending ? Sort.Direction.DESC : Sort.Direction.ASC;
        return repository.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(sortingDirection, "average_price")))
                .toList();
    }

    @Override
    public List<Restaurant> getAllRestaurantsByCuisine(int pageNumber, int pageSize, String cuisineType) {
        return repository.findAllByCuisine(cuisineType, PageRequest.of(pageNumber, pageSize))
                .toList();
    }
}
