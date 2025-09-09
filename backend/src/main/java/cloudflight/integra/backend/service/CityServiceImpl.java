package cloudflight.integra.backend.service;

import cloudflight.integra.backend.model.City;
import cloudflight.integra.backend.repository.CityRepository;
import cloudflight.integra.backend.repository.InMemoryCityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityServiceImpl implements CityService {
    private CityRepository repository;

    @Autowired
    public CityServiceImpl(CityRepository repository) {
        this.repository = repository;
    }

    @Override
    public City addCity(City city) {
        return repository.addCity(city);
    }

    @Override
    public City deleteCity(Long id) {
        return repository.deleteCity(id);
    }

    @Override
    public City updateCity(Long id, City newCity) {
        if (repository.findCity(id) == null) {
            return null;
        }
        return repository.updateCity(id, newCity);
    }

    @Override
    public List<City> getAllCities() {
        return repository.getAllCities();
    }
}
