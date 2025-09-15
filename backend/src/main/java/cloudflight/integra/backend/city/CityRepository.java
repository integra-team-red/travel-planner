package cloudflight.integra.backend.city;

import java.util.List;

public interface CityRepository {
    City addCity(City city);

    List<City> getAllCities();

    City deleteCity(Long id);

    City updateCity(Long id, City newCity);

    City findCity(Long id);
}
