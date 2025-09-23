package cloudflight.integra.backend.city;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CityServiceImpl implements CityService {

    private DBCityRepository repository;

    @Autowired
    public CityServiceImpl(DBCityRepository repository) {
        this.repository = repository;
    }

    @Override
    public City addCity(City city) {
        return repository.save(city);
    }

    @Override
    public void deleteCity(Long id) {
        repository.deleteById(id);
    }

    @Override
    public City updateCity(Long id, City newCity) {
        City dbCity = repository.findById(id).get();

        dbCity.setName(newCity.getName());
        return dbCity;
    }

    @Override
    public List<City> getAllCities() {
        return repository.findAll();
    }

    @Override
    public City getCity(Long id) {
        return repository.findById(id).get();
    }

    @Override
    public City getCity(String name) {
        return repository.findByName(name);
    }
}
