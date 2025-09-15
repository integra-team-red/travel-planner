package cloudflight.integra.backend.poi;

import cloudflight.integra.backend.city.DBCityRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class POIServiceImpl implements POIService {

    private final DBPOIRepository repo;
    private final DBCityRepository cityRepo;

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

    @Override
    public List<PointOfInterest> getPointsOfInterestByCity(Long id, String name) {
        if (id != null)
            return repo.findByCity_Id(id);
        else if (name != null && !name.isBlank())
            return repo.findByCity_Name(name.trim());
        else
            return List.of();
    }

}
