package cloudflight.integra.backend.service;

import cloudflight.integra.backend.model.City;

import java.util.List;

public interface CityService {
    City addCity(City city);
    City deleteCity(int id);
    City updateCity(int id, City newCity);
    List<City> getAllCities();
}
