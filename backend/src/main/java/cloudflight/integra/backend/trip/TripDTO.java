package cloudflight.integra.backend.trip;

public record TripDTO(Long id, String name, Long cityId, int days, float price, Long userId) {}
