package cloudflight.integra.backend.controller;


import cloudflight.integra.backend.DTO.RestaurantDTO;
import cloudflight.integra.backend.mapper.RestaurantMapper;
import cloudflight.integra.backend.model.Restaurant;
import cloudflight.integra.backend.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/restaurant")
public class RestaurantController {
    private RestaurantService restaurantService;

    @Autowired
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<RestaurantDTO> addRestaurant(@RequestBody RestaurantDTO restaurantDTO) {
        Restaurant savedRestaurant = restaurantService.addRestaurant(RestaurantMapper.RestaurantToEntity(restaurantDTO));
        return ResponseEntity.ok(RestaurantMapper.RestaurantToDTO(savedRestaurant));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<RestaurantDTO> deleteRestaurant(@PathVariable Long id) {
        Restaurant deletedRestaurant= restaurantService.deleteRestaurant(id);
        if(deletedRestaurant ==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(RestaurantMapper.RestaurantToDTO(deletedRestaurant));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<RestaurantDTO> updateRestaurant(@PathVariable Long id, @RequestBody RestaurantDTO newRestaurantDTO) {
        Restaurant updatedRestaurant= restaurantService.updateRestaurant(id, RestaurantMapper.RestaurantToEntity(newRestaurantDTO));
        if(updatedRestaurant ==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(RestaurantMapper.RestaurantToDTO(updatedRestaurant));
    }
}
