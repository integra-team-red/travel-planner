package cloudflight.integra.backend.mapper;

import cloudflight.integra.backend.DTO.RestaurantDTO;
import cloudflight.integra.backend.model.Restaurant;

public class RestaurantMapper {
    public static RestaurantDTO RestaurantToDTO(Restaurant restaurant) {
        return new RestaurantDTO(restaurant.getId(),
                restaurant.getName(),
                restaurant.getCityId(),
                restaurant.getAveragePrice(),
                restaurant.getCuisineType());
    }

    public static Restaurant RestaurantToEntity(RestaurantDTO restaurant_dto) {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurant_dto.id());
        restaurant.setName(restaurant_dto.name());
        restaurant.setCityId(restaurant_dto.cityId());
        restaurant.setAveragePrice(restaurant_dto.averagePrice());
        restaurant.setCuisineType(restaurant_dto.cuisineType());
        return restaurant;
    }
}
