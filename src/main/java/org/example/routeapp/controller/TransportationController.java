package org.example.routeapp.controller;

import org.example.routeapp.dto.IdResponseDto;
import org.example.routeapp.dto.RouteResponseDto;
import org.example.routeapp.model.Transportation;
import org.example.routeapp.service.TransportationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

//Controller for Transportation CRUD operations & find route

@CrossOrigin
@RestController
@RequestMapping("transportations")
public class TransportationController {

    private final TransportationService transportationService;

    @Autowired
    public TransportationController(TransportationService transportationService) {
        this.transportationService = transportationService;
    }

    @GetMapping()
    public ResponseEntity<List<Transportation>> getAllTransportations(){
        return transportationService.getAllTransportations();
    }

    @GetMapping("/{transportationId}")
    public ResponseEntity<Transportation> getByTransportationId(@PathVariable Long transportationId){
        return transportationService.getByTransportationId(transportationId);
    }

    @GetMapping("/origin/{originId}/dest/{destId}/date/{date}")
    public ResponseEntity<RouteResponseDto> getTransportationsBetween(@PathVariable Long originId,
                                                                      @PathVariable Long destId,
                                                                      @PathVariable String date){
        return transportationService.getTransportationsBetween(originId, destId, date);
    }

    @PostMapping()
    public ResponseEntity<IdResponseDto> addTransportation(@RequestBody Transportation transportation){
        return transportationService.addTransportation(transportation);
    }

    @PutMapping()
    public ResponseEntity<Void> updateTransportation(@RequestBody Transportation transportation) {
        return transportationService.updateTransportation(transportation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransportation(@PathVariable Long id){
        return transportationService.deleteTransportation(id);
    }

}
