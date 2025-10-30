package cloudflight.integra.backend.nightlife;

import cloudflight.integra.backend.validation.GenericConstraintValidator;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NightlifeServiceImpl implements NightlifeService {
    private final NightlifeRepository repository;
    private final GenericConstraintValidator<Nightlife> validator;
    private final NightlifeValidator nightlifeValidator;

    @Override
    public Nightlife add(Nightlife nightlife) {
        validator.validate(nightlife);
        nightlifeValidator.validate(nightlife);
        return repository.save(nightlife);
    }

    @Override
    public Nightlife update(Long id, Nightlife newNightlife) {
        validator.validate(newNightlife);
        nightlifeValidator.validate(newNightlife);
        var dbNightlife = get(id);

        dbNightlife.setName(newNightlife.getName());
        dbNightlife.setCity(newNightlife.getCity());
        dbNightlife.setType(newNightlife.getType());
        dbNightlife.setPriceLowerBound(newNightlife.getPriceLowerBound());
        dbNightlife.setPriceUpperBound(newNightlife.getPriceUpperBound());
        dbNightlife.setSchedule(newNightlife.getSchedule());
        dbNightlife.setRating(newNightlife.getRating());
        dbNightlife.setDescription(newNightlife.getDescription());

        return repository.save(dbNightlife);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Nightlife get(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("No nightlife id provided");
        }
        return repository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nightlife with the provided id not found"));
    }

    @Override
    public List<Nightlife> getAll() {
        return repository.findAll();
    }

    @Override
    public List<Nightlife> getAllByCity(Long id) {
        if (id != null) return repository.findByCity_Id(id);
        else return List.of();
    }
}
