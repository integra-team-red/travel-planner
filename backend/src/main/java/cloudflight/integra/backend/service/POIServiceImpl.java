package cloudflight.integra.backend.service;

import cloudflight.integra.backend.model.PointOfInterest;
import cloudflight.integra.backend.repository.POIRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class POIServiceImpl implements POIService {
    private POIRepository repo;

    @Autowired
    public POIServiceImpl(POIRepository repo) {
        this.repo = repo;
    }

    @Override
    public PointOfInterest addPointOfInterest(PointOfInterest pointOfInterest) {
        return repo.addPointOfInterest(pointOfInterest);
    }

    @Override
    public PointOfInterest updatePointOfInterest(Long id, PointOfInterest pointOfInterest) {
        if(repo.findPointOfInterestById(id) == null) {
            return null;
        }
        return repo.updatePointOfInterest(id, pointOfInterest);
    }

    @Override
    public PointOfInterest deletePointOfInterest(Long id) { return repo.deletePointOfInterestById(id); }

    @Override
    public List<PointOfInterest> getAllPointsOfInterest() { return repo.getAllPointsOfInterest(); }
}
