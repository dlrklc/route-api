package org.example.routeapp.exceptions;

//exception class for location code related exceptions
public class DuplicateLocationCodeException extends RuntimeException {
    public DuplicateLocationCodeException(String locationCode) {
        super("Location code " + locationCode + " already exists.");
    }
}
