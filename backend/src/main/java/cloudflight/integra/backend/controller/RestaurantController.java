package cloudflight.integra.backend.controller;


import cloudflight.integra.backend.model.Restaurant;
import cloudflight.integra.backend.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/restaurant")
public class RestaurantController {
    private RestaurantService restaurantService;

    @Autowired
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping
    public ResponseEntity<List<Restaurant>> getRestaurants() {
        try {
            List<Restaurant> restaurants = restaurantService.getAllRestaurants();
            return ResponseEntity.ok(restaurants);
        } catch (Exception e) {
            e.printStackTrace(); // log the error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<Restaurant> addRestaurant(@RequestBody Restaurant restaurant) {
        return ResponseEntity.ok(restaurantService.addRestaurant(restaurant));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Restaurant> deleteRestaurant(@PathVariable Long id) {
        Restaurant deletedRestaurant= restaurantService.deleteRestaurant(id);
        if(deletedRestaurant ==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(deletedRestaurant);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Restaurant> updateRestaurant(@PathVariable Long id, @RequestBody Restaurant restaurant) {
        Restaurant updatedRestaurant= restaurantService.updateRestaurant(id, restaurant);
        if(updatedRestaurant ==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(updatedRestaurant);
    }
}
