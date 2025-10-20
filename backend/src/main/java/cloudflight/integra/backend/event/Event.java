package cloudflight.integra.backend.event;

import cloudflight.integra.backend.poi.POI;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    Long id;

    String name;
    String description;
    Double price;
    Date startTime;
    Date endTime;

    @ManyToOne
    @JoinColumn(name = "poi_id", nullable = false)
    private POI poi;

    @Enumerated(EnumType.STRING)
    EventAudience audience;

    public Event() {}

    public Event(
            Long id,
            String name,
            String description,
            Double price,
            Date startTime,
            Date endTime,
            POI poi,
            EventAudience audience) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.startTime = startTime;
        this.endTime = endTime;
        this.poi = poi;
        this.audience = audience;
    }

    public Long getId() {
        return id;
    }

    public Event setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Event setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Event setDescription(String description) {
        this.description = description;
        return this;
    }

    public Double getPrice() {
        return price;
    }

    public Event setPrice(Double price) {
        this.price = price;
        return this;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Event setStartTime(Date startTime) {
        this.startTime = startTime;
        return this;
    }

    public Date getEndTime() {
        return endTime;
    }

    public Event setEndTime(Date endTime) {
        this.endTime = endTime;
        return this;
    }

    public POI getPoi() {
        return poi;
    }

    public Event setPoi(POI poi) {
        this.poi = poi;
        return this;
    }

    public Long getPoiId() {
        return poi.getId();
    }

    public EventAudience getAudience() {
        return audience;
    }

    public Event setAudience(EventAudience audience) {
        this.audience = audience;
        return this;
    }
}
