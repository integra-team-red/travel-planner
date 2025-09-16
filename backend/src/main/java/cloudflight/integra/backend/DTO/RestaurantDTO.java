package cloudflight.integra.backend.DTO;

public record RestaurantDTO(Long id, String name, Long cityId, Double averagePrice, String cuisineType) {
}
