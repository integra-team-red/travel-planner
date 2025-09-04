package cloudflight.integra.backend.service;

import cloudflight.integra.backend.model.PointOfInterest;

import java.util.List;

public interface POIService {
    PointOfInterest addPointOfInterest(PointOfInterest pointOfInterest);
    PointOfInterest updatePointOfInterest(Long id, PointOfInterest pointOfInterest);
    PointOfInterest deletePointOfInterest(Long id);
    List<PointOfInterest> getAllPointsOfInterest();
}
