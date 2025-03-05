package org.example.routeapp.exceptions;

//exception class when location with given id does not exist related exceptions
public class LocationNotFoundException extends RuntimeException {
    public LocationNotFoundException(Long id) {
        super("Location id " + id + " not found.");
    }
}
