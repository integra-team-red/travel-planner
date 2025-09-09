package cloudflight.integra.backend.repository;

import cloudflight.integra.backend.model.City;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public class InMemoryCityRepository implements CityRepository {
    private HashMap<Long, City> cities;
    private Long lastId = 1L;

    private Long generateId() {
        return lastId++;
    }

    public InMemoryCityRepository() {
        cities = new HashMap<>();
    }

    @Override
    public City addCity(City city) {
        city.setId(generateId());
        return cities.put(city.getId(), city);
    }

    @Override
    public List<City> getAllCities() {
        return cities.values().stream().toList();
    }

    @Override
    public City deleteCity(Long id) {
        return cities.remove(id);
    }

    @Override
    public City updateCity(Long id, City newCity) {
        return cities.put(id, newCity);
    }

    public City findCity(Long id) {
        return cities.get(id);
    }
}
