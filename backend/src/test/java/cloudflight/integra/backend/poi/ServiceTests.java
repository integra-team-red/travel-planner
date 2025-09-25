package cloudflight.integra.backend.poi;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

import cloudflight.integra.backend.city.City;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

@ExtendWith(MockitoExtension.class)
public class ServiceTests {
    @Mock
    DBPOIRepository repo;

    @InjectMocks
    POIServiceImpl service;

    @Test
    void getAllPointsOfInterestSortedByNameTest() {
        City city = new City();
        city.setName("Viena");
        PointOfInterest poi1 =
                new PointOfInterest(1L, "Museum of Science", "Desc", city, 10.0, PointOfInterestType.MUSEUM);
        PointOfInterest poi2 = new PointOfInterest(2L, "Belvedere Park", "Desc", city, 15.0, PointOfInterestType.PARK);
        PointOfInterest poi3 = new PointOfInterest(3L, "Zalha", "Desc", city, 20.0, PointOfInterestType.MONUMENT);

        Page<PointOfInterest> pageAsc = new PageImpl<>(List.of(poi2, poi1, poi3));
        Page<PointOfInterest> pageDesc = new PageImpl<>(List.of(poi3, poi1, poi2));

        when(repo.findAll(PageRequest.of(0, 3, Sort.by(Sort.Direction.ASC, "name"))))
                .thenReturn(pageAsc);

        List<PointOfInterest> resultAsc = service.getAllPointsOfInterestSortedByName(0, 3, false);
        assertThat(resultAsc.get(0).getName()).isEqualTo("Belvedere Park");
        assertThat(resultAsc.get(1).getName()).isEqualTo("Museum of Science");
        assertThat(resultAsc.get(2).getName()).isEqualTo("Zalha");

        when(repo.findAll(PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "name"))))
                .thenReturn(pageDesc);

        List<PointOfInterest> resultDesc = service.getAllPointsOfInterestSortedByName(0, 3, true);
        assertThat(resultDesc.get(0).getName()).isEqualTo("Zalha");
        assertThat(resultDesc.get(1).getName()).isEqualTo("Museum of Science");
        assertThat(resultDesc.get(2).getName()).isEqualTo("Belvedere Park");
    }

    @Test
    void getAllPointsOfInterestSortedByPriceTest() {
        City city = new City();
        city.setName("Viena");

        PointOfInterest poi1 =
                new PointOfInterest(1L, "Museum of Science", "Desc", city, 10.0, PointOfInterestType.MUSEUM);
        PointOfInterest poi2 = new PointOfInterest(2L, "Belvedere Park", "Desc", city, 15.0, PointOfInterestType.PARK);
        PointOfInterest poi3 = new PointOfInterest(3L, "Zalha", "Desc", city, 20.0, PointOfInterestType.MONUMENT);

        Page<PointOfInterest> pageAsc = new PageImpl<>(List.of(poi1, poi2, poi3));
        Page<PointOfInterest> pageDesc = new PageImpl<>(List.of(poi3, poi2, poi1));

        when(repo.findAll(PageRequest.of(0, 3, Sort.by(Sort.Direction.ASC, "price"))))
                .thenReturn(pageAsc);

        List<PointOfInterest> resultAsc = service.getAllPointsOfInterestSortedByPrice(0, 3, false);
        assertThat(resultAsc.get(0).getPrice()).isEqualTo(10.0);
        assertThat(resultAsc.get(1).getPrice()).isEqualTo(15.0);
        assertThat(resultAsc.get(2).getPrice()).isEqualTo(20.0);

        when(repo.findAll(PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "price"))))
                .thenReturn(pageDesc);

        List<PointOfInterest> resultDesc = service.getAllPointsOfInterestSortedByPrice(0, 3, true);
        assertThat(resultDesc.get(0).getPrice()).isEqualTo(20.0);
        assertThat(resultDesc.get(1).getPrice()).isEqualTo(15.0);
        assertThat(resultDesc.get(2).getPrice()).isEqualTo(10.0);
    }

    @Test
    void getAllPointsOfInterestSortedByTypeTest() {
        City city = new City();
        city.setName("Viena");

        PointOfInterest poi1 =
                new PointOfInterest(1L, "Museum of Science", "Desc", city, 10.0, PointOfInterestType.MUSEUM);
        PointOfInterest poi2 = new PointOfInterest(2L, "Belvedere Park", "Desc", city, 15.0, PointOfInterestType.PARK);
        PointOfInterest poi3 = new PointOfInterest(3L, "Art Museum", "Desc", city, 20.0, PointOfInterestType.MUSEUM);

        Page<PointOfInterest> pageMuseum = new PageImpl<>(List.of(poi1, poi3));
        when(repo.findAllByType("MUSEUM", PageRequest.of(0, 3))).thenReturn(pageMuseum);

        List<PointOfInterest> result = service.getAllPointsOfInterestSortedByType(0, 3, "MUSEUM");

        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getType().name()).isEqualTo("MUSEUM");
        assertThat(result.get(1).getType().name()).isEqualTo("MUSEUM");
    }
}
