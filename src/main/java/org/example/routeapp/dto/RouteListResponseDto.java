package org.example.routeapp.dto;

import org.example.routeapp.model.Transportation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Dto for route related responses
public class RouteListResponseDto {

    Map<Long, List<RouteDto>> transportationsPerFlight = new HashMap<>();

    public RouteListResponseDto(){}

    public RouteListResponseDto(Map<Long, List<RouteDto>> transportationsPerFlight) {
        this.transportationsPerFlight.putAll(transportationsPerFlight);
    }

    public  Map<Long, List<RouteDto>> getTransportationsPerFlight() {
        return transportationsPerFlight;
    }

    public void setTransportationsPerFlight(Map<Long, List<RouteDto>> transportationsPerFlight) {
        this.transportationsPerFlight.putAll(transportationsPerFlight);
    }

}
