package cloudflight.integra.backend.restaurant;

import cloudflight.integra.backend.validation.GenericConstraintValidator;
import cloudflight.integra.backend.validation.PageRequestValidator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class RestaurantServiceImpl implements RestaurantService {
    private final RestaurantRepository repository;
    private final GenericConstraintValidator<Restaurant> validator;

    @Autowired
    public RestaurantServiceImpl(RestaurantRepository repository, GenericConstraintValidator<Restaurant> validator) {
        this.validator = validator;
        this.repository = repository;
    }

    @Override
    public Restaurant addRestaurant(Restaurant restaurant) {
        validator.validate(restaurant);
        return repository.save(restaurant);
    }

    public List<Restaurant> addRestaurants(List<Restaurant> restaurants) {
        try {
            restaurants.forEach(validator::validate);
        } catch (ConstraintViolationException err) {
            throw new ConstraintViolationException(
                    "One or more restaurants were invalid:\n" + err.getMessage(), err.getConstraintViolations());
        }
        return repository.saveAll(restaurants);
    }

    @Override
    public Restaurant updateRestaurant(Long id, Restaurant newRestaurant) {
        validator.validate(newRestaurant);
        var restaurant = getRestaurant(id);

        restaurant.setName(newRestaurant.getName());
        restaurant.setCity(newRestaurant.getCity());
        restaurant.setAddress(newRestaurant.getAddress());
        restaurant.setOpeningHours(newRestaurant.getOpeningHours());
        restaurant.setDescription(newRestaurant.getDescription());
        restaurant.setAveragePrice(newRestaurant.getAveragePrice());
        restaurant.setCuisineType(newRestaurant.getCuisineType());
        restaurant.setRating(newRestaurant.getRating());
        restaurant.setImage(newRestaurant.getImage());

        return repository.save(restaurant);
    }

    @Override
    public void deleteRestaurant(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Restaurant getRestaurant(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("No restaurant id provided");
        }
        var restaurantWrapper = repository.findById(id);
        if (restaurantWrapper.isEmpty()) {
            throw new EntityNotFoundException("Restaurant with the provided id not found");
        }
        return restaurantWrapper.get();
    }

    @Override
    public List<Restaurant> getAllRestaurants() {
        return repository.findAll();
    }

    @Override
    public List<Restaurant> getAllRestaurantsSortedByName(int pageNumber, int pageSize, boolean isDescending) {
        PageRequestValidator.validate(pageNumber, pageSize);
        var sortingDirection = isDescending ? Sort.Direction.DESC : Sort.Direction.ASC;
        return repository
                .findAll(PageRequest.of(pageNumber, pageSize, Sort.by(sortingDirection, "name")))
                .toList();
    }

    @Override
    public List<Restaurant> getAllRestaurantsSortedByAveragePrice(int pageNumber, int pageSize, boolean isDescending) {
        PageRequestValidator.validate(pageNumber, pageSize);
        var sortingDirection = isDescending ? Sort.Direction.DESC : Sort.Direction.ASC;
        return repository
                .findAll(PageRequest.of(pageNumber, pageSize, Sort.by(sortingDirection, "average_price")))
                .toList();
    }

    @Override
    public List<Restaurant> getAllRestaurantsByCuisine(int pageNumber, int pageSize, String cuisineType) {
        PageRequestValidator.validate(pageNumber, pageSize);
        return repository
                .findAllByCuisine(cuisineType, PageRequest.of(pageNumber, pageSize))
                .toList();
    }

    @Override
    public List<Restaurant> getRestaurantsByCity(Long id, String name) {
        if (id != null) return repository.findByCity_Id(id);
        else if (name != null) return repository.findByCity_Name(name);
        else return List.of();
    }
}
