package cloudflight.integra.backend.restaurant;

import cloudflight.integra.backend.city.City;
import java.util.List;

public class RestaurantMapper {
    public static RestaurantDTO entityToDTO(Restaurant restaurant) {
        return new RestaurantDTO(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getCityId(),
                restaurant.getAddress(),
                restaurant.getOpeningHours(),
                restaurant.getDescription(),
                restaurant.getAveragePrice(),
                restaurant.getCuisineType(),
                restaurant.getRating(),
                restaurant.getImage());
    }

    public static Restaurant DTOtoEntity(RestaurantDTO restaurant_dto, City city) {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(null);
        restaurant.setName(restaurant_dto.name());
        restaurant.setCity(city);
        restaurant.setAddress(restaurant_dto.address());
        restaurant.setOpeningHours(restaurant_dto.openingHours());
        restaurant.setDescription(restaurant_dto.description());
        restaurant.setAveragePrice(restaurant_dto.averagePrice());
        restaurant.setCuisineType(restaurant_dto.cuisineType());
        restaurant.setRating(restaurant_dto.rating());
        restaurant.setImage(restaurant_dto.image());
        return restaurant;
    }

    public static List<RestaurantDTO> entityListToDTOList(List<Restaurant> restaurants) {
        return restaurants.stream().map(RestaurantMapper::entityToDTO).toList();
    }
}
