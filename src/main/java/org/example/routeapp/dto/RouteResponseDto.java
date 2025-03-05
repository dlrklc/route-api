package org.example.routeapp.dto;

import org.example.routeapp.model.Location;
import org.example.routeapp.model.TransportationType;

import java.util.List;

//Dto for route related responses
public class RouteResponseDto {

    List<List<Location>> routes;
    List<List<TransportationType>> transportationTypes;

    public RouteResponseDto(){}

    public RouteResponseDto(List<List<Location>> routes, List<List<TransportationType>> transportationTypes) {
        this.routes = routes;
        this.transportationTypes = transportationTypes;
    }

    public List<List<Location>> getRoutes() {
        return routes;
    }

    public void setRoutes(List<List<Location>> routes) {
        this.routes = routes;
    }

    public List<List<TransportationType>> getTransportationTypes() {
        return transportationTypes;
    }

    public void setTransportationTypes(List<List<TransportationType>> transportationTypes) {
        this.transportationTypes = transportationTypes;
    }
}
