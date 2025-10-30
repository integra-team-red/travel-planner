package cloudflight.integra.backend.trip;

public record DailyItineraryDTO(
        Long restaurantId,
        Long coffeeShopId,
        Object morningActivity,
        Object afternoonActivity,
        Object eveningActivity
) {}
