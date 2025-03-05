package org.example.routeapp.exceptions;

//exception class for location name related exceptions
public class DuplicateNameException extends RuntimeException {
    public DuplicateNameException(String name) {
        super("Location name " + name + " already exists.");
    }
}
