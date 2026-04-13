package com.example.springbootapp.exception;

// I am creating a custom exception class called ResourceNotFoundException that extends RuntimeException. This exception is created to be thrown when a requested resource (like a user) is not found in the repository.
public class ResourceNotFoundException extends RuntimeException {

    private final String resourceName;
    private final Object resourceId;
    

    // Here creating a constructor for the ResourceNotFoundException that takes the name of the resource and its identifier as parameters. The constructor calls the superclass constructor with a formatted error message that includes the resource name and ID, making it easier to understand what resource was not found when the exception is thrown.
    public ResourceNotFoundException(String resourceName, Object resourceId) {
        super(String.format("%s not found with id: %s", resourceName, resourceId));
        this.resourceName = resourceName;
        this.resourceId = resourceId;
    }

    public String getResourceName() { return resourceName; }
    public Object getResourceId() { return resourceId; }
}
