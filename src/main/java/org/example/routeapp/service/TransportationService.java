package org.example.routeapp.service;

import org.example.routeapp.dao.LocationDao;
import org.example.routeapp.dao.TransportationDao;
import org.example.routeapp.dto.IdResponseDto;
import org.example.routeapp.dto.RouteDto;
import org.example.routeapp.dto.RouteListResponseDto;
import org.example.routeapp.dto.TransportationDto;
import org.example.routeapp.exceptions.LocationNotFoundException;
import org.example.routeapp.model.Graph;
import org.example.routeapp.model.Location;
import org.example.routeapp.model.Transportation;
import org.example.routeapp.model.TransportationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
@Transactional
public class TransportationService {

    private static final Integer MAX_NUM_OF_TRANSPORTATIONS = 3;
    private static final Integer MAX_NUM_OF_FOUND_LOCATIONS_BY_ID = 2;

    private final TransportationDao transportationDao;
    private final LocationService locationService;
    private final LocationDao locationDao;

    @Autowired
    public TransportationService(TransportationDao transportationDao, LocationService locationService, LocationDao locationDao) {
        this.transportationDao = transportationDao;
        this.locationService = locationService;
        this.locationDao = locationDao;
    }

    public ResponseEntity<List<Transportation>> getAllTransportations() {

        try {
            return new ResponseEntity<>(transportationDao.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Transportation> getByTransportationId(Long transportationId) {
        if (transportationId == null || transportationId < 1) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Optional<Transportation> transportation = transportationDao.findById(transportationId);

        if (transportation.isPresent()) {
            return new ResponseEntity<>(transportation.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<RouteListResponseDto> getTransportationsBetween(Long originId,
                                                                          Long destId,
                                                                          String date) {

        if (originId == null || destId == null || date == null || originId.equals(destId)
                || originId < 1 || destId < 1) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Location originLocation;
        Location destLocation;

        try {
            originLocation = locationService.getByLocationId(originId);
            destLocation = locationService.getByLocationId(destId);
        } catch (LocationNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Set<Transportation> possibleTransportations = transportationDao.
                findByOriginLocation(originLocation);   //gets transportations from given origin

        Set<Transportation> tempTransportations = possibleTransportations;

        int index = 0;
        //loops length of path, maximum 3 transportation for our use case, reduced to 2 since first transportations are fetched
        while (index < MAX_NUM_OF_TRANSPORTATIONS - 1) {
            tempTransportations = getAllPossiblePathTransportation(tempTransportations);
            possibleTransportations.addAll(tempTransportations);
            index++;
        }

        //save it into graph
        Graph graph = new Graph();

        for (Transportation transportation : possibleTransportations) {
            graph.addVertex(transportation.getOriginLocation());
            graph.addVertex(transportation.getDestinationLocation());
            graph.addEdge(transportation);
        }

        List<List<TransportationType>> allTransportationTypes = new ArrayList<>();
        List<List<Transportation>> allRoutes = graph.findAllRoutes(originLocation, destLocation, date);  //finds routes

        graph.displayGraph();
        //print all found routes & transportation types
        System.out.println("All routes from " + originLocation + " to " + destLocation + ":");
        for (List<Transportation> route : allRoutes) {
            System.out.println(route);
        }
        for (List<TransportationType> type : allTransportationTypes) {
            System.out.println(type);
        }


        Map<Long, List<RouteDto>> transportationsPerFlight = new HashMap<>();
        List<Transportation> transportationsType = new ArrayList<>();
        Long transportationId = 0L;
        int flightCounter = 0;

        List<RouteDto> RouteListDto = new ArrayList<>();

        for (List<Transportation> route : allRoutes) {
            RouteDto routeDto = new RouteDto();
            for (int i = 0; i < route.size(); i++) {
                if (route.get(i).getTransportationType().equals(TransportationType.FLIGHT)) {
                    routeDto.setFlight(new TransportationDto(route.get(i).getOriginLocation().getName(), route.get(i).getDestinationLocation().getName(),
                            Arrays.asList(route.get(i).getTransportationType())));
                    transportationId = route.get(i).getTransportationId();
                } else if (routeDto.getFlight() == null) {
                    routeDto.setBeforeFlight(new TransportationDto(route.get(i).getOriginLocation().getName(), route.get(i).getDestinationLocation().getName(),
                            Arrays.asList(route.get(i).getTransportationType())));
                } else {
                    routeDto.setAfterFlight(new TransportationDto(route.get(i).getOriginLocation().getName(), route.get(i).getDestinationLocation().getName(),
                            Arrays.asList(route.get(i).getTransportationType())));
                }

            }
            if (!transportationsPerFlight.containsKey(transportationId)) {
                transportationsPerFlight.put(transportationId, new ArrayList<>());
            }

            boolean isAdd = mergeTransportationData(transportationsPerFlight, routeDto, transportationId);

            if (isAdd) {
                transportationsPerFlight.get(transportationId).add(routeDto);
            }
        }

        System.out.println("transportationsPerFlight" + transportationsPerFlight);

        //= allRoutes.stream()
        //.collect(groupingBy(Transportation::getTransportationId, toList()));

        /*TransportationDto transportationDto = new TransportationDto(t.getOriginLocation().getName()
                ,t.getDestinationLocation().getName(), Arrays.asList());
        routeDto.setBeforeFlight(transportationDto);*/


        RouteListResponseDto res = new RouteListResponseDto(transportationsPerFlight);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    public ResponseEntity<IdResponseDto> addTransportation(Transportation transportation) {

        Long originLocationId = transportation.getOriginLocation().getLocationId();
        Long destLocationId = transportation.getDestinationLocation().getLocationId();

        if (originLocationId == null
                || destLocationId == null
                || transportation.getTransportationType() == null
                || originLocationId < 1
                || destLocationId < 1) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<Long> ids = new ArrayList<>();
        ids.add(originLocationId);
        ids.add(destLocationId);

        if (originLocationId.equals(destLocationId)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        if (locationDao.findByLocationIds(ids).size() < MAX_NUM_OF_FOUND_LOCATIONS_BY_ID) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Transportation created;
        try {
            created = transportationDao.save(transportation);
        } catch (Exception e) {
            return new ResponseEntity<>(new IdResponseDto(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        IdResponseDto res = new IdResponseDto(created.getTransportationId());

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    public ResponseEntity<Void> updateTransportation(Transportation transportation) {
        Long originLocationId = transportation.getOriginLocation().getLocationId();
        Long destLocationId = transportation.getDestinationLocation().getLocationId();

        if (transportation.getTransportationId() == null || originLocationId == null
                || destLocationId == null
                || transportation.getTransportationType() == null
                || originLocationId < 1
                || destLocationId < 1
                || transportation.getTransportationId() < 1) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (originLocationId.equals(destLocationId)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        //list to make only one request for fetching locations
        List<Long> ids = new ArrayList<>();
        ids.add(transportation.getOriginLocation().getLocationId());
        ids.add(transportation.getDestinationLocation().getLocationId());

        try {
            if (locationDao.findByLocationIds(ids).size() < MAX_NUM_OF_FOUND_LOCATIONS_BY_ID ||
                    !(transportationDao.findById(transportation.getTransportationId()).isPresent())) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            transportationDao.save(transportation);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<Void> deleteTransportation(Long transportationId) {
        if (transportationId == null || transportationId < 1) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (!transportationDao.findById(transportationId).isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        try {
            transportationDao.deleteById(transportationId);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //fetches possible transportations which may be in the route
    private Set<Transportation> getAllPossiblePathTransportation(Set<Transportation> transportations) {
        Set<Transportation> tempList;

        Set<Transportation> possiblePathTransportations = new HashSet<>();

        for (Transportation transportation : transportations) {
            tempList = transportationDao
                    .findByOriginLocationNotDestLocation(transportation.getDestinationLocation().getLocationId(),
                            transportation.getOriginLocation().getLocationId());

            possiblePathTransportations.addAll(tempList);
        }
        return possiblePathTransportations;
    }

    private boolean mergeTransportationData(Map<Long, List<RouteDto>> transportationsPerFlight, RouteDto routeDtoIn, Long flightId) {
        for (RouteDto routeDto : transportationsPerFlight.get(flightId)) {

            if (routeDto.getBeforeFlight() != null && routeDtoIn.getBeforeFlight()!= null &&
                    routeDto.getBeforeFlight().getOriginName().equals(routeDtoIn.getBeforeFlight().getOriginName())
                    && routeDto.getBeforeFlight().getDestinationName().equals(routeDtoIn.getBeforeFlight().getDestinationName())) {
                routeDto.getBeforeFlight().addTransportationType(routeDtoIn.getBeforeFlight().getTransportationType());
                routeDtoIn.getBeforeFlight().addTransportationType(routeDto.getBeforeFlight().getTransportationType());
            }

            if (routeDto.getAfterFlight() != null && routeDtoIn.getAfterFlight()!= null &&
                    routeDto.getAfterFlight().getOriginName().equals(routeDtoIn.getAfterFlight().getOriginName())
                    && routeDto.getAfterFlight().getDestinationName().equals(routeDtoIn.getAfterFlight().getDestinationName())) {
                routeDto.getAfterFlight().addTransportationType(routeDtoIn.getAfterFlight().getTransportationType());
                routeDtoIn.getAfterFlight().addTransportationType(routeDto.getAfterFlight().getTransportationType());
            }
            if (routeDto.equals(routeDtoIn)) {
                return false;
            }


        }
        return true;
    }
}
