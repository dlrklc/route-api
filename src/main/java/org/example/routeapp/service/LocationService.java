package org.example.routeapp.service;

import org.example.routeapp.dao.LocationDao;
import org.example.routeapp.dto.IdResponseDto;
import org.example.routeapp.exceptions.DuplicateLocationCodeException;
import org.example.routeapp.exceptions.DuplicateNameException;
import org.example.routeapp.exceptions.LocationNotFoundException;
import org.example.routeapp.model.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LocationService {

    private final LocationDao locationDao;

    @Autowired
    public LocationService(LocationDao locationDao) {
        this.locationDao = locationDao;
    }

    public ResponseEntity<List<Location>> getAllLocations() {

        try {
            return new ResponseEntity<>(locationDao.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Location getByLocationId(Long locationId) {

        Optional<Location> location = locationDao.findById(locationId);

        if(!location.isPresent()) {
            throw new LocationNotFoundException(locationId);
        }

        return location.get();
    }

    public ResponseEntity<IdResponseDto> addLocation(Location location) {
        if (locationDao.existsByLocationCode(location.getLocationCode())) { //checks duplicate locaion code
            throw new DuplicateLocationCodeException(location.getLocationCode());
        }

        if (locationDao.existsByName((location.getName()))) {  //checks duplicate name
            throw new DuplicateNameException(location.getName());
        }
        Location created;
        try{
            created = locationDao.save(location);
        }
        catch(Exception e){
            return new ResponseEntity<>(new IdResponseDto(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        IdResponseDto res = new IdResponseDto(created.getLocationId());
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    public ResponseEntity<Void> updateLocation(Location location) {

        Long locationId = location.getLocationId();

        if(locationId == null || locationId < 1) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if(!locationDao.findById(location.getLocationId()).isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        try{
            locationDao.save(location);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<Void> deleteLocation(Long locationId) {
        if(locationId == null || locationId < 1) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(!locationDao.findById(locationId).isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        try{
            locationDao.deleteById(locationId);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
