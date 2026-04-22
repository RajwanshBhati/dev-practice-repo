package com.interview_tracking_system.backend.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Request body to refresh an expired access token.
 */
public class RefreshTokenRequestDTO {

    /**
     * Valid refresh token previously issued on login.
     */
    @NotBlank(message = "Refresh token is required")
    private String refreshToken;

    /**
     * Default constructor.
     */
    public RefreshTokenRequestDTO() {

    }

    /**
     * All-args constructor.
     *
     * @param refreshToken the refresh token to set
     */
    public RefreshTokenRequestDTO(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    /**
     * Gets the refresh token.
     *
     * @return refreshToken
     */
    public String getRefreshToken() {
        return refreshToken;
    }

    /**
     * Set refresh token
     *
     * @param refreshToken the refresh token to set
     */
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
