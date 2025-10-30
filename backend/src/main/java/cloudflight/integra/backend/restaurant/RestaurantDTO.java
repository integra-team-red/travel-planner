package cloudflight.integra.backend.restaurant;

public record RestaurantDTO(
        Long id,
        String name,
        Long cityId,
        String address,
        String openingHours,
        String description,
        Double averagePrice,
        String cuisineType,
        Double rating,
        String image) {}
