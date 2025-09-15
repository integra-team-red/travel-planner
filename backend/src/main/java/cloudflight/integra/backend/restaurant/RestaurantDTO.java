package cloudflight.integra.backend.restaurant;

public record RestaurantDTO(Long id, String name, Long cityId, Double averagePrice, String cuisineType) {
}
