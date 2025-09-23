package cloudflight.integra.backend.poi;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DBPOIRepository extends JpaRepository<PointOfInterest, Long> {
    List<PointOfInterest> findByCity_Id(Long cityId);

    List<PointOfInterest> findByCity_Name(String cityName);

    @Query("SELECT p FROM PointOfInterest p WHERE LOWER(p.type) = LOWER(:type)")
    Page<PointOfInterest> findAllByType(@Param("type") String type, Pageable pageable);
}
