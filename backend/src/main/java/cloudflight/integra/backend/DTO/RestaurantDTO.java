package cloudflight.integra.backend.DTO;

public record RestaurantDTO(long id, String name, long cityId, double averagePrice, String cuisineType) {
}
