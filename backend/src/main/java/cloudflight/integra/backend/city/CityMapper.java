package cloudflight.integra.backend.city;

import java.util.List;

public class CityMapper {
    public static CityDTO entityToDTO(City city) {

        return new CityDTO(city.getId(), city.getName());
    }

    public static City DTOtoEntity(CityDTO city_dto) {
        City city = new City();
        city.setId(null);
        city.setName(city_dto.name());
        return city;
    }

    public static List<CityDTO> entityListToDTOList(List<City> cities) {
        return cities.stream().map(CityMapper::entityToDTO).toList();
    }
}
