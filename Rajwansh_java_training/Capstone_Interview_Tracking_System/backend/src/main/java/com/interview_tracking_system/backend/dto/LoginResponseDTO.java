package com.interview_tracking_system.backend.dto;

import com.interview_tracking_system.backend.enums.Role;

/**
 * Response body after successful login.
 * Returns JWT access token, refresh token, and user details.
 */
public class LoginResponseDTO {

    /** JWT access token. */
    private String accessToken;

    /** Refresh token. */
    private String refreshToken;

    /** Token type — always Bearer. */
    private String tokenType = "Bearer";

    /** Logged-in user's name. */
    private String name;

    /** Logged-in user's email. */
    private String email;

    /** Logged-in user's role. */
    private Role role;

    /**
     * Default constructor.
     */
    public LoginResponseDTO() {
    }

    /**
     * Creates login response DTO.
     *
     * @param accessToken  access token
     * @param refreshToken refresh token
     * @param tokenType    token type
     * @param name         user name
     * @param email        user email
     * @param role         user role
     */
    public LoginResponseDTO(
            final String accessToken,
            final String refreshToken,
            final String tokenType,
            final String name,
            final String email,
            final Role role) {

        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    /** @return access token */
    public String getAccessToken() {
        return accessToken;
    }

    /** @return refresh token */
    public String getRefreshToken() {
        return refreshToken;
    }

    /** @return token type */
    public String getTokenType() {
        return tokenType;
    }

    /** @return user name */
    public String getName() {
        return name;
    }

    /** @return email */
    public String getEmail() {
        return email;
    }

    /** @return role */
    public Role getRole() {
        return role;
    }

    /**
     * Sets access token.
     *
     * @param token access token
     */
    public void setAccessToken(final String token) {
        this.accessToken = token;
    }

    /**
     * Sets refresh token.
     *
     * @param token refresh token
     */
    public void setRefreshToken(final String token) {
        this.refreshToken = token;
    }

    /**
     * Sets token type.
     *
     * @param type token type
     */
    public void setTokenType(final String type) {
        this.tokenType = type;
    }

    /**
     * Sets user name.
     *
     * @param userName name
     */
    public void setName(final String userName) {
        this.name = userName;
    }

    /**
     * Sets email.
     *
     * @param userEmail email
     */
    public void setEmail(final String userEmail) {
        this.email = userEmail;
    }

    /**
     * Sets role.
     *
     * @param userRole role
     */
    public void setRole(final Role userRole) {
        this.role = userRole;
    }

    /**
     * Returns builder instance.
     *
     * @return builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder class for LoginResponseDTO.
     */
    public static final class Builder {

        /** JWT access token. */
        private String accessToken;

        /** Refresh token. */
        private String refreshToken;

        /** Token type — always Bearer. */
        private String tokenType = "Bearer";

        /** Logged-in user's name. */
        private String name;

        /** Logged-in user's email. */
        private String email;

        /** Logged-in user's role. */
        private Role role;

        /**
         * Sets access token.
         *
         * @param token access token
         * @return this builder instance
         */
        public Builder accessToken(final String token) {
            this.accessToken = token;
            return this;
        }

        /**
         * Sets refresh token.
         *
         * @param token refresh token
         * @return this builder instance
         */
        public Builder refreshToken(final String token) {
            this.refreshToken = token;
            return this;
        }

        /**
         * Sets token type.
         *
         * @param type token type
         * @return this builder instance
         */
        public Builder tokenType(final String type) {
            this.tokenType = type;
            return this;
        }

        /**
         * Sets user name.
         *
         * @param userName user name
         * @return this builder instance
         */
        public Builder name(final String userName) {
            this.name = userName;
            return this;
        }

        /**
         * Sets user email.
         *
         * @param userEmail user email
         * @return this builder instance
         */
        public Builder email(final String userEmail) {
            this.email = userEmail;
            return this;
        }

        /**
         * Sets user role.
         *
         * @param userRole user role
         * @return this builder instance
         */
        public Builder role(final Role userRole) {
            this.role = userRole;
            return this;
        }

        /**
         * Builds LoginResponseDTO.
         *
         * @return LoginResponseDTO
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
