package org.example.routeapp.dto;

import org.example.routeapp.model.Transportation;
import org.example.routeapp.model.TransportationType;

import java.util.Objects;

public class RouteDto
{
    TransportationDto beforeFlight;
    TransportationDto afterFlight;
    TransportationDto flight;

    public RouteDto(){

    }

    public TransportationDto getBeforeFlight() {
        return beforeFlight;
    }

    public void setBeforeFlight(TransportationDto beforeFlight) {
        this.beforeFlight = beforeFlight;
    }

    public TransportationDto getAfterFlight() {
        return afterFlight;
    }

    public void setAfterFlight(TransportationDto afterFlight) {
        this.afterFlight = afterFlight;
    }

    public TransportationDto getFlight() {
        return flight;
    }

    public void setFlight(TransportationDto flight) {
        this.flight = flight;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RouteDto routeDto = (RouteDto) o;
        return Objects.equals(beforeFlight, routeDto.beforeFlight) && Objects.equals(afterFlight, routeDto.afterFlight) && Objects.equals(flight, routeDto.flight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(beforeFlight, afterFlight, flight);
    }

    @Override
    public String toString() {
        return "RouteDto{" +
                "beforeFlight=" + beforeFlight +
                ", afterFlight=" + afterFlight +
                ", flight=" + flight +
                '}';
    }
}
