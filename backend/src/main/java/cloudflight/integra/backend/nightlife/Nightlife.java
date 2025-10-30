package cloudflight.integra.backend.nightlife;

import cloudflight.integra.backend.city.City;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Table(name = "nightlife")
@Getter
@Setter
@Accessors(chain = true)
public class Nightlife {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private Long id;

    @NotNull
    private String name;

    @NotNull
    @Pattern(regexp = "[0-9]{2}-[0-9]{2}", message = "Nightlife doesn't conform to the pattern (XX:XX)")
    private String schedule;

    @NotNull
    private Double priceLowerBound;

    @NotNull
    private Double priceUpperBound;

    @NotNull
    private Long rating;

    @NotNull
    @Enumerated(EnumType.STRING)
    private NightlifeType type;

    private String description;

    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    private City city;
}
