package cloudflight.integra.backend.trip;

import cloudflight.integra.backend.coffeeShop.CoffeeShopRepository;
import cloudflight.integra.backend.poi.DBPOIRepository;
import cloudflight.integra.backend.restaurant.DBRestaurantRepository;
import cloudflight.integra.backend.spa.DBSpaRepository;
import cloudflight.integra.backend.user.DBUserRepository;
import cloudflight.integra.backend.user.User;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class TripServiceImpl implements TripService {
    private final DBTripRepository tripRepository;
    private final DBUserRepository userRepository;
    private final DBPOIRepository poiRepository;
    private final DBRestaurantRepository restaurantRepository;
    private final CoffeeShopRepository coffeeShopRepository;
    private final DBSpaRepository spaRepository;

    public TripServiceImpl(
            DBTripRepository tripRepository,
            DBUserRepository userRepository,
            DBPOIRepository poiRepository,
            DBRestaurantRepository restaurantRepository,
            CoffeeShopRepository coffeeShopRepository,
            DBSpaRepository spaRepository) {
        this.tripRepository = tripRepository;
        this.userRepository = userRepository;
        this.poiRepository = poiRepository;
        this.restaurantRepository = restaurantRepository;
        this.coffeeShopRepository = coffeeShopRepository;
        this.spaRepository = spaRepository;
    }

    @Override
    public Trip addTrip(Trip trip) {
        return tripRepository.save(trip);
    }

    @Override
    public List<Trip> addTrips(List<Trip> trips) {
        return tripRepository.saveAll(trips);
    }

    @Override
    public Trip updateTrip(Long id, Trip trip) {
        Trip existingTrip =
                tripRepository.findById(id).orElseThrow(() -> new RuntimeException("Trip not found with id: " + id));
        existingTrip.setName(trip.getName());
        existingTrip.setCity(trip.getCity());
        existingTrip.setDays(trip.getDays());
        existingTrip.setPrice(trip.getPrice());
        existingTrip.setUser(trip.getUser());
        return tripRepository.save(existingTrip);
    }

    @Override
    public void deleteTrip(Long id) {
        if (!tripRepository.existsById(id)) {
            throw new RuntimeException("Trip not found with id: " + id);
        }
        tripRepository.deleteById(id);
    }

    @Override
    public Trip getTrip(Long id) {
        return tripRepository.findById(id).orElseThrow(() -> new RuntimeException("Trip not found with id: " + id));
    }

    @Override
    public List<Trip> getAllTrips() {
        return tripRepository.findAll();
    }

    @Override
    public List<Trip> getTripsByUser(Long userId) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return tripRepository.findByUser(user);
    }

    @Override
    public Map<String, List<?>> getAttractionsCityId(Long cityId) {
        Map<String, List<?>> result = new HashMap<>();
        result.put("pois", poiRepository.findByCity_Id(cityId));
        result.put("restaurants", restaurantRepository.findByCity_Id(cityId));
        result.put("coffeeShops", coffeeShopRepository.findByCity_Id(cityId));
        result.put("spas", spaRepository.findByCity_Id(cityId));
        return result;
    }
}
