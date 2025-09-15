package cloudflight.integra.backend.user;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository {
    UserDetails findUserByEmail(String email);
}
