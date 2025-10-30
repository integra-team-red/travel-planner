package cloudflight.integra.backend.nightlife;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NightlifeRepository extends JpaRepository<Nightlife, Long> {
    List<Nightlife> findByCity_Id(Long cityId);
}
