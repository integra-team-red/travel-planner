package cloudflight.integra.backend.model;

import jakarta.persistence.Entity;
import org.springframework.data.annotation.Id;

public class User {
    public String email;
    public String password;

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }
}
