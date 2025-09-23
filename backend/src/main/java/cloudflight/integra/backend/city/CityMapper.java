package cloudflight.integra.backend.city;

import java.util.List;

public class CityMapper {
    public static CityDTO CityToDTO(City city) {

        return new CityDTO(city.getId(), city.getName());
    }

    public static City CityToEntity(CityDTO city_dto) {
        City city = new City();
        city.setId(null);
        city.setName(city_dto.name());
        return city;
    }

    public static List<CityDTO> EntityListToDTOList(List<City> cities) {
        return cities.stream().map(CityMapper::CityToDTO).toList();
    }
}
