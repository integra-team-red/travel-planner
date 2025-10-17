package cloudflight.integra.backend.city;

import cloudflight.integra.backend.validation.GenericConstraintValidator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CityServiceImpl implements CityService {
    private final DBCityRepository repository;
    private final GenericConstraintValidator<City> validator;

    @Autowired
    public CityServiceImpl(DBCityRepository repository, GenericConstraintValidator<City> validator) {
        this.validator = validator;
        this.repository = repository;
    }

    @Override
    public City addCity(City city) {
        validator.validate(city);
        return repository.save(city);
    }

    @Override
    public List<City> addCities(List<City> cities) {
        try {
            cities.forEach(validator::validate);
        } catch (ConstraintViolationException err) {
            throw new ConstraintViolationException(
                    "One or more cities were invalid:\n" + err.getMessage(), err.getConstraintViolations());
        }
        return repository.saveAll(cities);
    }

    @Override
    public City updateCity(Long id, City newCity) {
        validator.validate(newCity);
        var dbCity = getCity(id);

        dbCity.setName(newCity.getName());

        return repository.save(dbCity);
    }

    @Override
    public void deleteCity(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<City> getAllCities() {
        return repository.findAll();
    }

    @Override
    public City getCity(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("No city id provided");
        }
        var cityWrapper = repository.findById(id);
        if (cityWrapper.isEmpty()) {
            throw new EntityNotFoundException("City with the provided id not found");
        }
        return cityWrapper.get();
    }
}
