package cloudflight.integra.backend.event;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByPoi_Id(Long poiId);

    List<Event> findByPoi_Name(String poiName);

    @Query("SELECT e FROM Event e WHERE LOWER(e.audience) = LOWER(:audience)")
    Page<Event> findAllByAudience(@Param("audience") String audience, Pageable pageable);
}
