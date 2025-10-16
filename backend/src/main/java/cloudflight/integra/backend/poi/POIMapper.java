package cloudflight.integra.backend.poi;

import cloudflight.integra.backend.city.City;
import java.util.List;

public class POIMapper {
    public static POIDTO POIToDTO(POI poi) {
        return new POIDTO(
                poi.getId(),
                poi.getName(),
                poi.getDescription(),
                poi.getAddress(),
                poi.getCityId(),
                poi.getPrice(),
                poi.getType() != null ? poi.getType().name() : null,
                poi.getImage());
    }

    public static POI POIToEntity(POIDTO poi_dto, City city) {
        POI poi = new POI();
        poi.setId(null);
        poi.setName(poi_dto.name());
        poi.setDescription(poi_dto.description());
        poi.setAddress(poi_dto.address());
        poi.setCity(city);
        poi.setPrice(poi_dto.price());
        poi.setType(poi_dto.type() != null ? PointOfInterestType.valueOf(poi_dto.type()) : null);
        poi.setImage(poi_dto.image());
        return poi;
    }

    public static List<POIDTO> EntityListToDTOList(List<POI> entityList) {
        return entityList.stream().map(POIMapper::POIToDTO).toList();
    }
}
