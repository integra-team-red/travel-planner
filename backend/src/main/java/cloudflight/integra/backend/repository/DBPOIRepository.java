package cloudflight.integra.backend.repository;

import cloudflight.integra.backend.model.PointOfInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DBPOIRepository extends JpaRepository<PointOfInterest, Long> {
}
