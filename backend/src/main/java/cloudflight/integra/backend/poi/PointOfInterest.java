package cloudflight.integra.backend.poi;

import cloudflight.integra.backend.city.City;
import jakarta.persistence.*;

@Entity
@Table(name = "point_of_interest")
public class PointOfInterest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    Long id;

    String name;
    String description;
    String address;

    // @Column(columnDefinition = "serial")
    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    Double price;

    @Enumerated(EnumType.STRING)
    PointOfInterestType type;

    String image;

    public PointOfInterest() {}

    public PointOfInterest(
            Long id, String name, String description, City city, Double price, PointOfInterestType type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.city = city;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getCityId() {
        return city.getId();
    }

    public City getCity() {
        return city;
    }

    public PointOfInterest setCity(City city) {
        this.city = city;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
