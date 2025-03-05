package org.example.routeapp.controller;

import jakarta.validation.Valid;
import org.example.routeapp.dto.IdResponseDto;
import org.example.routeapp.exceptions.DuplicateLocationCodeException;
import org.example.routeapp.exceptions.DuplicateNameException;
import org.example.routeapp.exceptions.LocationNotFoundException;
import org.example.routeapp.model.Location;
import org.example.routeapp.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Controller for Location CRUD operations
@CrossOrigin
@RestController
@RequestMapping("locations")
public class LocationController {

    private final LocationService locationService;

    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping()
    public ResponseEntity<List<Location>> getAllLocations(){
        return locationService.getAllLocations();
    }

    @GetMapping("/{locationId}")
    public ResponseEntity<Object> getByLocationId(@PathVariable Long locationId){

        if(locationId == null || locationId < 1) {
            return new ResponseEntity<>(new IdResponseDto("location id cannot be null or less than 1"),
                    HttpStatus.BAD_REQUEST);
        }

        try{
            return new ResponseEntity<>(locationService.getByLocationId(locationId), HttpStatus.OK);
        }
        catch(LocationNotFoundException locationNotFoundException){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(Exception e){
            return new ResponseEntity<>(new IdResponseDto(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping()
    public ResponseEntity<IdResponseDto> addLocation(@Valid @RequestBody Location location){
        try{
            return locationService.addLocation(location);
        } catch (DuplicateLocationCodeException | DuplicateNameException e) {
            return new ResponseEntity<>(new IdResponseDto(e.getMessage()), HttpStatus.CONFLICT);
        }
        catch(Exception e){
            return new ResponseEntity<>(new IdResponseDto(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("")
    public ResponseEntity<Void> updateLocation(@Valid @RequestBody Location location) {
        return locationService.updateLocation(location);
    }

    @DeleteMapping("/{locationId}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long locationId) {
        return locationService.deleteLocation(locationId);
    }

    //handle @Valid errors
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

}
