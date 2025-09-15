package cloudflight.integra.backend.restaurant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DBRestaurantRepository extends JpaRepository<Restaurant, Long> {
    @Query("SELECT r FROM Restaurant r WHERE LOWER(r.cuisineType) = LOWER(:type)")
    Page<Restaurant> findAllByCuisine(@Param("type") String cuisineType, Pageable pageable);
}
