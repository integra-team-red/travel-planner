package cloudflight.integra.backend.repository;

import cloudflight.integra.backend.model.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DBCityRepository extends JpaRepository<City, Integer> {}
