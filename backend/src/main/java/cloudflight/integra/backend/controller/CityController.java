package cloudflight.integra.backend.controller;

import cloudflight.integra.backend.model.City;
import cloudflight.integra.backend.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController(value = "/city")
public class CityController {
    private CityService service;

    @Autowired
    public CityController(CityService cityService) {
        service = cityService;
    }

    @GetMapping()
    public ResponseEntity<List<City>> getCities() {
        return ResponseEntity.ok(service.getAllCities());
    }

    @PostMapping()
    public ResponseEntity<City> addCity(@RequestBody City city) {
        /// TODO: Remove this if statement after implementing DTOs
        if (city.getId() != 0) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
        if (!isValidName(city.getName())) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
        return ResponseEntity.ok(service.addCity(city));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<City> deleteCity(@PathVariable int id) {
        City deletedCity = service.deleteCity(id);
        if (deletedCity == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(deletedCity);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<City> updateCity(@PathVariable int id, @RequestBody City newCity) {
        /// TODO: Remove this if statement after implementing DTOs
        if (newCity.getId() != 0) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
        City updatedCity = service.updateCity(id, newCity);
        if (updatedCity == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(updatedCity);
    }

    private boolean isValidName(String cityName) {
        int nameLength = cityName.length();

        return 2 < nameLength && nameLength < 32 &&
                cityName.matches("^([A-Za-z]+(-|\\s))*[A-Za-z]+$");
    }
}
