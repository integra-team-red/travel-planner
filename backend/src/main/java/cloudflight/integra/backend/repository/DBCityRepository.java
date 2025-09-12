package cloudflight.integra.backend.repository;

import cloudflight.integra.backend.city.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DBCityRepository extends JpaRepository<City, Long> {
    City findByName(String name);
}
