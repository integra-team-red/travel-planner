package cloudflight.integra.backend.trip;

import cloudflight.integra.backend.city.City;
import cloudflight.integra.backend.city.CityService;
import cloudflight.integra.backend.user.User;
import cloudflight.integra.backend.user.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trips")
@SecurityRequirement(name = "bearerAuth")
public class TripController {

    private final TripService service;
    private final UserRepository userRepository;
    private final CityService cityService;

    @Autowired
    public TripController(TripService service, UserRepository userRepository, CityService cityService) {
        this.service = service;
        this.userRepository = userRepository;
        this.cityService = cityService;
    }

    @Operation(
            summary = "get a list of all trips",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "all trips returned successfully",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        array = @ArraySchema(schema = @Schema(implementation = TripDTO.class))))
            })
    @GetMapping
    public ResponseEntity<List<TripDTO>> getTrips() {
        List<TripDTO> trips = TripMapper.entityListToDTOList(service.getAllTrips());
        return ResponseEntity.ok(trips);
    }

    @Operation(
            summary = "add a trip",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "trip added successfully",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = TripDTO.class))),
                @ApiResponse(responseCode = "404", description = "user id not found", content = @Content)
            })
    @PostMapping
    public ResponseEntity<TripDTO> addTrip(@RequestBody TripDTO tripDTO) {
        User user = userRepository
                .findById(tripDTO.userId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + tripDTO.userId()));

        City city = cityService.getCity(tripDTO.cityId());
        Trip savedTrip = service.addTrip(TripMapper.DTOtoEntity(tripDTO, user, city));
        return ResponseEntity.ok(TripMapper.entityToDTO(savedTrip));
    }

    @Operation(
            summary = "delete a trip",
            responses = {
                @ApiResponse(responseCode = "200", description = "trip deleted successfully", content = @Content),
                @ApiResponse(responseCode = "404", description = "trip not found", content = @Content)
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrip(@PathVariable Long id) {
        service.deleteTrip(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(
            summary = "update a trip",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "trip updated successfully",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = TripDTO.class))),
                @ApiResponse(responseCode = "404", description = "Trip or user not found", content = @Content)
            })
    @PutMapping("/{id}")
    public ResponseEntity<TripDTO> updateTrip(@PathVariable Long id, @RequestBody TripDTO newTripDTO) {
        User user = userRepository
                .findById(newTripDTO.userId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + newTripDTO.userId()));

        City city = cityService.getCity(newTripDTO.cityId());
        Trip updatedTrip = service.updateTrip(id, TripMapper.DTOtoEntity(newTripDTO, user, city));
        return ResponseEntity.ok(TripMapper.entityToDTO(updatedTrip));
    }

    @Operation(
            summary = "Get a list of trips belonging to a specific user",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "trips for the user returned successfully",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        array = @ArraySchema(schema = @Schema(implementation = TripDTO.class)))),
                @ApiResponse(responseCode = "404", description = "user not found", content = @Content)
            })
    @GetMapping("/byUser/{userId}")
    public ResponseEntity<List<TripDTO>> getTripsByUser(@PathVariable Long userId) {
        List<TripDTO> trips = TripMapper.entityListToDTOList(service.getTripsByUser(userId));
        return ResponseEntity.ok(trips);
    }

    @Operation(
            summary = "get all pois, restaurants, coffee shops, and spas for a specific city by ID",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "city attractions returned successfully",
                        content = @Content(mediaType = "application/json")),
                @ApiResponse(responseCode = "404", description = "no attractions found", content = @Content)
            })
    @GetMapping("/byCity/{cityId}")
    public ResponseEntity<Map<String, List<?>>> getAttractionsByCity(@PathVariable Long cityId) {
        Map<String, List<?>> result = service.getAttractionsCityId(cityId);

        boolean isEmpty = result.values().stream().allMatch(List::isEmpty);
        if (isEmpty) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }

        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = "generate an itinerary",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "itinerary generated successfully",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = ItineraryDTO.class))),
                @ApiResponse(responseCode = "404", description = "user id or city not found", content = @Content),
                @ApiResponse(
                        responseCode = "406",
                        description = "minimum cost exceeds budget",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = ItineraryDTO.class)))
            })
    @PostMapping("/generate")
    public ResponseEntity<ItineraryDTO> generateItinerary(@RequestBody TripDTO tripDTO) {
        User user = userRepository
                .findById(tripDTO.userId())
                .orElseThrow(() -> new RuntimeException("user not found with id: " + tripDTO.userId()));
        City city = cityService.getCity(tripDTO.cityId());
        Trip trip = TripMapper.DTOtoEntity(tripDTO, user, city);
        Itinerary itinerary = service.generateItinerary(trip);
        ItineraryDTO itineraryDTO = ItineraryMapper.ItineraryToDTO(itinerary);

        return ResponseEntity.ok(itineraryDTO);
    }
}
