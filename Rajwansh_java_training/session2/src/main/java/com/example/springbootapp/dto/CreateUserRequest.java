package com.example.springbootapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

// I am creating a DTO (Data Transfer Object) class is CreateUserRequest that will be used to capture the data needed to create a new user. This class has three fields: name, email, and role. Each field is annotated with validation constraints to ensure that the input data is valid when creating a user. The class includes getter and setter methods for each field, as well as a default constructor.
public class CreateUserRequest {
    

    // @notBlank ensures that the name field is not null or empty, and provides a custom error message if the validation fails.
    @NotBlank(message = "Name must not be blank")
    private String name;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email must be a valid format")
    private String email;

    @NotBlank(message = "Role must not be blank")
    private String role;

    public CreateUserRequest() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
