package cloudflight.integra.backend.controller;

import cloudflight.integra.backend.DTO.CityDTO;
import cloudflight.integra.backend.mapper.CityMapper;
import cloudflight.integra.backend.model.City;
import cloudflight.integra.backend.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController()
@RequestMapping("/city")
public class CityController {
    private CityService service;

    @Autowired
    public CityController(CityService cityService) {
        service = cityService;
    }

    @GetMapping()
    public ResponseEntity<List<CityDTO>> getCities() {
        List<CityDTO> cities = service.getAllCities()
                .stream()
                .map(CityMapper::CityToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(cities);
    }

    @PostMapping()
    public ResponseEntity<CityDTO> addCity(@RequestBody CityDTO cityDTO) {

        if (cityDTO.id() != 0) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
        if (!isValidName(cityDTO.name())) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
        City savedCity = service.addCity(CityMapper.CityToEntity(cityDTO));
        return ResponseEntity.ok(CityMapper.CityToDTO(savedCity));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<CityDTO> deleteCity(@PathVariable Long id) {
        City deletedCity = service.deleteCity(id);
        if (deletedCity == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(CityMapper.CityToDTO(deletedCity));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<CityDTO> updateCity(@PathVariable Long id, @RequestBody CityDTO newCityDTO) {

        if (newCityDTO.id() != 0) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
        City updatedCity = service.updateCity(id, CityMapper.CityToEntity(newCityDTO));
        if (updatedCity == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(CityMapper.CityToDTO(updatedCity));
    }

    private boolean isValidName(String cityName) {
        int nameLength = cityName.length();

        return 2 < nameLength && nameLength < 32 &&
                cityName.matches("^([A-Za-z]+(-|\\s))*[A-Za-z]+$");
    }
}
