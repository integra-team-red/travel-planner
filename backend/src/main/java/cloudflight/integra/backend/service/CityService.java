package cloudflight.integra.backend.service;

import cloudflight.integra.backend.model.City;

import java.util.List;

public interface CityService {
    City addCity(City city);
    City deleteCity(Long id);
    City updateCity(Long id, City newCity);
    List<City> getAllCities();
}
