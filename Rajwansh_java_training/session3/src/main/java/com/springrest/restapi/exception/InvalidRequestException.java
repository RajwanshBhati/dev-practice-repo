package com.springrest.restapi.exception;

public class InvalidRequestException extends RuntimeException {
 
    public InvalidRequestException(String message) {
        super(message);
    }
}
 