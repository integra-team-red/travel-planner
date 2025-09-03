package cloudflight.integra.backend.repository;

import cloudflight.integra.backend.model.City;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public class InMemoryCityRepository implements CityRepository {
    private HashMap<Integer, City> cities;
    private int lastId = 1;

    private int generateId() {
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
    public City deleteCity(int id) {
        return cities.remove(id);
    }

    @Override
    public City updateCity(int id, City newCity) {
        return cities.put(id, newCity);
    }

    public City findCity(int id) {
        return cities.get(id);
    }
}
