package cloudflight.integra.backend.repository;

import cloudflight.integra.backend.model.City;
import cloudflight.integra.backend.model.PointOfInterest;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class InMemoryPOIRepository implements POIRepository {
    private HashMap<Long, PointOfInterest> pointsOfInterest;
    private Long lastId = 1L;

    private Long generateId() {
        return lastId++;
    }

    public InMemoryPOIRepository() {
        pointsOfInterest = new HashMap<>();
    }

    @Override
    public PointOfInterest addPointOfInterest(PointOfInterest pointOfInterest) {
        pointOfInterest.setId(generateId());
        return pointsOfInterest.put(pointOfInterest.getId(), pointOfInterest);
    }

    @Override
    public List<PointOfInterest> getAllPointsOfInterest() {
        return pointsOfInterest.values().stream().toList();
    }

    public PointOfInterest findPointOfInterestById(Long id) {
        return pointsOfInterest.get(id);
    }

    public PointOfInterest updatePointOfInterest(Long id, PointOfInterest pointOfInterest) {
        pointOfInterest.setId(id);
        return pointsOfInterest.put(id, pointOfInterest);
    }

    public PointOfInterest deletePointOfInterestById(Long id) {
        return pointsOfInterest.remove(id);
    }

}
