package com.interview_tracking_system.backend.dto;

import com.interview_tracking_system.backend.constants.ValidationMessages;

import jakarta.validation.constraints.NotBlank;

/**
 * Request body to refresh an expired access token.
 */
public class RefreshTokenRequestDTO {

    /**
     * Valid refresh token previously issued on login.
     */
    @NotBlank(message = ValidationMessages.REFRESH_TOKEN_REQUIRED)
    private String refreshToken;

    /**
     * Default constructor.
     */
    public RefreshTokenRequestDTO() {
    }

    /**
     * Creates refresh token request.
     *
     * @param token refresh token
     */
    public RefreshTokenRequestDTO(final String token) {
        this.refreshToken = token;
    }

    /**
     * Returns the refresh token.
     *
     * @return refresh token
     */
    public String getRefreshToken() {
        return refreshToken;
    }

    /**
     * Sets the refresh token.
     *
     * @param token refresh token
     */
    public void setRefreshToken(final String token) {
        this.refreshToken = token;
    }
}
