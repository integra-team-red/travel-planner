package cloudflight.integra.backend.city;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(summary = "Get a list of all cities from the repository", responses = {@ApiResponse(responseCode = "200", description = "All cities returned successfully", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CityDTO.class)))),
    })
    @GetMapping()
    public ResponseEntity<List<CityDTO>> getCities() {
        List<CityDTO> cities = service.getAllCities()
                .stream()
                .map(CityMapper::CityToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(cities);
    }

    @Operation(summary = "Add a city to the repository", responses = {@ApiResponse(responseCode = "200", description = "City added successfully; returns the added city", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CityDTO.class))), @ApiResponse(responseCode = "406", description = "Invalid city supplied", content = @Content)
    })
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

    @Operation(summary = "Delete a city from the repository", responses = {@ApiResponse(responseCode = "200", description = "City deleted successfully", content = @Content)
    })
    @DeleteMapping(value = "/{id}")
    ResponseEntity<HttpStatus> deleteCity(@PathVariable Long id) {
        service.deleteCity(id);
        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }

    @Operation(summary = "Update a city's data with the provided city", responses = {@ApiResponse(responseCode = "200", description = "City updated successfully; returns the updated city", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CityDTO.class))), @ApiResponse(responseCode = "406", description = "Invalid city supplied", content = @Content), @ApiResponse(responseCode = "404", description = "City to update not found", content = @Content)
    })
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

    @Operation(summary = "Download a JSON file of all cities from the repository to the user's computer", responses = {@ApiResponse(responseCode = "200", description = "Cities returned for download successfully", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CityDTO.class))), headers = {@Header(name = "Content-Disposition", schema = @Schema(example = "attachment; filename=\"ApprovedCities.json\"")
    )
    }),
    })
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

    @Operation(summary = "Upload an array of cities formatted in JSON to add to the repository", responses = {@ApiResponse(responseCode = "200", description = "Cities added successfully", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CityDTO.class)))), @ApiResponse(responseCode = "422", description = "One or more cities were invalid / already exist in the repository", content = @Content)
    })
    @PostMapping(value = "/upload")
    public ResponseEntity<List<CityDTO>> importApprovedCitiesFromJson(@RequestBody List<CityDTO> cities) {
        try {
            cities.forEach(cityDto -> service.addCity(CityMapper.CityToEntity(cityDto)));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .build();
        }
        return ResponseEntity.ok(CityMapper.EntityListToDTOList(service.getAllCities()));
    }

    private boolean isValidName(String cityName) {
        int nameLength = cityName.length();

        return 2 < nameLength && nameLength < 32 && cityName.matches("^([A-Za-z]+(-|\\s))*[A-Za-z]+$");
    }
}
