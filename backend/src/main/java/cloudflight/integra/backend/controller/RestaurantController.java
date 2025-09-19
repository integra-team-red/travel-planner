package cloudflight.integra.backend.controller;

import cloudflight.integra.backend.DTO.RestaurantDTO;
import cloudflight.integra.backend.city.City;
import cloudflight.integra.backend.mapper.RestaurantMapper;
import cloudflight.integra.backend.model.Restaurant;
import cloudflight.integra.backend.service.CityService;
import cloudflight.integra.backend.service.RestaurantService;
import java.util.List;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/restaurant")
public class RestaurantController {
    private final RestaurantService restaurantService;
    private final CityService cityService;

    @Autowired
    public RestaurantController(RestaurantService restaurantService, CityService cityService) {
        this.restaurantService = restaurantService;
        this.cityService = cityService;
    }

    @Operation(summary = "Get a list of all restaurants from the repository", responses = {@ApiResponse(responseCode = "200", description = "All restaurants returned successfully", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = RestaurantDTO.class)))),
    })
    @GetMapping
    public ResponseEntity<List<RestaurantDTO>> getRestaurants() {
        List<RestaurantDTO> restaurants = restaurantService.getAllRestaurants()
                .stream()
                .map(RestaurantMapper::RestaurantToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(restaurants);
    }

    @Operation(summary = "Add a restaurant to the repository", responses = {@ApiResponse(responseCode = "200", description = "Restaurant added successfully; returns the added restaurant", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestaurantDTO.class))), @ApiResponse(responseCode = "5xx", description = "Invalid restaurant supplied", content = @Content)
    })
    @PostMapping
    public ResponseEntity<RestaurantDTO> addRestaurant(@RequestBody RestaurantDTO restaurantDTO) {
        City city = cityService.getCity(restaurantDTO.cityId());
        Restaurant savedRestaurant = restaurantService.addRestaurant(RestaurantMapper
                .RestaurantToEntity(restaurantDTO, city));
        return ResponseEntity.ok(RestaurantMapper.RestaurantToDTO(savedRestaurant));
    }

    @Operation(summary = "Delete a restaurant from the repository", responses = {@ApiResponse(responseCode = "200", description = "Restaurant deleted successfully", content = @Content)
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<RestaurantDTO> deleteRestaurant(@PathVariable Long id) {
        restaurantService.deleteRestaurant(id);
        return ResponseEntity.status(HttpStatus.OK)
                .build();

    }

    @Operation(summary = "Update a restaurant's data with the provided restaurant", responses = {@ApiResponse(responseCode = "200", description = "Restaurant updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestaurantDTO.class))), @ApiResponse(responseCode = "404", description = "Restaurant to update not found", content = @Content)
    })
    @PutMapping(value = "/{id}")
    public ResponseEntity<RestaurantDTO> updateRestaurant(@PathVariable Long id,
                                                          @RequestBody RestaurantDTO newRestaurantDTO) {
        City city = cityService.getCity(newRestaurantDTO.cityId());
        Restaurant updatedRestaurant = restaurantService.updateRestaurant(id,
                                                                          RestaurantMapper
                                                                                  .RestaurantToEntity(newRestaurantDTO,
                                                                                                      city));
        if (updatedRestaurant == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .build();
        }
        return ResponseEntity.ok(RestaurantMapper.RestaurantToDTO(updatedRestaurant));
    }

    @Operation(summary = "Get a list of restaurants from a given page from the repository sorted in given direction by name", responses = {@ApiResponse(responseCode = "200", description = "Page returned successfully", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = RestaurantDTO.class)))
    ), @ApiResponse(responseCode = "403", description = "Invalid page requested", content = @Content)
    })
    @GetMapping(value = "/sortedByName")
    public ResponseEntity<List<RestaurantDTO>> getAllRestaurantsSortedByName(@RequestParam int pageSize,
                                                                             @RequestParam int pageNumber,
                                                                             @RequestParam Optional<Boolean> isDescending) {
        pageNumber -= 1;
        if (pageSize <= 0 || pageNumber <= 0) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .build();
        }
        return ResponseEntity.ok(
                                 RestaurantMapper.EntityListToDTOList(isDescending.isPresent() ? restaurantService
                                         .getAllRestaurantsSortedByName(pageNumber,
                                                                        pageSize,
                                                                        isDescending.get()) : restaurantService
                                                                                .getAllRestaurantsSortedByName(pageNumber,
                                                                                                               pageSize,
                                                                                                               false)
                                 )
        );
    }

    @Operation(summary = "Get a list of restaurants from a given page from the repository sorted in given direction by average price", responses = {@ApiResponse(responseCode = "200", description = "Page returned successfully", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = RestaurantDTO.class)))
    ), @ApiResponse(responseCode = "403", description = "Invalid page requested", content = @Content)
    })
    @GetMapping(value = "/sortedByPrice")
    public ResponseEntity<List<RestaurantDTO>> getAllRestaurantsSortedByAveragePrice(@RequestParam int pageSize,
                                                                                     @RequestParam int pageNumber,
                                                                                     @RequestParam Optional<Boolean> isDescending) {
        pageNumber -= 1;
        if (pageSize <= 0 || pageNumber <= 0) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .build();
        }
        return ResponseEntity.ok(
                                 RestaurantMapper.EntityListToDTOList(
                                                                      isDescending.isPresent() ? restaurantService
                                                                              .getAllRestaurantsSortedByAveragePrice(pageNumber,
                                                                                                                     pageSize,
                                                                                                                     isDescending
                                                                                                                             .get()) : restaurantService
                                                                                                                                     .getAllRestaurantsSortedByAveragePrice(pageNumber,
                                                                                                                                                                            pageSize,
                                                                                                                                                                            false)
                                 )
        );
    }

    @Operation(summary = "Get a list of restaurants from a given page from the repository having the given cuisine type", responses = {@ApiResponse(responseCode = "200", description = "Page returned successfully", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = RestaurantDTO.class)))
    ), @ApiResponse(responseCode = "403", description = "Invalid page requested", content = @Content)
    })
    @GetMapping(value = "/sortedByCuisine")
    public ResponseEntity<List<RestaurantDTO>> getAllRestaurantsSortedByCuisineType(@RequestParam int pageSize,
                                                                                    @RequestParam int pageNumber,
                                                                                    @RequestParam String cuisineType) {
        pageNumber -= 1;
        if (pageSize <= 0 || pageNumber <= 0) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .build();
        }
        return ResponseEntity.ok(
                                 RestaurantMapper.EntityListToDTOList(
                                                                      restaurantService.getAllRestaurantsByCuisine(
                                                                                                                   pageNumber,
                                                                                                                   pageSize,
                                                                                                                   cuisineType)
                                 )
        );
    }
}
