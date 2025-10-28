package cloudflight.integra.backend.trip;

public record TripDTO(Long id, String name, String city, int days, float price, Long userId) {}
