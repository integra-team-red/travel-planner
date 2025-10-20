package cloudflight.integra.backend.event;

import cloudflight.integra.backend.poi.POI;
import java.util.List;

public class EventMapper {
    public static EventDTO EntityToDTO(Event event) {
        return new EventDTO(
                event.getId(),
                event.getName(),
                event.getDescription(),
                event.getStartTime(),
                event.getEndTime(),
                event.getPoiId(),
                event.getPrice(),
                event.getAudience() != null ? event.getAudience().name() : null);
    }

    public static Event DTOToEntity(EventDTO dto, POI poi) {
        Event event = new Event();
        event.setId(null);
        event.setName(dto.name());
        event.setDescription(dto.description());
        event.setStartTime(dto.startTime());
        event.setEndTime(dto.endTime());
        event.setPoi(poi);
        event.setPrice(dto.price());
        event.setAudience(dto.audience() != null ? EventAudience.valueOf(dto.audience()) : null);
        return event;
    }

    public static List<EventDTO> EntityListToDTOList(List<Event> events) {
        return events.stream().map(EventMapper::EntityToDTO).toList();
    }
}
