package com.interview_tracking_system.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import com.interview_tracking_system.backend.constants.ValidationMessages;

/**
 * Request body for setting password via activation token.
 * Used by panel members during onboarding.
 */
public class ChangePasswordRequestDTO {

    /** Minimum password length. */
    private static final int MIN_PASSWORD_LENGTH = 8;
    /**
     * Activation token received via email.
     */
    @NotBlank(message = ValidationMessages.TOKEN_REQUIRED)
    private String token;

    /**
     * New password — minimum 8 characters.
     */
    @NotBlank(message = ValidationMessages.NEW_PASSWORD_REQUIRED)
    @Size(min = MIN_PASSWORD_LENGTH, message = ValidationMessages.PASSWORD_MIN_LENGTH)
    private String newPassword;

    /**
     * Must match newPassword — validated in service.
     */
    @NotBlank(message = ValidationMessages.CONFIRM_PASSWORD_REQUIRED)
    private String confirmPassword;

    /**
     * Default constructor.
     */
    public ChangePasswordRequestDTO() {
    }

    /**
     * Creates request with all fields.
     *
     * @param token           activation token
     * @param newPassword     new password
     * @param confirmPassword confirm password
     */
    public ChangePasswordRequestDTO(
            final String token,
            final String newPassword,
            final String confirmPassword) {
        this.token = token;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }

    /**
     * Returns the token.
     *
     * @return token
     */
    public String getToken() {
        return token;
    }

    /**
     * Returns the new password.
     *
     * @return new password
     */
    public String getNewPassword() {
        return newPassword;
    }

    /**
     * Returns the confirm password.
     *
     * @return confirm password
     */
    public String getConfirmPassword() {
        return confirmPassword;
    }

    /**
     * Sets the token.
     *
     * @param requestToken token value
     */
    public void setToken(final String requestToken) {
        this.token = requestToken;
    }

    /**
     * Sets the new password.
     *
     * @param password new password
     */
    public void setNewPassword(final String password) {
        this.newPassword = password;
    }

    /**
     * Sets the confirm password.
     *
     * @param confirmPwd confirm password
     */
    public void setConfirmPassword(final String confirmPwd) {
        this.confirmPassword = confirmPwd;
    }
}
