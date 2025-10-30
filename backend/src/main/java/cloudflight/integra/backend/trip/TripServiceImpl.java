package cloudflight.integra.backend.trip;

import cloudflight.integra.backend.coffeeShop.CoffeeShop;
import cloudflight.integra.backend.coffeeShop.CoffeeShopRepository;
import cloudflight.integra.backend.poi.POI;
import cloudflight.integra.backend.poi.POIRepository;
import cloudflight.integra.backend.restaurant.DBRestaurantRepository;
import cloudflight.integra.backend.restaurant.Restaurant;
import cloudflight.integra.backend.spa.SpaRepository;
import cloudflight.integra.backend.user.User;
import cloudflight.integra.backend.user.UserRepository;
import java.util.*;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class TripServiceImpl implements TripService {
    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final POIRepository poiRepository;
    private final DBRestaurantRepository restaurantRepository;
    private final CoffeeShopRepository coffeeShopRepository;
    private final SpaRepository spaRepository;

    public TripServiceImpl(
            TripRepository tripRepository,
            UserRepository userRepository,
            POIRepository poiRepository,
            DBRestaurantRepository restaurantRepository,
            CoffeeShopRepository coffeeShopRepository,
            SpaRepository spaRepository) {
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

    @Override
    public Itinerary generateItinerary(Trip trip) {
        List<POI> pois = poiRepository.findByCity_Id(trip.getCity().getId(), Sort.by(Sort.Direction.ASC, "price"));
        List<Restaurant> restaurants =
                restaurantRepository.findByCity_Id(trip.getCity().getId(), Sort.by(Sort.Direction.ASC, "averagePrice"));
        List<CoffeeShop> coffeeShops =
                coffeeShopRepository.findByCity_Id(trip.getCity().getId(), Sort.by(Sort.Direction.ASC, "averagePrice"));

        int days = trip.getDays();
        double maxBudget = trip.getPrice();

        if (pois.size() < days * 3) {
            throw new IllegalStateException("Not enough POIs for " + days + " days (need " + days * 3 + ")");
        }

        List<Restaurant> chosenRestaurants = new ArrayList<>(Collections.nCopies(days, restaurants.getFirst()));
        List<CoffeeShop> chosenCoffeeShops = new ArrayList<>(Collections.nCopies(days, coffeeShops.getFirst()));
        List<POI> chosenPOIs = new ArrayList<>(pois.subList(0, days * 3));

        double total = calculateTotal(chosenRestaurants, chosenCoffeeShops, chosenPOIs);

        if (total > maxBudget) {
            maxBudget += 0.2 * maxBudget;
        }

        if (total > maxBudget) {
            throw new IllegalArgumentException("The minimum cost for the trip exceeds the budget");
        }

        int poiNextIndex = days * 3;
        boolean improved = true;

        while (improved) {
            improved = false;
            double bestGain = Double.MAX_VALUE;
            Runnable bestUpgrade = null;

            for (int day = 0; day < days; day++) {
                Restaurant current = chosenRestaurants.get(day);
                int nextIndex = findNextMoreExpensive(restaurants, current.getAveragePrice());
                if (nextIndex != -1) {
                    double gain = restaurants.get(nextIndex).getAveragePrice() - current.getAveragePrice();
                    if (gain > 0 && total + gain <= maxBudget && gain < bestGain) {
                        bestGain = gain;
                        int finalDay = day;
                        bestUpgrade = () -> chosenRestaurants.set(finalDay, restaurants.get(nextIndex));
                    }
                }
            }

            for (int day = 0; day < days; day++) {
                CoffeeShop current = chosenCoffeeShops.get(day);
                int nextIndex = findNextMoreExpensive(coffeeShops, current.getAveragePrice());
                if (nextIndex != -1) {
                    double gain = coffeeShops.get(nextIndex).getAveragePrice() - current.getAveragePrice();
                    if (gain > 0 && total + gain <= maxBudget && gain < bestGain) {
                        bestGain = gain;
                        int finalDay = day;
                        bestUpgrade = () -> chosenCoffeeShops.set(finalDay, coffeeShops.get(nextIndex));
                    }
                }
            }

            if (poiNextIndex < pois.size()) {
                POI cheapestChosen = chosenPOIs.stream()
                        .min(Comparator.comparingDouble(POI::getPrice))
                        .orElse(null);
                if (cheapestChosen != null) {
                    double gain = pois.get(poiNextIndex).getPrice() - cheapestChosen.getPrice();
                    if (gain > 0 && total + gain <= maxBudget && gain < bestGain) {
                        int finalPoiNextIndex = poiNextIndex;
                        bestGain = gain;
                        bestUpgrade = () -> {
                            chosenPOIs.remove(cheapestChosen);
                            chosenPOIs.add(pois.get(finalPoiNextIndex));
                        };
                    }
                }
            }

            if (bestUpgrade != null) {
                bestUpgrade.run();
                total += bestGain;
                improved = true;
                poiNextIndex += (bestGain < 0.001) ? 0 : 1;
            }
        }

        Collections.shuffle(chosenRestaurants);
        Collections.shuffle(chosenCoffeeShops);
        Collections.shuffle(chosenPOIs);

        List<DailyItinerary> itineraries = new ArrayList<>();
        int poiIndex = 0;
        for (int day = 0; day < days; day++) {
            itineraries.add(new DailyItinerary(
                    chosenRestaurants.get(day),
                    chosenCoffeeShops.get(day),
                    chosenPOIs.get(poiIndex++),
                    chosenPOIs.get(poiIndex++),
                    chosenPOIs.get(poiIndex++)));
        }

        return new Itinerary(itineraries);
    }

    private int findNextMoreExpensive(List<?> list, double currentPrice) {
        for (int i = 0; i < list.size(); i++) {
            double price = 0;
            if (list.get(i) instanceof Restaurant r) price = r.getAveragePrice();
            else if (list.get(i) instanceof CoffeeShop c) price = c.getAveragePrice();
            if (price > currentPrice) return i;
        }
        return -1;
    }

    private double calculateTotal(List<Restaurant> restaurants, List<CoffeeShop> coffeeShops, List<POI> pois) {
        return restaurants.stream().mapToDouble(Restaurant::getAveragePrice).sum()
                + coffeeShops.stream().mapToDouble(CoffeeShop::getAveragePrice).sum()
                + pois.stream().mapToDouble(POI::getPrice).sum();
    }
}
