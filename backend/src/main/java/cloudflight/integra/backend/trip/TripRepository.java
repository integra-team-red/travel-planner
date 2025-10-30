package cloudflight.integra.backend.trip;

import cloudflight.integra.backend.user.User;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip> findByCityIgnoreCase(String city, PageRequest pageRequest);

    List<Trip> findByUser(User user);
}
