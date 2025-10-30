package cloudflight.integra.backend.restaurant;

import cloudflight.integra.backend.city.City;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "restaurant")
public class Restaurant {

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
    private String cuisineType;

    @NotNull
    private Double rating;

    private String image;

    public Restaurant(
            Long id,
            String name,
            City city,
            String address,
            String openingHours,
            String description,
            Double averagePrice,
            String cuisineType,
            Double rating,
            String image) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.address = address;
        this.openingHours = openingHours;
        this.description = description;
        this.averagePrice = averagePrice;
        this.cuisineType = cuisineType;
        this.rating = rating;
        this.image = image;
    }

    public Restaurant() {}

    public Long getId() {
        return id;
    }

    public Restaurant setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Restaurant setName(String name) {
        this.name = name;
        return this;
    }

    public City getCity() {
        return city;
    }

    public Restaurant setCity(City city) {
        this.city = city;
        return this;
    }

    public Long getCityId() {
        return city.getId();
    }

    public String getAddress() {
        return address;
    }

    public Restaurant setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public Restaurant setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Restaurant setDescription(String description) {
        this.description = description;
        return this;
    }

    public Double getAveragePrice() {
        return averagePrice;
    }

    public Restaurant setAveragePrice(Double averagePrice) {
        this.averagePrice = averagePrice;
        return this;
    }

    public String getCuisineType() {
        return cuisineType;
    }

    public Restaurant setCuisineType(String cuisineType) {
        this.cuisineType = cuisineType;
        return this;
    }

    public Double getRating() {
        return rating;
    }

    public Restaurant setRating(Double rating) {
        this.rating = rating;
        return this;
    }

    public String getImage() {
        return image;
    }

    public Restaurant setImage(String image) {
        this.image = image;
        return this;
    }
}
