package cloudflight.integra.backend.city;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

@Entity
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    Long id;

    @Pattern(regexp = "^([A-Za-z]+(-|\\s))*[A-Za-z]+$", message = "City name doesn't match the standard pattern")
    @Length(min = 2, max = 32, message = "City name must be between 2 and 32 characters long inclusive")
    @NotNull
    String name;

    public Long getId() {
        return id;
    }

    public City setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public City setName(String name) {
        this.name = name;
        return this;
    }
}
