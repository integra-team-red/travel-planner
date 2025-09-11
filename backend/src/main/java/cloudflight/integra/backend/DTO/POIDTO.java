package cloudflight.integra.backend.DTO;

import cloudflight.integra.backend.model.City;

public record POIDTO(Long id,
                     String name,
                     String description,
                     Long cityId,
                     Double price,
                     String type) {
}
