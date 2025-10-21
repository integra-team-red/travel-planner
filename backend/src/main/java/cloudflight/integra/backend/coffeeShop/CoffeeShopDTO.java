package cloudflight.integra.backend.coffeeShop;

public record CoffeeShopDTO(
        Long id,
        String name,
        Long cityId,
        String address,
        String openingHours,
        String description,
        Double averagePrice,
        Double rating,
        String image) {}
