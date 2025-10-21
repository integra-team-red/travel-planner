package cloudflight.integra.backend.coffeeShop;

import cloudflight.integra.backend.city.City;
import cloudflight.integra.backend.city.CityService;
import cloudflight.integra.backend.restaurant.RestaurantDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coffee-shop")
@SecurityRequirement(name = "bearerAuth")
public class CoffeeShopController {
    private final CoffeeShopService coffeeService;
    private final CityService cityService;

    @Autowired
    public CoffeeShopController(final CoffeeShopService coffeeService, final CityService cityService) {
        this.coffeeService = coffeeService;
        this.cityService = cityService;
    }

    @Operation(
            summary = "Get a list of all CoffeeShops from the repository",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "All CoffeeShops returned successfully",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        array = @ArraySchema(schema = @Schema(implementation = CoffeeShopDTO.class)))),
            })
    @GetMapping
    public ResponseEntity<List<CoffeeShopDTO>> getCoffeeShops() {
        List<CoffeeShopDTO> coffees = coffeeService.getAllCoffeeShops().stream()
                .map(CoffeeShopMapper::EntityToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(coffees);
    }

    @Operation(
            summary = "Add a CoffeeShop to the repository",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "CoffeeShop added successfully; returns the added CoffeeShop",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = CoffeeShopDTO.class))),
                @ApiResponse(responseCode = "422", description = "Invalid CoffeeShop supplied", content = @Content),
                @ApiResponse(
                        responseCode = "404",
                        description = "City id of the CoffeeShop not found",
                        content = @Content)
            })
    @PostMapping
    public ResponseEntity<CoffeeShopDTO> addCoffeeShop(@RequestBody CoffeeShopDTO coffeeDTO) {
        City city = cityService.getCity(coffeeDTO.cityId());
        CoffeeShop savedCoffeeShop = coffeeService.addCoffeeShop(CoffeeShopMapper.DTOtoEntity(coffeeDTO, city));
        return ResponseEntity.ok(CoffeeShopMapper.EntityToDTO(savedCoffeeShop));
    }

    @Operation(
            summary = "Delete a CoffeeShop from the repository",
            responses = {
                @ApiResponse(responseCode = "200", description = "CoffeeShop deleted successfully", content = @Content)
            })
    @DeleteMapping(value = "/{id}")
    ResponseEntity<HttpStatus> deleteCoffeeShop(@PathVariable Long id) {
        coffeeService.deleteCoffeeShop(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(
            summary = "Update a CoffeeShop's data with the provided CoffeeShop",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "CoffeeShop updated successfully; returns the updated CoffeeShop",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = CoffeeShopDTO.class))),
                @ApiResponse(responseCode = "404", description = "CoffeeShop to update not found", content = @Content),
                @ApiResponse(
                        responseCode = "404",
                        description = "City id of the CoffeeShop not found",
                        content = @Content),
                @ApiResponse(responseCode = "422", description = "Invalid CoffeeShop supplied", content = @Content)
            })
    @PutMapping(value = "/{id}")
    public ResponseEntity<CoffeeShopDTO> updateCoffeeShop(
            @PathVariable Long id, @RequestBody CoffeeShopDTO newCoffeeShopDTO) {
        if (newCoffeeShopDTO.id() != null && newCoffeeShopDTO.id() != 0) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
        City city = cityService.getCity(newCoffeeShopDTO.cityId());
        CoffeeShop updatedCoffeeShop =
                coffeeService.updateCoffeeShop(id, CoffeeShopMapper.DTOtoEntity(newCoffeeShopDTO, city));
        return ResponseEntity.ok(CoffeeShopMapper.EntityToDTO(updatedCoffeeShop));
    }

    @Operation(
            summary =
                    "Get a list of coffee shops from a given page from the repository sorted in given direction by name",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Page returned successfully",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        array = @ArraySchema(schema = @Schema(implementation = CoffeeShopDTO.class)))),
                @ApiResponse(responseCode = "406", description = "Invalid page requested", content = @Content)
            })
    @GetMapping(value = "/sortedByName")
    public ResponseEntity<List<CoffeeShopDTO>> getAllCoffeeShopsSortedByName(
            @RequestParam int pageSize, @RequestParam int pageNumber, @RequestParam Optional<Boolean> isDescending) {
        return ResponseEntity.ok(CoffeeShopMapper.EntityListToDTOList(
                isDescending.isPresent()
                        ? coffeeService.getAllCoffeeShopsSortedByName(pageNumber, pageSize, isDescending.get())
                        : coffeeService.getAllCoffeeShopsSortedByName(pageNumber, pageSize, false)));
    }

    @Operation(
            summary =
                    "Get a list of coffee shops from a given page from the repository sorted in given direction by average price",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Page returned successfully",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        array = @ArraySchema(schema = @Schema(implementation = CoffeeShopDTO.class)))),
                @ApiResponse(responseCode = "406", description = "Invalid page requested", content = @Content)
            })
    @GetMapping(value = "/sortedByPrice")
    public ResponseEntity<List<CoffeeShopDTO>> getAllCoffeeShopsSortedByAveragePrice(
            @RequestParam int pageSize, @RequestParam int pageNumber, @RequestParam Optional<Boolean> isDescending) {
        return ResponseEntity.ok(CoffeeShopMapper.EntityListToDTOList(
                isDescending.isPresent()
                        ? coffeeService.getAllCoffeeShopsSortedByAveragePrice(pageNumber, pageSize, isDescending.get())
                        : coffeeService.getAllCoffeeShopsSortedByAveragePrice(pageNumber, pageSize, false)));
    }

    @Operation(
            summary = "Get a list of restaurants from a given page from the repository having the given rating",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Page returned successfully",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        array = @ArraySchema(schema = @Schema(implementation = RestaurantDTO.class)))),
                @ApiResponse(responseCode = "406", description = "Invalid page requested", content = @Content)
            })
    @GetMapping(value = "/sortedByRating")
    public ResponseEntity<List<CoffeeShopDTO>> getAllCoffeeShopsSortedByRating(
            @RequestParam int pageSize, @RequestParam int pageNumber, @RequestParam Optional<Boolean> isDescending) {
        return ResponseEntity.ok(CoffeeShopMapper.EntityListToDTOList(
                isDescending.isPresent()
                        ? coffeeService.getAllCoffeeShopsSortedByRating(pageNumber, pageSize, isDescending.get())
                        : coffeeService.getAllCoffeeShopsSortedByRating(pageNumber, pageSize, false)));
    }
}
