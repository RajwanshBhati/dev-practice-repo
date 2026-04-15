package com.springrest.restapi.exception;


// Here I extend RuntimeException to create a custom exception is InvalidRequestException. This exception can be thrown when the application encounters an invalid request, such as missing required parameters or invalid input data. By creating this custom exception
public class InvalidRequestException extends RuntimeException {
 
    public InvalidRequestException(String message) {
        super(message);
    }
}
 