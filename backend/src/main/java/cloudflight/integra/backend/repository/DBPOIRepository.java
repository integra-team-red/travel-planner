package cloudflight.integra.backend.repository;

import cloudflight.integra.backend.model.PointOfInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DBPOIRepository extends JpaRepository<PointOfInterest, Long> {
    List<PointOfInterest> findByCity_Id(Long cityId);

    List<PointOfInterest> findByCity_Name(String cityName);
}
