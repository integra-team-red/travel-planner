package cloudflight.integra.backend.repository;

import cloudflight.integra.backend.model.City;

import java.util.List;

public interface CityRepository {
    City addCity(City city);
    List<City> getAllCities();
    City deleteCity(Long id);
    City updateCity(Long id, City newCity);
    City findCity(Long id);
}
