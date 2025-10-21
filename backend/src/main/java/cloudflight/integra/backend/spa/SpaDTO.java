package cloudflight.integra.backend.spa;

public record SpaDTO(
        Long id,
        String name,
        Long cityId,
        Double priceLowerBound,
        Double priceUpperBound,
        String schedule,
        Long rating,
        String description) {}
