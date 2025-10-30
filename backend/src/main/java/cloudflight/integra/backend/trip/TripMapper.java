package cloudflight.integra.backend.trip;

import cloudflight.integra.backend.user.User;
import java.util.List;

public class TripMapper {
    public static TripDTO entityToDTO(Trip trip) {
        return new TripDTO(
                trip.getId(),
                trip.getName(),
                trip.getCity(),
                trip.getDays(),
                trip.getPrice(),
                trip.getUser() != null ? trip.getUser().id : null);
    }

    public static Trip DTOtoEntity(TripDTO tripDTO, User user) {
        Trip trip = new Trip();
        trip.setId(tripDTO.id());
        trip.setName(tripDTO.name());
        trip.setCity(tripDTO.city());
        trip.setDays(tripDTO.days());
        trip.setPrice(tripDTO.price());
        trip.setUser(user);
        return trip;
    }

    public static List<TripDTO> entityListToDTOList(List<Trip> trips) {
        return trips.stream().map(TripMapper::entityToDTO).toList();
    }
}
