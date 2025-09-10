package cloudflight.integra.backend.repository;

import cloudflight.integra.backend.model.City;
import cloudflight.integra.backend.model.PointOfInterest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DBCityRepository extends JpaRepository<City, Long> {
    City findByName(String name);
}
