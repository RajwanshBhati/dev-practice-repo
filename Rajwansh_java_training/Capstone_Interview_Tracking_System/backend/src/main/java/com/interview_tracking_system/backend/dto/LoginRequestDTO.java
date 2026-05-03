package com.interview_tracking_system.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Request body for login API.
 */
public class LoginRequestDTO {

    /**
     * Registered email address — used as username.
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    /**
     * Plain text password — matched against BCrypt hash in DB.
     */
    @NotBlank(message = "Password is required")
    private String password;

    /**
     * Default constructor.
     */
    public LoginRequestDTO() {
    }

    /**
     * Creates login request with email and password.
     *
     * @param userEmail    user email
     * @param userPassword user password
     */
    public LoginRequestDTO(final String userEmail, final String userPassword) {
        this.email = userEmail;
        this.password = userPassword;
    }

    /**
     * Returns the email.
     *
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns the password.
     *
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the email.
     *
     * @param requestEmail email
     */
    public void setEmail(final String requestEmail) {
        this.email = requestEmail;
    }

    /**
     * Sets the password.
     *
     * @param requestPassword password
     */
    public void setPassword(final String requestPassword) {
        this.password = requestPassword;
    }
}
