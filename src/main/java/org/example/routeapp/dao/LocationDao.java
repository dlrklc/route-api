package org.example.routeapp.dao;

import org.example.routeapp.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationDao extends JpaRepository<Location, Long> {
    boolean existsByLocationCode(String locationCode);

    boolean existsByName(String name);

    //get locations for given ids
    @Query(value="select * from locations l where l.location_id in :ids" , nativeQuery = true)
    List<Long> findByLocationIds(List<Long> ids);

}
