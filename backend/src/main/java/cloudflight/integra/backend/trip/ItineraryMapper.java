package cloudflight.integra.backend.trip;

import cloudflight.integra.backend.restaurant.Restaurant;
import cloudflight.integra.backend.coffeeShop.CoffeeShop;

import java.util.List;

public class ItineraryMapper {

    public static ItineraryDTO ItineraryToDTO(Itinerary itinerary) {
        List<DailyItineraryDTO> itineraryDTOs = itinerary.getItineraries().stream()
                .map(ItineraryMapper::DailyItineraryToDTO)
                .toList();

        return new ItineraryDTO(itineraryDTOs);
    }

    public static DailyItineraryDTO DailyItineraryToDTO(DailyItinerary daily) {
        return new DailyItineraryDTO(
                daily.getRestaurant() != null ? daily.getRestaurant().getId() : null,
                daily.getCoffeeShop() != null ? daily.getCoffeeShop().getId() : null,
                daily.getMorningActivity(),
                daily.getAfternoonActivity(),
                daily.getEveningActivity()
        );
    }

    public static Itinerary ItineraryToEntity(ItineraryDTO itineraryDTO,
                                              List<DailyItinerary> dailyItineraries) {
        // Here we construct the Itinerary by passing pre-constructed DailyItinerary entities
        // (just like your TripToEntity expects a city and a user to be passed in)
        return new Itinerary(dailyItineraries);
    }

    public static DailyItinerary DailyItineraryToEntity(DailyItineraryDTO dto,
                                                        Restaurant restaurant,
                                                        CoffeeShop coffeeShop) {
        return new DailyItinerary(
                restaurant,
                coffeeShop,
                dto.morningActivity(),
                dto.afternoonActivity(),
                dto.eveningActivity()
        );
    }

    public static List<ItineraryDTO> EntityListToDTOList(List<Itinerary> itineraries) {
        return itineraries.stream().map(ItineraryMapper::ItineraryToDTO).toList();
    }
}
