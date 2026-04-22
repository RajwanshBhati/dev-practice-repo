package com.interview_tracking_system.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request body for setting password via activation token.
 * Used by panel members during onboarding.
 */
public class ChangePasswordRequestDTO {

    /**
     * Activation token received via email.
     */
    @NotBlank(message = "Token is required")
    private String token;

    /**
     * New password — minimum 8 characters.
     */
    @NotBlank(message = "New password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String newPassword;

    /**
     * Must match newPassword — validated in service.
     */
    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;

    /**
     * Default constructor.
     */
    public ChangePasswordRequestDTO() {
    }

    /**
     * All-args constructor.
     *
     * @param token           the activation token
     * @param newPassword     the new password
     * @param confirmPassword the confirmation of the new password
     */
    public ChangePasswordRequestDTO(String token, String newPassword, String confirmPassword) {
        this.token = token;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }

    /**
     * Gets the token.
     * 
     * @return token
     */
    public String getToken() {
        return token;
    }

    /**
     * Gets the new password.
     * 
     * @return newPassword
     */
    public String getNewPassword() {
        return newPassword;
    }

    /**
     * Gets the confirm password.
     * 
     * @return confirmPassword
     */
    public String getConfirmPassword() {
        return confirmPassword;
    }

    /**
     * Set token
     *
     * @param token the token to set
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Set new password
     *
     * @param newPassword the new password to set
     */
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    /**
     * Set confirm password
     *
     * @param confirmPassword the confirm password to set
     */
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
