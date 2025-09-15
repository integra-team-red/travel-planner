package cloudflight.integra.backend.DTO;

public record POIDTO(Long id,
                     String name,
                     String description,
                     Long cityId,
                     Double price,
                     String type) {}
