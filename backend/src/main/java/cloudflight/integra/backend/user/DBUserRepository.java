package cloudflight.integra.backend.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DBUserRepository extends JpaRepository<User, Long> {
    User findUserByEmail(String email);
}
