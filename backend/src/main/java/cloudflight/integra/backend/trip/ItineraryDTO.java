package cloudflight.integra.backend.trip;

import java.util.List;

public record ItineraryDTO(
        List<DailyItineraryDTO> dailyItineraries
) {}
