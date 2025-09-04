package cloudflight.integra.backend.repository;

import cloudflight.integra.backend.model.PointOfInterest;

import java.util.List;

public interface POIRepository {
    PointOfInterest addPointOfInterest(PointOfInterest pointOfInterest);
    PointOfInterest findPointOfInterestById(Long id);
    List<PointOfInterest> getAllPointsOfInterest();
    PointOfInterest updatePointOfInterest(Long id, PointOfInterest pointOfInterest);
    PointOfInterest deletePointOfInterestById(Long id);
}
