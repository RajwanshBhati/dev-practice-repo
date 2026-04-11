package com.training.session2.exception;

public class InvalidMessageTypeException extends RuntimeException {

    private final String invalidType;

    public InvalidMessageTypeException(String invalidType) {
        super(String.format("Unsupported message type: '%s'. Accepted values: SHORT, LONG", invalidType));
        this.invalidType = invalidType;
    }

    public String getInvalidType() { return invalidType; }
}
