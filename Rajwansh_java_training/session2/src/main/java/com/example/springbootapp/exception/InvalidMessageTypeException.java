package com.example.springbootapp.exception;
// I am creating a custom exception class is InvalidMessageTypeException that extends RuntimeException. This exception is designed to be thrown when an unsupported message type is encountered in the application. 
public class InvalidMessageTypeException extends RuntimeException {

    private final String invalidType;

    public InvalidMessageTypeException(String invalidType) {
        super(String.format("Unsupported message type: '%s'. Accepted values: SHORT, LONG", invalidType));
        this.invalidType = invalidType;
    }

    public String getInvalidType() { return invalidType; }
}
