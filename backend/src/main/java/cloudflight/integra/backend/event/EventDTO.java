package cloudflight.integra.backend.event;

import java.util.Date;

public record EventDTO(
        Long id,
        String name,
        String description,
        Date startTime,
        Date endTime,
        Long poiId,
        Double price,
        String audience) {}
