package cloudflight.integra.backend.city;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DBCityRepository extends JpaRepository<City, Long> {
    City findByName(String name);
}
