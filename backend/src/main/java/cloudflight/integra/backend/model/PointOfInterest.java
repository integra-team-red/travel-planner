package cloudflight.integra.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name="point_of_interest")
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
    @Column(columnDefinition = "serial")
    Long cityId;
    Double price;
    @Enumerated(EnumType.STRING)
    PointOfInterestType type;

    public PointOfInterest() {}

    public PointOfInterest(Long id, String name, String description, Long cityId, Double price, PointOfInterestType type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.cityId = cityId;
        this.price = price;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public PointOfInterest setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public PointOfInterest setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public PointOfInterest setDescription(String description) {
        this.description = description;
        return this;
    }

    public Long getCityId() {
        return cityId;
    }

    public PointOfInterest setCityId(Long cityId) {
        this.cityId = cityId;
        return this;
    }

    public Double getPrice() {
        return price;
    }

    public PointOfInterest setPrice(Double price) {
        this.price = price;
        return this;
    }

    public PointOfInterestType getType() {
        return type;
    }

    public PointOfInterest setType(PointOfInterestType type) {
        this.type = type;
        return this;
    }
}
