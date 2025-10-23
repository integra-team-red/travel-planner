package cloudflight.integra.backend.spa;

import cloudflight.integra.backend.city.City;
import cloudflight.integra.backend.city.CityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/spa")
@SecurityRequirement(name = "bearerAuth")
public class SpaController {
    private final SpaService service;
    private final CityService cityService;

    @Autowired
    public SpaController(SpaService service, CityService cityService) {
        this.service = service;
        this.cityService = cityService;
    }

    @Operation(
            summary = "Get a list of all spas from the repository",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "All spas returned successfully",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        array = @ArraySchema(schema = @Schema(implementation = SpaDTO.class)))),
            })
    @GetMapping
    public ResponseEntity<List<SpaDTO>> getSpas() {
        List<SpaDTO> spas =
                service.getAllSpas().stream().map(SpaMapper::SpaToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(spas);
    }

    @Operation(
            summary = "Add a spa to the repository",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Spa added successfully; returns the added spa",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = SpaDTO.class))),
                @ApiResponse(responseCode = "422", description = "Invalid spa supplied", content = @Content),
                @ApiResponse(responseCode = "404", description = "City ID of the spa", content = @Content)
            })
    @PostMapping
    public ResponseEntity<SpaDTO> addSpa(@RequestBody SpaDTO spaDTO) {
        City city = cityService.getCity(spaDTO.cityId());
        Spa savedSpa = service.addSpa(SpaMapper.SpaToEntity(spaDTO, city));
        return ResponseEntity.ok(SpaMapper.SpaToDTO(savedSpa));
    }

    @Operation(
            summary = "Delete a spa from the repository",
            responses = {
                @ApiResponse(responseCode = "200", description = "Spa deleted successfully", content = @Content)
            })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<SpaDTO> deleteSpa(@PathVariable Long id) {
        service.deleteSpa(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(
            summary = "Update a Spa's data with the provided Spa",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Spa updated successfully",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = SpaDTO.class))),
                @ApiResponse(responseCode = "404", description = "Spa to update not found", content = @Content),
                @ApiResponse(responseCode = "404", description = "City id of the spa not found", content = @Content),
                @ApiResponse(responseCode = "422", description = "Invalid spa supplied", content = @Content)
            })
    @PutMapping(value = "/{id}")
    public ResponseEntity<SpaDTO> updateSpa(@PathVariable Long id, @RequestBody SpaDTO newSpaDTO) {
        City city = cityService.getCity(newSpaDTO.cityId());
        Spa updatedSpa = service.updateSpa(id, SpaMapper.SpaToEntity(newSpaDTO, city));
        return ResponseEntity.ok(SpaMapper.SpaToDTO(updatedSpa));
    }
}
