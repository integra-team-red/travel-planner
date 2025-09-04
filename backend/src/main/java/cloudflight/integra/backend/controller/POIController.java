package cloudflight.integra.backend.controller;

import cloudflight.integra.backend.model.PointOfInterest;
import cloudflight.integra.backend.service.POIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController()
@RequestMapping("/point-of-interest")
public class POIController {
    private final POIService service;

    @Autowired
    public POIController(final POIService poiService) {service = poiService;}

    @GetMapping
    public ResponseEntity<List<PointOfInterest>> getPointsOfInterest() {
        return ResponseEntity.ok(service.getAllPointsOfInterest());
    }

    @PostMapping
    public ResponseEntity<PointOfInterest> addPointOfInterest(@RequestBody PointOfInterest pointOfInterest) {
        /// TODO: Remove this if statement after implementing DTOs
        if(pointOfInterest.getId() != null && pointOfInterest.getId() != 0) { return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build(); }
        /// TODO: cityId validation
        return ResponseEntity.ok(service.addPointOfInterest(pointOfInterest));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<PointOfInterest> deletePointOfInterest(@PathVariable Long id) {
        PointOfInterest deletedPOI = service.deletePointOfInterest(id);
        if (deletedPOI == null) { return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); }
        return ResponseEntity.ok(deletedPOI);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<PointOfInterest> updatePointOfInterest(@PathVariable Long id, @RequestBody PointOfInterest newPointOfInterest) {
        /// TODO: Remove this if statement after implementing DTOs
        if(newPointOfInterest.getId() != null && newPointOfInterest.getId() != 0) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }

        PointOfInterest updatedPOI = service.updatePointOfInterest(id, newPointOfInterest);
        if (updatedPOI == null) { return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); }
        return ResponseEntity.ok(updatedPOI);
    }

}
