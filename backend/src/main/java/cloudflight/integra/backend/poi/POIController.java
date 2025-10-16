package cloudflight.integra.backend.poi;

import cloudflight.integra.backend.city.City;
import cloudflight.integra.backend.city.CityDTO;
import cloudflight.integra.backend.city.CityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.EntityNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/point-of-interest")
@SecurityRequirement(name = "bearerAuth")
public class POIController {
    private final POIService service;
    private final CityService cityService;

    @Autowired
    public POIController(POIService poiService, CityService cityService) {
        this.service = poiService;
        this.cityService = cityService;
    }

    @Operation(
            summary = "Get a list of all POIs from the repository",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "All POIs returned successfully",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        array = @ArraySchema(schema = @Schema(implementation = POIDTO.class)))),
            })
    @GetMapping
    public ResponseEntity<List<POIDTO>> getPointsOfInterest() {
        List<POIDTO> pois = service.getAllPointsOfInterest().stream()
                .map(POIMapper::POIToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(pois);
    }

    @Operation(
            summary = "Add a POI to the repository",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "POI added successfully; returns the added POI",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = POIDTO.class))),
                @ApiResponse(responseCode = "422", description = "Invalid POI supplied", content = @Content),
                @ApiResponse(responseCode = "404", description = "City id of the POI not found", content = @Content)
            })
    @PostMapping
    public ResponseEntity<POIDTO> addPointOfInterest(@RequestBody POIDTO poiDTO) {
        City city = cityService.getCity(poiDTO.cityId());
        POI savedPoi = service.addPointOfInterest(POIMapper.POIToEntity(poiDTO, city));
        return ResponseEntity.ok(POIMapper.POIToDTO(savedPoi));
    }

    @Operation(
            summary = "Delete a POI from the repository",
            responses = {
                @ApiResponse(responseCode = "200", description = "POI deleted successfully", content = @Content)
            })
    @DeleteMapping(value = "/{id}")
    ResponseEntity<HttpStatus> deletePointOfInterest(@PathVariable Long id) {
        service.deletePointOfInterest(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(
            summary = "Update a POI's data with the provided POI",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "POI updated successfully; returns the updated POI",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = POIDTO.class))),
                @ApiResponse(responseCode = "404", description = "POI to update not found", content = @Content),
                @ApiResponse(responseCode = "404", description = "City id of the POI not found", content = @Content),
                @ApiResponse(responseCode = "422", description = "Invalid POI supplied", content = @Content)
            })
    @PutMapping(value = "/{id}")
    public ResponseEntity<POIDTO> updatePointOfInterest(
            @PathVariable Long id, @RequestBody POIDTO newPointOfInterestDTO) {
        City city = cityService.getCity(newPointOfInterestDTO.cityId());
        POI updatedPOI = service.updatePointOfInterest(id, POIMapper.POIToEntity(newPointOfInterestDTO, city));
        return ResponseEntity.ok(POIMapper.POIToDTO(updatedPOI));
    }

    @Operation(
            summary = "Get a list of all POIs related to a provided city",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "POIs returned successfully",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        array = @ArraySchema(schema = @Schema(implementation = POIDTO.class)))),
            })
    @GetMapping("/bycity")
    public List<POI> getPointsOfInterestByCity(
            @RequestParam(required = false) Long cityId, @RequestParam(required = false) String cityName) {
        return service.getPointsOfInterestByCity(cityId, cityName);
    }

    @Operation(
            summary = "Download a JSON file of all POIs from the repository to the user's computer",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "POIs returned for download successfully",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        array = @ArraySchema(schema = @Schema(implementation = POIDTO.class))),
                        headers = {
                            @Header(
                                    name = "Content-Disposition",
                                    schema = @Schema(example = "attachment; filename=\"ApprovedPOIs.json\""))
                        }),
            })
    @GetMapping(value = "/download")
    public ResponseEntity<String> exportApprovedPOIsToJson() throws IOException {
        final var jsonMapper = new ObjectMapper();
        final var outputStream = new StringWriter();

        // TODO(MC): Maybe find a **clean** way to remove the "id" field here or get mixins to work
        jsonMapper.writeValue(outputStream, POIMapper.EntityListToDTOList(service.getAllPointsOfInterest()));
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Disposition", "attachment; filename=\"ApprovedPOIs.json\"")
                .contentLength(outputStream.getBuffer().length())
                .contentType(MediaType.APPLICATION_JSON)
                .body(outputStream.toString());
    }

    @Operation(
            summary = "Upload an array of POIs formatted in JSON to add to the repository",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "POIs added successfully",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        array = @ArraySchema(schema = @Schema(implementation = CityDTO.class)))),
                @ApiResponse(responseCode = "422", description = "One or more POIs were invalid", content = @Content),
                @ApiResponse(
                        responseCode = "404",
                        description = "One or more POIs' city IDs were not found",
                        content = @Content)
            })
    @PostMapping(value = "/upload")
    public ResponseEntity<List<POIDTO>> importApprovedPOIsFromJson(@RequestBody List<POIDTO> pois) {
        List<POI> poiEntities;
        try {
            poiEntities = pois.stream()
                    .map(poidto -> POIMapper.POIToEntity(poidto, cityService.getCity(poidto.cityId())))
                    .toList();
        } catch (EntityNotFoundException err) {
            throw new EntityNotFoundException("One or more POIs' city IDs were not found");
        }
        service.addPointsOfInterest(poiEntities);
        return ResponseEntity.ok(POIMapper.EntityListToDTOList(service.getAllPointsOfInterest()));
    }

    @Operation(
            summary = "Get a list of POIs from a given page from the repository sorted in given direction by name",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Page returned successfully",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        array = @ArraySchema(schema = @Schema(implementation = POIDTO.class)))),
                @ApiResponse(responseCode = "406", description = "Invalid page requested", content = @Content)
            })
    @GetMapping(value = "/sortedByName")
    public ResponseEntity<List<POIDTO>> getAllPointsOfInterestSortedByName(
            @RequestParam int pageSize, @RequestParam int pageNumber, @RequestParam Optional<Boolean> isDescending) {
        return ResponseEntity.ok(POIMapper.EntityListToDTOList(
                isDescending.isPresent()
                        ? service.getAllPointsOfInterestSortedByName(pageNumber, pageSize, isDescending.get())
                        : service.getAllPointsOfInterestSortedByName(pageNumber, pageSize, false)));
    }

    @Operation(
            summary = "Get a list of POIs from a given page from the repository sorted in given direction by price",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Page returned successfully",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        array = @ArraySchema(schema = @Schema(implementation = POIDTO.class)))),
                @ApiResponse(responseCode = "406", description = "Invalid page requested", content = @Content)
            })
    @GetMapping(value = "/sortedByPrice")
    public ResponseEntity<List<POIDTO>> getAllPointsOfInterestSortedByPrice(
            @RequestParam int pageSize, @RequestParam int pageNumber, @RequestParam Optional<Boolean> isDescending) {
        return ResponseEntity.ok(POIMapper.EntityListToDTOList(
                isDescending.isPresent()
                        ? service.getAllPointsOfInterestSortedByPrice(pageNumber, pageSize, isDescending.get())
                        : service.getAllPointsOfInterestSortedByPrice(pageNumber, pageSize, false)));
    }

    @Operation(
            summary = "Get a list of POIs from a given page from the repository having the given type",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Page returned successfully",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        array = @ArraySchema(schema = @Schema(implementation = POIDTO.class)))),
                @ApiResponse(responseCode = "406", description = "Invalid page requested", content = @Content)
            })
    @GetMapping(value = "/sortedByType")
    public ResponseEntity<List<POIDTO>> getAllPointsOfInterestSortedByType(
            @RequestParam int pageSize, @RequestParam int pageNumber, @RequestParam String type) {
        return ResponseEntity.ok(
                POIMapper.EntityListToDTOList(service.getAllPointsOfInterestSortedByType(pageNumber, pageSize, type)));
    }

    @Operation(
            summary = "Get a list of POI types",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Page returned successfully",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        array =
                                                @ArraySchema(
                                                        schema = @Schema(implementation = PointOfInterestType.class)))),
            })
    @GetMapping(value = "/types")
    public ResponseEntity<List<PointOfInterestType>> getAllPointsOfInterestTypes() {
        return ResponseEntity.ok(Arrays.stream(PointOfInterestType.values()).toList());
    }
}
