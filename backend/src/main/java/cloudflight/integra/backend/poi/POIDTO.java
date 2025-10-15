package cloudflight.integra.backend.poi;

public record POIDTO(
        Long id,
        String name,
        String description,
        String address,
        Long cityId,
        Double price,
        String type,
        String image) {}
