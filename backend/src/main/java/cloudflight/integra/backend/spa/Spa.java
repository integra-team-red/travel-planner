package cloudflight.integra.backend.spa;

import cloudflight.integra.backend.city.City;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "spa")
public class Spa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private Long id;

    @NotNull
    private String name;

    @NotNull
    @Pattern(regexp = "[0-9]{2}-[0-9]{2}", message = "Spa doesn't conform to the pattern (XX:XX)")
    private String schedule;

    @NotNull
    private Double priceLowerBound;

    @NotNull
    private Double priceUpperBound;

    @NotNull
    private Long rating;

    private String description;

    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    public Long getId() {
        return id;
    }

    public Spa setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Spa setName(String name) {
        this.name = name;
        return this;
    }

    public String getSchedule() {
        return schedule;
    }

    public Spa setSchedule(String schedule) {
        this.schedule = schedule;
        return this;
    }

    public @NotNull Double getPriceLowerBound() {
        return priceLowerBound;
    }

    public Spa setPriceLowerBound(@NotNull Double priceLowerBound) {
        this.priceLowerBound = priceLowerBound;
        return this;
    }

    public @NotNull Double getPriceUpperBound() {
        return priceUpperBound;
    }

    public Spa setPriceUpperBound(@NotNull Double priceUpperBound) {
        this.priceUpperBound = priceUpperBound;
        return this;
    }

    public @NotNull Long getRating() {
        return rating;
    }

    public Spa setRating(@NotNull Long rating) {
        this.rating = rating;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Spa setDescription(String description) {
        this.description = description;
        return this;
    }

    public City getCity() {
        return city;
    }

    public Spa setCity(City city) {
        this.city = city;
        return this;
    }
}
