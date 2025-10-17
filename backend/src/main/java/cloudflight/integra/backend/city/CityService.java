package cloudflight.integra.backend.city;

import java.util.List;

public interface CityService {
    City addCity(City city);

    List<City> addCities(List<City> cities);

    void deleteCity(Long id);

    City updateCity(Long id, City newCity);

    List<City> getAllCities();

    City getCity(Long id);
}
