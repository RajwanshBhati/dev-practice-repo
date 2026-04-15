package com.springrest.restapi.exception;


// Here I extend RuntimeException to create a custom exception is UserNotFoundException. This exception can be thrown when the application attempts to retrieve or manipulate a user that does not exist 
public class UserNotFoundException extends RuntimeException {
 
    public UserNotFoundException(String message) {
        super(message);
    }
}