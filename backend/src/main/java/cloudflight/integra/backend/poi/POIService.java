package cloudflight.integra.backend.poi;

import java.util.List;

public interface POIService {
    POI addPointOfInterest(POI pointOfInterest);

    List<POI> addPointsOfInterest(List<POI> pois);

    POI updatePointOfInterest(Long id, POI pointOfInterest);

    void deletePointOfInterest(Long id);

    List<POI> getAllPointsOfInterest();

    POI getPointOfInterest(Long id);

    List<POI> getPointsOfInterestByCity(Long id, String name);

    List<POI> getAllPointsOfInterestSortedByName(int pageNumber, int pageSize, boolean isDescending);

    List<POI> getAllPointsOfInterestSortedByPrice(int pageNumber, int pageSize, boolean isDescending);

    List<POI> getAllPointsOfInterestSortedByType(int pageNumber, int pageSize, String type);
}
