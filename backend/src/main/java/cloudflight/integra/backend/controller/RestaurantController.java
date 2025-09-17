package cloudflight.integra.backend.controller;

import cloudflight.integra.backend.DTO.RestaurantDTO;
import cloudflight.integra.backend.city.City;
import cloudflight.integra.backend.mapper.RestaurantMapper;
import cloudflight.integra.backend.model.Restaurant;
import cloudflight.integra.backend.service.CityService;
import cloudflight.integra.backend.service.RestaurantService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/restaurant")
public class RestaurantController {
    private RestaurantService restaurantService;
    private CityService cityService;

    @Autowired
    public RestaurantController(RestaurantService restaurantService, CityService cityService) {
        this.restaurantService = restaurantService;
        this.cityService = cityService;
    }

    @GetMapping
    public ResponseEntity<List<RestaurantDTO>> getRestaurants() {
        try {
            List<RestaurantDTO> restaurants = restaurantService.getAllRestaurants()
                    .stream()
                    .map(RestaurantMapper::RestaurantToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(restaurants);
        } catch (Exception e) {
            e.printStackTrace(); // log the error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    @PostMapping
    public ResponseEntity<RestaurantDTO> addRestaurant(@RequestBody RestaurantDTO restaurantDTO) {
        City city = cityService.getCity(restaurantDTO.cityId());
        Restaurant savedRestaurant = restaurantService.addRestaurant(RestaurantMapper
                .RestaurantToEntity(restaurantDTO, city));
        return ResponseEntity.ok(RestaurantMapper.RestaurantToDTO(savedRestaurant));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<RestaurantDTO> deleteRestaurant(@PathVariable Long id) {
        restaurantService.deleteRestaurant(id);
        return ResponseEntity.status(HttpStatus.OK)
                .build();

    }

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
    @GetMapping(value="/bycity")
    public List<Restaurant> getRestaurantsByCity(@RequestParam(required = false) Long cityId, @RequestParam(required = false) String cityName){
        return restaurantService.getRestaurantsByCity(cityId,cityName);
    }
}
