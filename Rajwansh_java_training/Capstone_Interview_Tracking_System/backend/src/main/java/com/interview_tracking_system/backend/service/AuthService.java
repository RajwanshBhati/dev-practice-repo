package com.interview_tracking_system.backend.service;

import com.interview_tracking_system.backend.dto.ChangePasswordRequestDTO;
import com.interview_tracking_system.backend.dto.LoginRequestDTO;
import com.interview_tracking_system.backend.dto.LoginResponseDTO;
import com.interview_tracking_system.backend.dto.RefreshTokenRequestDTO;

/**
 * Service interface for authentication operations.
 */
public interface AuthService {

    /**
     * Authenticate user and return access and refresh tokens.
     *
     * @param request the login credentials
     * @return the login response containing tokens
     */
    LoginResponseDTO login(LoginRequestDTO request);

    /**
     * Validate refresh token and return new access and refresh tokens.
     *
     * @param request the refresh token request
     * @return the new login response containing tokens
     */
    LoginResponseDTO refreshToken(RefreshTokenRequestDTO request);

    /**
     * Invalidate all refresh tokens for the user to effectively log them out.
     *
     * @param email the email of the user to log out
     */
    void logout(String email);

    /**
     * Change password for authenticated user via activation token.
     *
     * @param request the change password request details
     */
    void setPasswordViaActivationToken(ChangePasswordRequestDTO request);
}
