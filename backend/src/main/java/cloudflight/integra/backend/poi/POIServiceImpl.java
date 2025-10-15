package cloudflight.integra.backend.poi;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class POIServiceImpl implements POIService {

    private final DBPOIRepository repo;

    @Autowired
    public POIServiceImpl(DBPOIRepository repo) {
        this.repo = repo;
    }

    @Override
    public PointOfInterest addPointOfInterest(PointOfInterest pointOfInterest) {
        return repo.save(pointOfInterest);
    }

    @Override
    public PointOfInterest updatePointOfInterest(Long id, PointOfInterest pointOfInterest) {
        PointOfInterest DBPOI = repo.findById(id).get();

        DBPOI.setName(pointOfInterest.getName());
        DBPOI.setDescription(pointOfInterest.getDescription());
        DBPOI.setAddress(pointOfInterest.getAddress());
        DBPOI.setCity(pointOfInterest.getCity());
        DBPOI.setPrice(pointOfInterest.getPrice());
        DBPOI.setType(pointOfInterest.getType());
        DBPOI.setImage(pointOfInterest.getImage());

        repo.save(DBPOI);

        return DBPOI;
    }

    @Override
    public void deletePointOfInterest(Long id) {
        repo.deleteById(id);
    }

    @Override
    public List<PointOfInterest> getAllPointsOfInterest() {
        return repo.findAll();
    }

    @Override
    public List<PointOfInterest> getPointsOfInterestByCity(Long id, String name) {
        if (id != null) return repo.findByCity_Id(id);
        else if (name != null && !name.isBlank()) return repo.findByCity_Name(name.trim());
        else return List.of();
    }

    @Override
    public List<PointOfInterest> getAllPointsOfInterestSortedByName(
            int pageNumber, int pageSize, boolean isDescending) {
        var sortingDirection = isDescending ? Sort.Direction.DESC : Sort.Direction.ASC;
        return repo.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(sortingDirection, "name")))
                .toList();
    }

    @Override
    public List<PointOfInterest> getAllPointsOfInterestSortedByPrice(
            int pageNumber, int pageSize, boolean isDescending) {
        var sortingDirection = isDescending ? Sort.Direction.DESC : Sort.Direction.ASC;
        return repo.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(sortingDirection, "price")))
                .toList();
    }

    @Override
    public List<PointOfInterest> getAllPointsOfInterestSortedByType(int pageNumber, int pageSize, String type) {
        return repo.findAllByType(type, PageRequest.of(pageNumber, pageSize)).toList();
    }
}
