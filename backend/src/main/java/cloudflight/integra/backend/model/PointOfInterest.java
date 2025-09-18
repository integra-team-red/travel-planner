package cloudflight.integra.backend.model;

import cloudflight.integra.backend.city.City;
import jakarta.persistence.*;

@Entity
@Table(name = "point_of_interest")
public class PointOfInterest {

    public enum PointOfInterestType {
        MUSEUM, LANDMARK, PARK, MONUMENT, OTHER
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    Long id;

    String name;
    String description;

    // @Column(columnDefinition = "serial")
    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    Double price;

    @Enumerated(EnumType.STRING)
    PointOfInterestType type;

    public PointOfInterest() {
    }

    public PointOfInterest(Long id, String name, String description, City city, Double price,
                           PointOfInterestType type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.city = city;
        this.price = price;
        this.type = type;
    }

    public Long getId() { return id; }

    public PointOfInterest setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() { return name; }

    public PointOfInterest setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() { return description; }

    public PointOfInterest setDescription(String description) {
        this.description = description;
        return this;
    }

    public Long getCityId() { return city.getId(); }

    public String getCityName() { return city.getName(); }

    public City getCity() { return city; }

    public PointOfInterest setCity(City city) {
        this.city = city;
        return this;
    }

    public String getCityName() { return city.getName(); }

    public Double getPrice() { return price; }

    public PointOfInterest setPrice(Double price) {
        this.price = price;
        return this;
    }

    public PointOfInterestType getType() { return type; }

    public PointOfInterest setType(PointOfInterestType type) {
        this.type = type;
        return this;
    }
}
