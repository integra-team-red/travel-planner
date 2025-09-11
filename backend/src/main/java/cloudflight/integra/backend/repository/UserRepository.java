package cloudflight.integra.backend.repository;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository {
    UserDetails findUserByEmail(String email);
}
