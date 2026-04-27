package com.interview_tracking_system.backend.dto;

import com.interview_tracking_system.backend.enums.Role;

/**
 * Response body after successful login.
 * Returns JWT access token, refresh token, and basic user info.
 */
public class LoginResponseDTO {

    /**
     * Short-lived JWT access token.
     * Sent in Authorization header as Bearer token for all protected APIs.
     */
    private String accessToken;

    /**
     * Long-lived refresh token.
     * Used to get a new access token when it expires.
     */
    private String refreshToken;

    /**
     * Token type — always "Bearer".
     */
    private String tokenType = "Bearer";

    /**
     * Logged-in user's name.
     */
    private String name;

    /**
     * Logged-in user's email.
     */
    private String email;

    /**
     * Role of the logged-in user.
     * Frontend uses this to show/hide role-specific UI.
     */
    private Role role;

    /**
     * Default constructor.
     */
    public LoginResponseDTO() {
    }

    /**
     * All args constructor.
     */
    public LoginResponseDTO(String accessToken, String refreshToken,
            String tokenType, String name,
            String email, Role role) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    /**
     * Gets the access token.
     * 
     * @return accessToken
     */

    public String getAccessToken() {
        return accessToken;
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
     * Gets the token type.
     * 
     * @return tokenType
     */
    public String getTokenType() {
        return tokenType;
    }

    /**
     * Gets the name of the user.
     * 
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the email of the user.
     * 
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets the role of the user.
     * 
     * @return role
     */
    public Role getRole() {
        return role;
    }

    /**
     * Set access token
     * 
     * @param accessToken
     */

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * Set refresh token
     * 
     * @param refreshToken
     */
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    /**
     * Set token type
     * 
     * @param tokenType
     */
    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    /**
     * Set name
     * 
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set email
     * 
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Set role
     * 
     * @param role
     */
    public void setRole(Role role) {
        this.role = role;
    }

    /**
     * Builder for LoginResponseDTO to simplify object creation.
     */

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder class for LoginResponseDTO.
     * Allows chaining of setter methods for cleaner code.
     */
    public static class Builder {
        /* Required fields for login response */
        private String accessToken;
        /* Optional fields with default values */
        private String refreshToken;
        /* Token type is always "Bearer" for this application */
        private String tokenType = "Bearer";
        /* User info to return on login */
        private String name;
        /* User's email to return on login */
        private String email;
        /* User's role to return on login */
        private Role role;

        /**
         * Builder methods for each field. Returns the builder for chaining.
         */
        public Builder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        /**
         * Builder method for refresh token.
         * 
         * @param refreshToken the refresh token to set
         * @return the builder instance
         */
        public Builder refreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        /**
         * Builder method for token type.
         * 
         * @param tokenType the token type to set
         * @return the builder instance
         */
        public Builder tokenType(String tokenType) {
            this.tokenType = tokenType;
            return this;
        }

        /**
         * Builder method for user name.
         * 
         * @param name the name to set
         * @return the builder instance
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * Builder method for user email.
         * 
         * @param email the email to set
         * @return the builder instance
         */
        public Builder email(String email) {
            this.email = email;
            return this;
        }

        /**
         * Builder method for user role.
         * 
         * @param role the role to set
         * @return the builder instance
         */
        public Builder role(Role role) {
            this.role = role;
            return this;
        }

        /**
         * Builds the LoginResponseDTO object with the set fields.
         * 
         * @return a new LoginResponseDTO instance
         */
        public LoginResponseDTO build() {
            return new LoginResponseDTO(
                    accessToken,
                    refreshToken,
                    tokenType,
                    name,
                    email,
                    role);
        }
    }
}
