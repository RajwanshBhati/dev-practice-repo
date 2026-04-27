package com.interview_tracking_system.backend.service;

import com.interview_tracking_system.backend.dto.ChangePasswordRequestDTO;
import com.interview_tracking_system.backend.dto.LoginRequestDTO;
import com.interview_tracking_system.backend.dto.LoginResponseDTO;
import com.interview_tracking_system.backend.dto.RefreshTokenRequestDTO;

public interface AuthService {

    /**
     * Authenticate user and return access + refresh tokens.
     * 
     * @param request
     * @return
     */
    LoginResponseDTO login(LoginRequestDTO request);

    /**
     * Validate refresh token and return new access + refresh tokens.
     * 
     * @param request
     * @return
     */

    LoginResponseDTO refreshToken(RefreshTokenRequestDTO request);

    /**
     * Invalidate all refresh tokens for the user to effectively log them out.
     * 
     * @param email
     */

    void logout(String email);

    /**
     * Change password for authenticated user.
     * 
     * @param email
     * @param request
     */

    void setPasswordViaActivationToken(ChangePasswordRequestDTO request);
}
