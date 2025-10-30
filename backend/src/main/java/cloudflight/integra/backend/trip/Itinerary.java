package cloudflight.integra.backend.trip;

import java.util.List;

public class Itinerary {
    List<DailyItinerary> itineraries;

    public Itinerary(List<DailyItinerary> itineraries) {
        this.itineraries = itineraries;
    }

    public List<DailyItinerary> getItineraries() {
        return itineraries;
    }

    public Itinerary setItineraries(List<DailyItinerary> itineraries) {
        this.itineraries = itineraries;
        return this;
    }
}
