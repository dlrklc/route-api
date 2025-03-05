package org.example.routeapp.dao;

import org.example.routeapp.model.Location;
import org.example.routeapp.model.Transportation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.Set;

@Repository
public interface TransportationDao extends JpaRepository<Transportation, Long> {

    Set<Transportation> findByOriginLocation(Location originLocation);

    /*gets origin location with given origin id & destination location should not be the
                location which has given dest id*/
    @Query(value = "SELECT * FROM transportations t WHERE t.origin_location_id = :originLocation " +
            "AND t.destination_location_id != :destinationLocation",nativeQuery = true)
    Set<Transportation> findByOriginLocationNotDestLocation(Long originLocation,
                                                             Long destinationLocation);

}
