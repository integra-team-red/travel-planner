package cloudflight.integra.backend.model;

import cloudflight.integra.backend.city.City;
import jakarta.persistence.*;

@Entity
@Table(name = "restaurant")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private Long id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    private City city;
    private Double averagePrice;
    private String cuisineType;

    public Long getId() { return id; }

    public Restaurant setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() { return name; }

    public Restaurant setName(String name) {
        this.name = name;
        return this;
    }

    public City getCity() { return city; }

    public Restaurant setCity(City city) {
        this.city = city;
        return this;
    }

    public Double getAveragePrice() { return averagePrice; }

    public Restaurant setAveragePrice(Double averagePrice) {
        this.averagePrice = averagePrice;
        return this;
    }

    public String getCuisineType() { return cuisineType; }

    public Restaurant setCuisineType(String cuisineType) {
        this.cuisineType = cuisineType;
        return this;
    }

    public Long getCityId() { return city.getId(); }

}
