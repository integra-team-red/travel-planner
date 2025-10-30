package cloudflight.integra.backend.nightlife;

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
@RequestMapping(value = "/api/admin/nightlife")
@SecurityRequirement(name = "bearerAuth")
public class NightlifeController {
    private final NightlifeService service;
    private final CityService cityService;

    @Autowired
    public NightlifeController(NightlifeService service, CityService cityService) {
        this.service = service;
        this.cityService = cityService;
    }

    @Operation(
            summary = "Get a list of all nightlifes from the repository",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "All nightlifes returned successfully",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        array = @ArraySchema(schema = @Schema(implementation = NightlifeDTO.class)))),
            })
    @GetMapping
    public ResponseEntity<List<NightlifeDTO>> getNightlifes() {
        List<NightlifeDTO> nightlifes =
                service.getAll().stream().map(NightlifeMapper::entityToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(nightlifes);
    }

    @Operation(
            summary = "Add a nightlife to the repository",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Nightlife added successfully; returns the added nightlife",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = NightlifeDTO.class))),
                @ApiResponse(responseCode = "422", description = "Invalid nightlife supplied", content = @Content),
                @ApiResponse(responseCode = "404", description = "City ID of the nightlife", content = @Content)
            })
    @PostMapping
    public ResponseEntity<NightlifeDTO> addNightlife(@RequestBody NightlifeDTO nightlifeDTO) {
        City city = cityService.getCity(nightlifeDTO.cityId());
        Nightlife savedNightlife = service.add(NightlifeMapper.DTOtoEntity(nightlifeDTO, city));
        return ResponseEntity.ok(NightlifeMapper.entityToDTO(savedNightlife));
    }

    @Operation(
            summary = "Delete a nightlife from the repository",
            responses = {
                @ApiResponse(responseCode = "200", description = "Nightlife deleted successfully", content = @Content)
            })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<NightlifeDTO> deleteNightlife(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(
            summary = "Update a Nightlife's data with the provided Nightlife",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Nightlife updated successfully",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = NightlifeDTO.class))),
                @ApiResponse(responseCode = "404", description = "Nightlife to update not found", content = @Content),
                @ApiResponse(
                        responseCode = "404",
                        description = "City id of the nightlife not found",
                        content = @Content),
                @ApiResponse(responseCode = "422", description = "Invalid nightlife supplied", content = @Content)
            })
    @PutMapping(value = "/{id}")
    public ResponseEntity<NightlifeDTO> updateNightlife(
            @PathVariable Long id, @RequestBody NightlifeDTO newNightlifeDTO) {
        City city = cityService.getCity(newNightlifeDTO.cityId());
        Nightlife updatedNightlife = service.update(id, NightlifeMapper.DTOtoEntity(newNightlifeDTO, city));
        return ResponseEntity.ok(NightlifeMapper.entityToDTO(updatedNightlife));
    }
}
