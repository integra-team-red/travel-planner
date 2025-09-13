package cloudflight.integra.backend.model;

public class Restaurant {
    private Long id;
    private String name;
    private Long cityId;
    private Double averagePrice;
    private String cuisineType;

    public Long getCityId() { return cityId; }

    public Restaurant(Long id, String name, Long cityId, Double averagePrice, String cuisineType) {
        this.id = id;
        this.name = name;
        this.cityId = cityId;
        this.averagePrice = averagePrice;
        this.cuisineType = cuisineType;
    }

    public Restaurant() {
    }

    public String getCuisineType() { return cuisineType; }

    public String getName() { return name; }

    public Restaurant setName(String name) {
        this.name = name;
        return this;
    }

    public Restaurant setCuisineType(String cuisineType) {
        this.cuisineType = cuisineType;
        return this;
    }

    public Long getId() { return id; }

    public Double getAveragePrice() { return averagePrice; }

    public Restaurant setAveragePrice(Double averagePrice) {
        this.averagePrice = averagePrice;
        return this;
    }

    public Restaurant setId(Long id) {
        this.id = id;
        return this;
    }

    public Restaurant setCityId(Long cityId) {
        this.cityId = cityId;
        return this;
    }
}
