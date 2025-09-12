package cloudflight.integra.backend.service;

import cloudflight.integra.backend.model.PointOfInterest;
import cloudflight.integra.backend.repository.DBCityRepository;
import cloudflight.integra.backend.repository.DBPOIRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class POIServiceImpl implements POIService {

    private DBPOIRepository repo;
    private DBCityRepository cityRepo;

    @Autowired
    public POIServiceImpl(DBPOIRepository repo, DBCityRepository cityRepo) {
        this.repo = repo;
        this.cityRepo = cityRepo;
    }

    @Override
    public PointOfInterest addPointOfInterest(PointOfInterest pointOfInterest) {
        return repo.save(pointOfInterest);
    }

    @Override
    public PointOfInterest updatePointOfInterest(Long id, PointOfInterest pointOfInterest) {
        PointOfInterest DBPOI = repo.findById(id)
                .get();

        DBPOI.setName(pointOfInterest.getName());
        DBPOI.setDescription(pointOfInterest.getDescription());
        DBPOI.setCity(pointOfInterest.getCity());
        DBPOI.setPrice(pointOfInterest.getPrice());

        return DBPOI;
    }

    @Override
    public void deletePointOfInterest(Long id) {
        repo.deleteById(id);
    }

    @Override
    public List<PointOfInterest> getAllPointsOfInterest() { return repo.findAll(); }
}
