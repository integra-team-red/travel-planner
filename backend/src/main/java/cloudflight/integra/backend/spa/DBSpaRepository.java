package cloudflight.integra.backend.spa;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DBSpaRepository extends JpaRepository<Spa, Long> {
    List<Spa> findByCity_Id(Long cityId);
}
