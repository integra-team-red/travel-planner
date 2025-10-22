package cloudflight.integra.backend.event;

import java.util.List;

public interface EventService {

    Event addEvent(Event event);

    List<Event> addEvents(List<Event> events);

    Event updateEvent(Long id, Event event);

    void deleteEvent(Long id);

    List<Event> getAllEvents();

    Event getEvent(Long id);

    List<Event> getEventsByPoi(Long id);

    List<Event> getAllEventsSortedByName(int pageNumber, int pageSize, boolean isDescending);

    List<Event> getAllEventsSortedByPrice(int pageNumber, int pageSize, boolean isDescending);

    List<Event> getAllEventsSortedByAudience(int pageNumber, int pageSize, String audience);
}
