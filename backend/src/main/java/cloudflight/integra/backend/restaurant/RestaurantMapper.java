package cloudflight.integra.backend.restaurant;

import cloudflight.integra.backend.city.City;
import java.util.List;

public class RestaurantMapper {
    public static RestaurantDTO entityToDTO(Restaurant restaurant) {
        return new RestaurantDTO(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getCityId(),
                restaurant.getAveragePrice(),
                restaurant.getCuisineType());
    }

    public static Restaurant DTOtoEntity(RestaurantDTO restaurant_dto, City city) {
        Restaurant restaurant = new Restaurant()
                .setId(restaurant_dto.id())
                .setName(restaurant_dto.name())
                .setAveragePrice(restaurant_dto.averagePrice())
                .setCuisineType(restaurant_dto.cuisineType())
                .setCity(city);
        return restaurant;
    }

    public static List<RestaurantDTO> entityListToDTOList(List<Restaurant> restaurants) {
        return restaurants.stream().map(RestaurantMapper::entityToDTO).toList();
    }
}
