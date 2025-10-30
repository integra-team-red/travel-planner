package cloudflight.integra.backend.nightlife;

import cloudflight.integra.backend.city.City;

// NOTE(MC): If PO approves of org.mapstruct then use that here (last update was 1 year ago)
public class NightlifeMapper {
    public static NightlifeDTO entityToDTO(Nightlife entity) {
        return new NightlifeDTO(
                entity.getId(),
                entity.getName(),
                entity.getCity().getId(),
                entity.getType(),
                entity.getPriceLowerBound(),
                entity.getPriceUpperBound(),
                entity.getSchedule(),
                entity.getRating(),
                entity.getDescription());
    }

    public static Nightlife DTOtoEntity(NightlifeDTO DTO, City city) {
        return new Nightlife()
                .setId(DTO.id())
                .setName(DTO.name())
                .setCity(city)
                .setType(DTO.type())
                .setPriceLowerBound(DTO.priceLowerBound())
                .setPriceUpperBound(DTO.priceUpperBound())
                .setSchedule(DTO.schedule())
                .setRating(DTO.rating())
                .setDescription(DTO.description());
    }
}
