package cloudflight.integra.backend.spa;

import cloudflight.integra.backend.validation.GenericConstraintValidator;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpaServiceImpl implements SpaService {
    private final DBSpaRepository repository;
    private final GenericConstraintValidator<Spa> validator;
    private final SpaValidator spaValidator;

    @Autowired
    public SpaServiceImpl(
            DBSpaRepository repository, GenericConstraintValidator<Spa> validator, SpaValidator spaValidator) {
        this.validator = validator;
        this.spaValidator = spaValidator;
        this.repository = repository;
    }

    @Override
    public Spa addSpa(Spa spa) {
        validator.validate(spa);
        spaValidator.validate(spa);
        return repository.save(spa);
    }

    @Override
    public Spa updateSpa(Long id, Spa newSpa) {
        validator.validate(newSpa);
        spaValidator.validate(newSpa);
        var dbSpa = getSpa(id);

        dbSpa.setName(newSpa.getName());
        dbSpa.setCity(newSpa.getCity());
        dbSpa.setPriceLowerBound(newSpa.getPriceLowerBound());
        dbSpa.setPriceUpperBound(newSpa.getPriceUpperBound());
        dbSpa.setSchedule(newSpa.getSchedule());
        dbSpa.setRating(newSpa.getRating());
        dbSpa.setDescription(newSpa.getDescription());

        return repository.save(dbSpa);
    }

    @Override
    public void deleteSpa(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Spa getSpa(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("No spa id provided");
        }
        var spaWrapper = repository.findById(id);
        if (spaWrapper.isEmpty()) {
            throw new EntityNotFoundException("Spa with the provided id not found");
        }
        return spaWrapper.get();
    }

    @Override
    public List<Spa> getAllSpas() {
        return repository.findAll();
    }

    @Override
    public List<Spa> getSpasByCity(Long id) {
        if (id != null) return repository.findByCity_Id(id);
        else return List.of();
    }
}
