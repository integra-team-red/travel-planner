package cloudflight.integra.backend.proposal;

import cloudflight.integra.backend.city.City;
import cloudflight.integra.backend.poi.PointOfInterestType;
import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "proposals")
public class Proposal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProposalType type;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    private String description;
    private String address;
    private Double price;
    private Double averagePrice;
    private String cuisineType;
    private String image;

    @Enumerated(EnumType.STRING)
    private PointOfInterestType poiType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProposalType getType() {
        return type;
    }

    public void setType(ProposalType type) {
        this.type = type;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public Proposal setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getImage() {
        return image;
    }

    public Proposal setImage(String image) {
        this.image = image;
        return this;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(Double averagePrice) {
        this.averagePrice = averagePrice;
    }

    public String getCuisineType() {
        return cuisineType;
    }

    public void setCuisineType(String cuisineType) {
        this.cuisineType = cuisineType;
    }

    public PointOfInterestType getPoiType() {
        return poiType;
    }

    public void setPoiType(PointOfInterestType poiType) {
        this.poiType = poiType;
    }

    @Override
    public String toString() {
        return "Proposal{" + "id=" + id + ", name='" + name + '\'' + ", type=" + type + ", status=" + status + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Proposal proposal = (Proposal) o;
        return Objects.equals(id, proposal.id)
                && Objects.equals(name, proposal.name)
                && type == proposal.type
                && status == proposal.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type, status);
    }
}
