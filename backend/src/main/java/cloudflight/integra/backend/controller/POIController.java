package cloudflight.integra.backend.controller;

import cloudflight.integra.backend.DTO.POIDTO;
import cloudflight.integra.backend.city.City;
import cloudflight.integra.backend.mapper.POIMapper;
import cloudflight.integra.backend.model.PointOfInterest;
import cloudflight.integra.backend.service.CityService;
import cloudflight.integra.backend.service.POIService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.stream.Collectors;
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

    @GetMapping
    public ResponseEntity<List<POIDTO>> getPointsOfInterest() {
        List<POIDTO> pois = service.getAllPointsOfInterest()
                .stream()
                .map(POIMapper::POIToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(pois);
    }

    @PostMapping
    public ResponseEntity<POIDTO> addPointOfInterest(@RequestBody POIDTO poiDTO) {
        // TODO: cityId validation
        City city = cityService.getCity(poiDTO.cityId());
        PointOfInterest savedPoi = service.addPointOfInterest(POIMapper.POIToEntity(poiDTO, city));
        return ResponseEntity.ok(POIMapper.POIToDTO(savedPoi));
    }

    @DeleteMapping(value = "/{id}")
    ResponseEntity<HttpStatus> deletePointOfInterest(@PathVariable Long id) {
        service.deletePointOfInterest(id);
        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }

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

    @GetMapping("/bycity")
    public List<PointOfInterest> getPointsOfInterestByCity(@RequestParam(required = false) Long cityId,
                                                           @RequestParam(required = false) String cityName) {
        return service.getPointsOfInterestByCity(cityId, cityName);
    }

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

    @PostMapping(value = "/upload")
    public ResponseEntity<List<POIDTO>> importApprovedPOIsFromJson(@RequestBody List<POIDTO> poiDtos) {
        poiDtos.forEach(poiDto -> service.addPointOfInterest(POIMapper.POIToEntity(poiDto,
                                                                                   cityService.getCity(poiDto
                                                                                           .cityId())))
        );
        return ResponseEntity.ok(POIMapper.EntityListToDTOList(service.getAllPointsOfInterest()));
    }
}
