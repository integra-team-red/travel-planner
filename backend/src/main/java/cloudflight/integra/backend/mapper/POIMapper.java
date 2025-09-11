package cloudflight.integra.backend.mapper;

import cloudflight.integra.backend.DTO.POIDTO;
import cloudflight.integra.backend.model.City;
import cloudflight.integra.backend.model.PointOfInterest;

public class POIMapper {
    public static POIDTO POIToDTO(PointOfInterest poi) {
        return new POIDTO(poi.getId(),
                poi.getName(),
                poi.getDescription(),
                poi.getCityId(),
                poi.getPrice(),
                poi.getType() != null ? poi.getType().name() : null
        );
    }

    public static PointOfInterest POIToEntity(POIDTO poi_dto, City city) {
        PointOfInterest poi = new PointOfInterest();
        poi.setId(poi_dto.id());
        poi.setName(poi_dto.name());
        poi.setDescription(poi_dto.description());
        poi.setCity(city);
        poi.setPrice(poi_dto.price());
        poi.setType(poi_dto.type() != null
                ? PointOfInterest.PointOfInterestType.valueOf(poi_dto.type()) : null);
        return poi;
    }
}
