package org.example.routeapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Objects;

@Entity
@Table(name = "locations")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id", nullable = false)
    private Long locationId;

    @NotBlank(message = "name is mandatory")
    @Size(max = 100, message = "name should be maximum 100 characters")
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank(message = "country is mandatory")
    @Size(max = 100, message = "country should be maximum 100 characters")
    @Column(name = "country", nullable = false)
    private String country;

    @NotBlank(message = "city is mandatory")
    @Size(max = 100, message = "city should be maximum 100 characters")
    @Column(name = "city", nullable = false)
    private String city;

    @NotBlank(message = "location code is mandatory")
    @Size(max = 5, message = "location code should be maximum 5 characters")
    @Column(name = "location_code", nullable = false, unique = true)
    private String locationCode;

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Objects.equals(locationId, location.locationId) && Objects.equals(name, location.name) && Objects.equals(country, location.country) && Objects.equals(city, location.city) && Objects.equals(locationCode, location.locationCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(locationId, name, country, city, locationCode);
    }

    @Override
    public String toString() {
        return "Location{" +
                "locationId=" + locationId +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", locationCode='" + locationCode + '\'' +
                '}';
    }
}
