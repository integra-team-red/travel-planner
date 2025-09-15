package cloudflight.integra.backend.restaurant;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServiceTests {
    @Mock
    DBRestaurantRepository repository;
    @Mock
    Pageable pageable;
    @InjectMocks
    RestaurantServiceImpl service;
    PageImpl<Restaurant> page;

    @Test
    public void getAllRestaurantsByCuisine() {
        page = new PageImpl<>(List.of(
                                      new Restaurant().setCuisineType("Romana"),
                                      new Restaurant().setCuisineType("Engleza"),
                                      new Restaurant().setCuisineType("Spaniola"),
                                      new Restaurant().setCuisineType("Germana")
        ));
        when(repository.findAllByCuisine("Romana", PageRequest.of(1, 1))).thenReturn(page);
        when(repository.findAllByCuisine("romana", PageRequest.of(1, 1))).thenReturn(Page.empty());
        assertThat(service.getAllRestaurantsByCuisine(1, 1, "Romana")
                .stream()
                .map(Restaurant::getCuisineType)
                .toList()).asString()
                .contains("Romana");
        assertThat(service.getAllRestaurantsByCuisine(1, 1, "romana")
                .stream()
                .map(Restaurant::getCuisineType)
                .toList()).asString()
                .doesNotContain("romana");
    }

    @Test
    public void getAllRestaurantsSortedByAveragePrice() {

    }

    @Test
    public void getAllRestaurantsSortedByName() {

    }
}
