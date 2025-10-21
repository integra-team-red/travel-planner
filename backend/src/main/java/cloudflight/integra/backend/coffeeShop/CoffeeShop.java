package cloudflight.integra.backend.coffeeShop;

import cloudflight.integra.backend.city.City;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "coffee_shop")
public class CoffeeShop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private Long id;

    @NotNull
    private String name;

    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    @NotNull
    private String address;

    @NotNull
    private String openingHours;

    @NotNull
    private String description;

    @NotNull
    private Double averagePrice;

    @NotNull
    private Double rating;

    private String image;

    public CoffeeShop() {}

    public CoffeeShop(
            Long id,
            String name,
            City city,
            String address,
            String openingHours,
            String description,
            Double averagePrice,
            Double rating,
            String image) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.address = address;
        this.openingHours = openingHours;
        this.description = description;
        this.averagePrice = averagePrice;
        this.rating = rating;
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public CoffeeShop setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public CoffeeShop setName(String name) {
        this.name = name;
        return this;
    }

    public City getCity() {
        return city;
    }

    public CoffeeShop setCity(City city) {
        this.city = city;
        return this;
    }

    public Long getCityId() {
        return city.getId();
    }

    public String getAddress() {
        return address;
    }

    public CoffeeShop setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public CoffeeShop setOpeningHours(String program) {
        this.openingHours = program;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public CoffeeShop setDescription(String description) {
        this.description = description;
        return this;
    }

    public Double getAveragePrice() {
        return averagePrice;
    }

    public CoffeeShop setAveragePrice(Double averagePrice) {
        this.averagePrice = averagePrice;
        return this;
    }

    public Double getRating() {
        return rating;
    }

    public CoffeeShop setRating(Double rating) {
        this.rating = rating;
        return this;
    }

    public String getImage() {
        return image;
    }

    public CoffeeShop setImage(String image) {
        this.image = image;
        return this;
    }
}
