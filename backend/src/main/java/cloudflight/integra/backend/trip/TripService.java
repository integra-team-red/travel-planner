package cloudflight.integra.backend.trip;

import java.util.List;
import java.util.Map;

public interface TripService {
    Trip addTrip(Trip trip);

    List<Trip> addTrips(List<Trip> trips);

    Trip updateTrip(Long id, Trip trip);

    void deleteTrip(Long id);

    Trip getTrip(Long id);

    List<Trip> getAllTrips();

    List<Trip> getTripsByUser(Long userId);

    Map<String, List<?>> getAttractionsCityId(Long cityId);

    Itinerary generateItinerary(Trip trip);
    }
