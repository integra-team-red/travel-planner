package cloudflight.integra.backend.spa;

import cloudflight.integra.backend.city.City;

public class SpaMapper {
    public static SpaDTO SpaToDTO(Spa spa) {
        return new SpaDTO(
                spa.getId(),
                spa.getName(),
                spa.getCity().getId(),
                spa.getPriceLowerBound(),
                spa.getPriceUpperBound(),
                spa.getSchedule(),
                spa.getRating(),
                spa.getDescription());
    }

    public static Spa SpaToEntity(SpaDTO spaDTO, City city) {
        return new Spa()
                .setId(spaDTO.id())
                .setName(spaDTO.name())
                .setCity(city)
                .setPriceLowerBound(spaDTO.priceLowerBound())
                .setPriceUpperBound(spaDTO.priceUpperBound())
                .setSchedule(spaDTO.schedule())
                .setRating(spaDTO.rating())
                .setDescription(spaDTO.description());
    }
}
