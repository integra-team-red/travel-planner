package cloudflight.integra.backend.repository;

import cloudflight.integra.backend.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DBRestaurantRepository extends JpaRepository<Restaurant, Long> {
    List<Restaurant> findByCity_Id(Long cityId );
    List<Restaurant> findByCity_Name(String name);
}

