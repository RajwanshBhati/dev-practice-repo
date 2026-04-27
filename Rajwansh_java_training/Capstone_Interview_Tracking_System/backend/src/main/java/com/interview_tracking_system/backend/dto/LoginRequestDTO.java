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
     * All-args constructor.
     *
     * @param email    user email
     * @param password user password
     */
    public LoginRequestDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    /**
     * Gets the email.
     *
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets the password.
     *
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set email
     *
     * @param email email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Set password
     *
     * @param password password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
