package cloudflight.integra.backend.controller;

import cloudflight.integra.backend.DTO.POIDTO;
import cloudflight.integra.backend.city.City;
import cloudflight.integra.backend.mapper.POIMapper;
import cloudflight.integra.backend.model.PointOfInterest;
import cloudflight.integra.backend.service.CityService;
import cloudflight.integra.backend.service.POIService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

        if (poiDTO.id() != null && poiDTO.id() != 0) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .build();
        }
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

        if (newPointOfInterestDTO.id() != null && newPointOfInterestDTO.id() != 0) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .build();
        }
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
}
