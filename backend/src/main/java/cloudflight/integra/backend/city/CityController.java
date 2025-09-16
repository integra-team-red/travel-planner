package cloudflight.integra.backend.city;

import cloudflight.integra.backend.service.CityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.stream.Collectors;

@RestController()
@RequestMapping("/city")
public class CityController {
    private final CityService service;

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

        if (cityDTO.id() != null && cityDTO.id() != 0) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .build();
        }
        if (!isValidName(cityDTO.name())) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .build();
        }
        City savedCity = service.addCity(CityMapper.CityToEntity(cityDTO));
        return ResponseEntity.ok(CityMapper.CityToDTO(savedCity));
    }

    @DeleteMapping(value = "/{id}")
    ResponseEntity<HttpStatus> deleteCity(@PathVariable Long id) {
        service.deleteCity(id);
        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<CityDTO> updateCity(@PathVariable Long id, @RequestBody CityDTO newCityDTO) {

        if (newCityDTO.id() != null && newCityDTO.id() != 0) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .build();
        }
        City updatedCity = service.updateCity(id, CityMapper.CityToEntity(newCityDTO));
        if (updatedCity == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .build();
        }
        return ResponseEntity.ok(CityMapper.CityToDTO(updatedCity));
    }

    private boolean isValidName(String cityName) {
        int nameLength = cityName.length();

        return 2 < nameLength && nameLength < 32 && cityName.matches("^([A-Za-z]+(-|\\s))*[A-Za-z]+$");
    }

    @GetMapping(value = "/download")
    public ResponseEntity<String> exportApprovedCitiesToJson() throws IOException {
        final var jsonMapper = new ObjectMapper();
        final var outputStream = new StringWriter();

        jsonMapper.writeValue(outputStream, CityMapper.EntityListToDTOList(service.getAllCities()));
        return ResponseEntity
                .status(HttpStatus.OK)
                .header("Content-Disposition", "attachment; filename=\"ApprovedCities.json\"")
                .contentLength(outputStream.getBuffer()
                        .length())
                .contentType(MediaType.APPLICATION_JSON)
                .body(outputStream.toString());
    }

    @PostMapping(value = "/upload")
    public ResponseEntity<List<CityDTO>> importApprovedCitiesFromJson(@RequestBody List<CityDTO> cities) {
        cities.forEach(cityDto -> service.addCity(CityMapper.CityToEntity(cityDto)));
        return ResponseEntity.ok(CityMapper.EntityListToDTOList(service.getAllCities()));
    }
}
