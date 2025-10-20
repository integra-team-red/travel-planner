package cloudflight.integra.backend.event;

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
public class EventServiceImpl implements EventService {
    private final EventRepository repository;
    private final GenericConstraintValidator<Event> validator;

    @Autowired
    public EventServiceImpl(final EventRepository repository, final GenericConstraintValidator<Event> validator) {
        this.repository = repository;
        this.validator = validator;
    }

    @Override
    public Event addEvent(Event event) {
        validator.validate(event);
        return repository.save(event);
    }

    @Override
    public List<Event> addEvents(List<Event> events) {
        try {
            events.forEach(validator::validate);
        } catch (ConstraintViolationException err) {
            throw new ConstraintViolationException(
                    "One or more POIs were invalid:\n" + err.getMessage(), err.getConstraintViolations());
        }
        return repository.saveAll(events);
    }

    @Override
    public Event updateEvent(Long id, Event event) {
        validator.validate(event);
        var dbEvent = getEvent(id);

        dbEvent.setName(event.getName());
        dbEvent.setDescription(event.getDescription());
        dbEvent.setStartTime(event.getStartTime());
        dbEvent.setEndTime(event.getEndTime());
        dbEvent.setPoi(event.getPoi());
        dbEvent.setPrice(event.getPrice());
        dbEvent.setAudience(event.getAudience());

        return repository.save(dbEvent);
    }

    @Override
    public void deleteEvent(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Event> getAllEvents() {
        return repository.findAll();
    }

    @Override
    public Event getEvent(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("No event id provided");
        }
        var eventWrapper = repository.findById(id);
        if (eventWrapper.isEmpty()) {
            throw new EntityNotFoundException("Event with the provided id not found");
        }
        return eventWrapper.get();
    }

    @Override
    public List<Event> getEventsByPoi(Long id, String name) {
        if (id != null) return repository.findByPoi_Id(id);
        else if (name != null && !name.isBlank()) return repository.findByPoi_Name(name.trim());
        else return List.of();
    }

    @Override
    public List<Event> getAllEventsSortedByName(int pageNumber, int pageSize, boolean isDescending) {
        PageRequestValidator.validate(pageNumber, pageSize);
        var sortingDirection = isDescending ? Sort.Direction.DESC : Sort.Direction.ASC;
        return repository
                .findAll(PageRequest.of(pageNumber, pageSize, Sort.by(sortingDirection, "name")))
                .toList();
    }

    @Override
    public List<Event> getAllEventsSortedByPrice(int pageNumber, int pageSize, boolean isDescending) {
        PageRequestValidator.validate(pageNumber, pageSize);
        var sortingDirection = isDescending ? Sort.Direction.DESC : Sort.Direction.ASC;
        return repository
                .findAll(PageRequest.of(pageNumber, pageSize, Sort.by(sortingDirection, "price")))
                .toList();
    }

    @Override
    public List<Event> getAllEventsSortedByAudience(int pageNumber, int pageSize, String audience) {
        PageRequestValidator.validate(pageNumber, pageSize);
        return repository
                .findAllByAudience(audience, PageRequest.of(pageNumber, pageSize))
                .toList();
    }
}
