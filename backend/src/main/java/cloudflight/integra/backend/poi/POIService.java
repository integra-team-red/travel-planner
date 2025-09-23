package cloudflight.integra.backend.poi;

import java.util.List;

public interface POIService {
    PointOfInterest addPointOfInterest(PointOfInterest pointOfInterest);

    PointOfInterest updatePointOfInterest(Long id, PointOfInterest pointOfInterest);

    void deletePointOfInterest(Long id);

    List<PointOfInterest> getAllPointsOfInterest();

    List<PointOfInterest> getPointsOfInterestByCity(Long id, String name);

    List<PointOfInterest> getAllPointsOfInterestSortedByName(int pageNumber, int pageSize, boolean isDescending);

    List<PointOfInterest> getAllPointsOfInterestSortedByPrice(int pageNumber, int pageSize, boolean isDescending);

    List<PointOfInterest> getAllPointsOfInterestSortedByType(int pageNumber, int pageSize, String type);
}
