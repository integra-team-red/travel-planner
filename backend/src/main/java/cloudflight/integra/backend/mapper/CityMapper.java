package cloudflight.integra.backend.mapper;

import cloudflight.integra.backend.DTO.CityDTO;
import cloudflight.integra.backend.model.City;

public class CityMapper {
    public static CityDTO CityToDTO(City city){

        return new CityDTO(city.getId(),
                city.getName());
    }

    public static City CityToEntity(CityDTO city_dto){
        City city = new City();
        city.setId(city_dto.id());
        city.setName(city_dto.name());
        return city;
    }
}
