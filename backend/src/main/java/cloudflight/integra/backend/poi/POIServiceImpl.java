package cloudflight.integra.backend.poi;

import cloudflight.integra.backend.validation.GenericConstraintValidator;
import cloudflight.integra.backend.validation.PageRequestValidator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class POIServiceImpl implements POIService {
    private final DBPOIRepository repository;
    private final GenericConstraintValidator<POI> validator;

    @Autowired
    public POIServiceImpl(DBPOIRepository repository, GenericConstraintValidator<POI> validator) {
        this.validator = validator;
        this.repository = repository;
    }

    @Override
    public POI addPointOfInterest(POI poi) {
        validator.validate(poi);
        return repository.save(poi);
    }

    @Override
    public List<POI> addPointsOfInterest(List<POI> pois) {
        try {
            pois.forEach(validator::validate);
        } catch (ConstraintViolationException err) {
            throw new ConstraintViolationException(
                    "One or more POIs were invalid:\n" + err.getMessage(), err.getConstraintViolations());
        }
        return repository.saveAll(pois);
    }

    @Override
    public POI updatePointOfInterest(Long id, POI newPOI) {
        validator.validate(newPOI);
        var dbPOI = getPointOfInterest(id);

        dbPOI.setName(newPOI.getName());
        dbPOI.setDescription(newPOI.getDescription());
        dbPOI.setAddress(newPOI.getAddress());
        dbPOI.setCity(newPOI.getCity());
        dbPOI.setPrice(newPOI.getPrice());
        dbPOI.setType(newPOI.getType());
        dbPOI.setImage(newPOI.getImage());

        return repository.save(dbPOI);
    }

    @Override
    public void deletePointOfInterest(Long id) {
        repository.deleteById(id);
    }

    @Override
    public POI getPointOfInterest(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("No point of interest id provided");
        }
        var poiWrapper = repository.findById(id);
        if (poiWrapper.isEmpty()) {
            throw new EntityNotFoundException("Point of interest with the provided id not found");
        }
        return poiWrapper.get();
    }

    @Override
    public List<POI> getAllPointsOfInterest() {
        return repository.findAll();
    }

    @Override
    public List<POI> getPointsOfInterestByCity(Long id, String name) {
        if (id != null) return repository.findByCity_Id(id);
        else if (name != null && !name.isBlank()) return repository.findByCity_Name(name.trim());
        else return List.of();
    }

    @Override
    public List<POI> getAllPointsOfInterestSortedByName(int pageNumber, int pageSize, boolean isDescending) {
        PageRequestValidator.validate(pageNumber, pageSize);
        var sortingDirection = isDescending ? Sort.Direction.DESC : Sort.Direction.ASC;
        return repository
                .findAll(PageRequest.of(pageNumber, pageSize, Sort.by(sortingDirection, "name")))
                .toList();
    }

    @Override
    public List<POI> getAllPointsOfInterestSortedByPrice(int pageNumber, int pageSize, boolean isDescending) {
        PageRequestValidator.validate(pageNumber, pageSize);
        var sortingDirection = isDescending ? Sort.Direction.DESC : Sort.Direction.ASC;
        return repository
                .findAll(PageRequest.of(pageNumber, pageSize, Sort.by(sortingDirection, "price")))
                .toList();
    }

    @Override
    public List<POI> getAllPointsOfInterestSortedByType(int pageNumber, int pageSize, String type) {
        PageRequestValidator.validate(pageNumber, pageSize);
        return repository
                .findAllByType(type, PageRequest.of(pageNumber, pageSize))
                .toList();
    }
}
