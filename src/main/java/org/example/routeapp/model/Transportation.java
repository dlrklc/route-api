package org.example.routeapp.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "transportations")
public class Transportation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transportation_id", nullable = false)
    private Long transportationId;

    @ManyToOne
    @JoinColumn(name = "origin_location_id", nullable = false)
    private Location originLocation;

    @ManyToOne
    @JoinColumn(name = "destination_location_id", nullable = false)
    private Location destinationLocation;

    @Column(name = "transportation_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransportationType transportationType;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "operating_days", columnDefinition = "integer[]", nullable = false)
    private List<Integer> operatingDays;

    public List<Integer> getOperatingDays() {
        return operatingDays;
    }
    public void setOperatingDays(List<Integer> operatingDays) {
        this.operatingDays = operatingDays;
    }

    public Long getTransportationId() {
        return transportationId;
    }

    public void setTransportationId(Long transportationId) {
        this.transportationId = transportationId;
    }

    public TransportationType getTransportationType() {
        return transportationType;
    }

    public void setTransportationType(TransportationType transportationType) {
        this.transportationType = transportationType;
    }

    public Location getOriginLocation() {
        return originLocation;
    }

    public void setOriginLocation(Location originLocation) {
        this.originLocation = originLocation;
    }

    public Location getDestinationLocation() {
        return destinationLocation;
    }

    public void setDestinationLocation(Location destinationLocation) {
        this.destinationLocation = destinationLocation;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Transportation that = (Transportation) o;
        return Objects.equals(transportationId, that.transportationId) && Objects.equals(originLocation, that.originLocation) && Objects.equals(destinationLocation, that.destinationLocation) && transportationType == that.transportationType && Objects.equals(operatingDays, that.operatingDays);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transportationId, originLocation, destinationLocation, transportationType, operatingDays);
    }

    @Override
    public String toString() {
        return "Transportation{" +
                "transportationId=" + transportationId +
                ", originLocation=" + originLocation +
                ", destinationLocation=" + destinationLocation +
                ", transportationType=" + transportationType +
                ", operatingDays=" + operatingDays +
                '}';
    }
}
