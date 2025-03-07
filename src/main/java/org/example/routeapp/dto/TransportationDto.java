package org.example.routeapp.dto;

import org.example.routeapp.model.TransportationType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TransportationDto {
    String originName;
    String destinationName;
    List<TransportationType> transportationType;

    public TransportationDto(String originName, String destinationName, List<TransportationType> transportationType) {
        this.originName = originName;
        this.destinationName = destinationName;
        this.transportationType = new ArrayList<>(transportationType);
    }

    public List<TransportationType> getTransportationType() {
        return transportationType;
    }

    public void setTransportationType(List<TransportationType> transportationType) {
        this.transportationType = transportationType;
    }

    public void addTransportationType(List<TransportationType> additionalTransportationTypes) {
        // Combine the transportation types
        for (TransportationType type : additionalTransportationTypes) {
            if (!this.transportationType.contains(type)) {
                this.transportationType.add(type);
            }
        }
    }

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TransportationDto that = (TransportationDto) o;
        return Objects.equals(originName, that.originName) && Objects.equals(destinationName, that.destinationName)
                && transportationType.size() == that.transportationType.size()
                && transportationType.containsAll(that.transportationType) && that.transportationType.containsAll(transportationType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(originName, destinationName, transportationType);
    }

    @Override
    public String toString() {
        return "TransportationDto{" +
                "originName='" + originName + '\'' +
                ", destinationName='" + destinationName + '\'' +
                ", transportationType=" + transportationType +
                '}';
    }
}
