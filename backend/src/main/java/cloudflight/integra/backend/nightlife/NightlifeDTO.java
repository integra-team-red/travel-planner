package cloudflight.integra.backend.nightlife;

public record NightlifeDTO(
        Long id,
        String name,
        Long cityId,
        NightlifeType type,
        Double priceLowerBound,
        Double priceUpperBound,
        String schedule,
        Long rating,
        String description) {}
