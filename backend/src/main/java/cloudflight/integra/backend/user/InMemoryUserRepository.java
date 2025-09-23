package cloudflight.integra.backend.user;

import java.util.HashMap;
import java.util.function.Supplier;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private final HashMap<String, Supplier<UserDetails>> users;

    public InMemoryUserRepository() {
        users = new HashMap<>();
        users.put("user", () -> User.withUsername("ROLE_user")
                .password("user")
                .roles("user")
                .build());

        users.put("admin", () -> User.withUsername("ROLE_admin")
                .password("admin")
                .roles("admin")
                .build());
    }

    public UserDetails findUserByEmail(String email) {
        return users.get(email).get();
    }
}
