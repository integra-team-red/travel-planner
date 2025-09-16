package cloudflight.integra.backend.repository;

import cloudflight.integra.backend.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DBRestaurantRepository extends JpaRepository<Restaurant, Long> {}
