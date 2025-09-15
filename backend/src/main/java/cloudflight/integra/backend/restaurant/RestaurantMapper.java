package cloudflight.integra.backend.restaurant;

import cloudflight.integra.backend.city.City;
import cloudflight.integra.backend.restaurant.Restaurant;
import java.util.List;

public class RestaurantMapper {
    public static RestaurantDTO RestaurantToDTO(Restaurant restaurant) {
        return new RestaurantDTO(
                                 restaurant.getId(),
                                 restaurant.getName(),
                                 restaurant.getCity() != null ? restaurant.getCity()
                                         .getId() : null,
                                 restaurant.getAveragePrice(),
                                 restaurant.getCuisineType()
        );
    }

    public static Restaurant RestaurantToEntity(RestaurantDTO restaurant_dto, City city) {
        Restaurant restaurant = new Restaurant()
                .setId(restaurant_dto.id())
                .setName(restaurant_dto.name())
                .setAveragePrice(restaurant_dto.averagePrice())
                .setCuisineType(restaurant_dto.cuisineType())
                .setCity(city);
        return restaurant;
    }

    public static List<RestaurantDTO> EntityListToDTOList(List<Restaurant> restaurants) {
        return restaurants.stream()
                .map(RestaurantMapper::RestaurantToDTO)
                .toList();
    }
}
