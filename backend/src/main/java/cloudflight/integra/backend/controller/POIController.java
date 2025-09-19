package cloudflight.integra.backend.controller;

import cloudflight.integra.backend.DTO.POIDTO;
import cloudflight.integra.backend.city.City;
import cloudflight.integra.backend.city.CityDTO;
import cloudflight.integra.backend.mapper.POIMapper;
import cloudflight.integra.backend.model.PointOfInterest;
import cloudflight.integra.backend.service.CityService;
import cloudflight.integra.backend.service.POIService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.stream.Collectors;

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

@RestController()
@RequestMapping("/point-of-interest")
public class POIController {
    private final POIService service;
    private final CityService cityService;

    @Autowired
    public POIController(final POIService poiService, final CityService cityService) {
        this.service = poiService;
        this.cityService = cityService;
    }

    @Operation(summary = "Get a list of all POIs from the repository", responses = {@ApiResponse(responseCode = "200", description = "All POIs returned successfully", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = POIDTO.class)))),
    })
    @GetMapping
    public ResponseEntity<List<POIDTO>> getPointsOfInterest() {
        List<POIDTO> pois = service.getAllPointsOfInterest()
                .stream()
                .map(POIMapper::POIToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(pois);
    }

    @Operation(summary = "Add a POI to the repository", responses = {@ApiResponse(responseCode = "200", description = "POI added successfully; returns the added POI", content = @Content(mediaType = "application/json", schema = @Schema(implementation = POIDTO.class))), @ApiResponse(responseCode = "5xx", description = "Invalid POI supplied", content = @Content)
    })
    @PostMapping
    public ResponseEntity<POIDTO> addPointOfInterest(@RequestBody POIDTO poiDTO) {
        // TODO: cityId validation
        City city = cityService.getCity(poiDTO.cityId());
        PointOfInterest savedPoi = service.addPointOfInterest(POIMapper.POIToEntity(poiDTO, city));
        return ResponseEntity.ok(POIMapper.POIToDTO(savedPoi));
    }

    @Operation(summary = "Delete a POI from the repository", responses = {@ApiResponse(responseCode = "200", description = "POI deleted successfully", content = @Content)
    })
    @DeleteMapping(value = "/{id}")
    ResponseEntity<HttpStatus> deletePointOfInterest(@PathVariable Long id) {
        service.deletePointOfInterest(id);
        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }

    @Operation(summary = "Update a POI's data with the provided POI", responses = {@ApiResponse(responseCode = "200", description = "POI updated successfully; returns the updated POI", content = @Content(mediaType = "application/json", schema = @Schema(implementation = POIDTO.class))), @ApiResponse(responseCode = "404", description = "POI to update not found", content = @Content)
    })
    @PutMapping(value = "/{id}")
    public ResponseEntity<POIDTO> updatePointOfInterest(@PathVariable Long id,
                                                        @RequestBody POIDTO newPointOfInterestDTO) {
        City city = cityService.getCity(newPointOfInterestDTO.cityId());
        PointOfInterest updatedPOI = service.updatePointOfInterest(id,
                                                                   POIMapper
                                                                           .POIToEntity(newPointOfInterestDTO, city));
        if (updatedPOI == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .build();
        }
        return ResponseEntity.ok(POIMapper.POIToDTO(updatedPOI));
    }

    @Operation(summary = "Get a list of all POIs related to a provided city", responses = {@ApiResponse(responseCode = "200", description = "POIs returned successfully", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = POIDTO.class)))),
    })
    @GetMapping("/bycity")
    public List<PointOfInterest> getPointsOfInterestByCity(@RequestParam(required = false) Long cityId,
                                                           @RequestParam(required = false) String cityName) {
        return service.getPointsOfInterestByCity(cityId, cityName);
    }

    @Operation(summary = "Download a JSON file of all POIs from the repository to the user's computer", responses = {@ApiResponse(responseCode = "200", description = "POIs returned for download successfully", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = POIDTO.class))), headers = {@Header(name = "Content-Disposition", schema = @Schema(example = "attachment; filename=\"ApprovedPOIs.json\"")
    )
    }),
    })
    @GetMapping(value = "/download")
    public ResponseEntity<String> exportApprovedPOIsToJson() throws IOException {
        final var jsonMapper = new ObjectMapper();
        final var outputStream = new StringWriter();

        // TODO(MC): Maybe find a **clean** way to remove the "id" field here or get mixins to work
        jsonMapper.writeValue(outputStream, POIMapper.EntityListToDTOList(service.getAllPointsOfInterest()));
        return ResponseEntity
                .status(HttpStatus.OK)
                .header("Content-Disposition", "attachment; filename=\"ApprovedPOIs.json\"")
                .contentLength(outputStream.getBuffer()
                        .length())
                .contentType(MediaType.APPLICATION_JSON)
                .body(outputStream.toString());
    }

    @Operation(summary = "Upload an array of POIs formatted in JSON to add to the repository", responses = {@ApiResponse(responseCode = "200", description = "POIs added successfully", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CityDTO.class)))), @ApiResponse(responseCode = "422", description = "One or more POIs were invalid / already exist in the repository", content = @Content)
    })
    @PostMapping(value = "/upload")
    public ResponseEntity<List<POIDTO>> importApprovedPOIsFromJson(@RequestBody List<POIDTO> poiDtos) {
        try {
            poiDtos.forEach(poiDto -> service.addPointOfInterest(POIMapper.POIToEntity(poiDto,
                                                                                       cityService.getCity(poiDto
                                                                                               .cityId())
            ))
            );
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .build();
        }
        return ResponseEntity.ok(POIMapper.EntityListToDTOList(service.getAllPointsOfInterest()));
    }
}
