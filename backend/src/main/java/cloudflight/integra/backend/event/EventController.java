package cloudflight.integra.backend.event;

import cloudflight.integra.backend.poi.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/event")
@SecurityRequirement(name = "bearerAuth")
public class EventController {
    private final EventService service;
    private final POIService poiService;

    @Autowired
    public EventController(final EventService service, final POIService poiService) {
        this.service = service;
        this.poiService = poiService;
    }

    @Operation(
            summary = "Get a list of all events from the repository",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "All events returned successfully",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        array = @ArraySchema(schema = @Schema(implementation = EventDTO.class)))),
            })
    @GetMapping
    public ResponseEntity<List<EventDTO>> getEvents() {
        List<EventDTO> events =
                service.getAllEvents().stream().map(EventMapper::EntityToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(events);
    }

    @Operation(
            summary = "Add an event to the repository",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Event added successfully; returns the added event",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = EventDTO.class))),
                @ApiResponse(responseCode = "422", description = "Invalid event supplied", content = @Content),
                @ApiResponse(responseCode = "404", description = "POI id of the event not found", content = @Content)
            })
    @PostMapping
    public ResponseEntity<EventDTO> addEvent(@RequestBody EventDTO eventDTO) {
        POI poi = poiService.getPointOfInterest(eventDTO.poiId());
        Event savedEvent = service.addEvent(EventMapper.DTOToEntity(eventDTO, poi));
        return ResponseEntity.ok(EventMapper.EntityToDTO(savedEvent));
    }

    @Operation(
            summary = "Delete an event from the repository",
            responses = {
                @ApiResponse(responseCode = "200", description = "Event deleted successfully", content = @Content)
            })
    @DeleteMapping(value = "/{id}")
    ResponseEntity<HttpStatus> deleteEvent(@PathVariable Long id) {
        service.deleteEvent(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(
            summary = "Update an event's data with the provided event",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Event updated successfully; returns the updated event",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = EventDTO.class))),
                @ApiResponse(responseCode = "404", description = "Event to update not found", content = @Content),
                @ApiResponse(responseCode = "404", description = "POI id of the event not found", content = @Content),
                @ApiResponse(responseCode = "422", description = "Invalid event supplied", content = @Content)
            })
    @PutMapping(value = "/{id}")
    public ResponseEntity<EventDTO> updateEvent(@PathVariable Long id, @RequestBody EventDTO newEventDTO) {
        POI poi = poiService.getPointOfInterest(newEventDTO.poiId());
        Event updatedEvent = service.updateEvent(id, EventMapper.DTOToEntity(newEventDTO, poi));
        return ResponseEntity.ok(EventMapper.EntityToDTO(updatedEvent));
    }

    @Operation(
            summary = "Get a list of all events related to a provided POI",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "events returned successfully",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        array = @ArraySchema(schema = @Schema(implementation = EventDTO.class)))),
            })
    @GetMapping("/bypoi")
    public List<Event> getEventsByPoi(@RequestParam(required = false) Long poiId) {
        return service.getEventsByPoi(poiId);
    }

    @Operation(
            summary = "Get a list of events from a given page from the repository sorted in given direction by name",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Page returned successfully",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        array = @ArraySchema(schema = @Schema(implementation = EventDTO.class)))),
                @ApiResponse(responseCode = "406", description = "Invalid page requested", content = @Content)
            })
    @GetMapping(value = "/sortedByName")
    public ResponseEntity<List<EventDTO>> getAllEventsSortedByName(
            @RequestParam int pageSize, @RequestParam int pageNumber, @RequestParam Optional<Boolean> isDescending) {
        return ResponseEntity.ok(EventMapper.EntityListToDTOList(
                isDescending.isPresent()
                        ? service.getAllEventsSortedByName(pageNumber, pageSize, isDescending.get())
                        : service.getAllEventsSortedByName(pageNumber, pageSize, false)));
    }

    @Operation(
            summary = "Get a list of events from a given page from the repository sorted in given direction by price",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Page returned successfully",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        array = @ArraySchema(schema = @Schema(implementation = EventDTO.class)))),
                @ApiResponse(responseCode = "406", description = "Invalid page requested", content = @Content)
            })
    @GetMapping(value = "/sortedByPrice")
    public ResponseEntity<List<EventDTO>> getAllEventsSortedByPrice(
            @RequestParam int pageSize, @RequestParam int pageNumber, @RequestParam Optional<Boolean> isDescending) {
        return ResponseEntity.ok(EventMapper.EntityListToDTOList(
                isDescending.isPresent()
                        ? service.getAllEventsSortedByPrice(pageNumber, pageSize, isDescending.get())
                        : service.getAllEventsSortedByPrice(pageNumber, pageSize, false)));
    }

    @Operation(
            summary = "Get a list of events from a given page from the repository having the given type",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Page returned successfully",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        array = @ArraySchema(schema = @Schema(implementation = EventDTO.class)))),
                @ApiResponse(responseCode = "406", description = "Invalid page requested", content = @Content)
            })
    @GetMapping(value = "/sortedByAudience")
    public ResponseEntity<List<EventDTO>> getAllEventsSortedByAudience(
            @RequestParam int pageSize, @RequestParam int pageNumber, @RequestParam String audience) {
        return ResponseEntity.ok(
                EventMapper.EntityListToDTOList(service.getAllEventsSortedByAudience(pageNumber, pageSize, audience)));
    }

    @Operation(
            summary = "Get a list of event audiences",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Page returned successfully",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        array = @ArraySchema(schema = @Schema(implementation = EventAudience.class)))),
            })
    @GetMapping(value = "/audiences")
    public ResponseEntity<List<EventAudience>> getAllEventAudiences() {
        return ResponseEntity.ok(Arrays.stream(EventAudience.values()).toList());
    }
}
