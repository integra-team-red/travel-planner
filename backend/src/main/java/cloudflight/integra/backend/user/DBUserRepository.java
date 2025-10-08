package cloudflight.integra.backend.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DBUserRepository extends JpaRepository<User, Long> {
    User findUserByEmail(String email);
}
