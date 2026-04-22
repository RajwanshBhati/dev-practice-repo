package com.interview_tracking_system.backend.controller;

import com.interview_tracking_system.backend.constants.ApiEndpoints;
import com.interview_tracking_system.backend.dto.ChangePasswordRequestDTO;
import com.interview_tracking_system.backend.dto.LoginRequestDTO;
import com.interview_tracking_system.backend.dto.RefreshTokenRequestDTO;
import com.interview_tracking_system.backend.dto.ApiResponse;
import com.interview_tracking_system.backend.dto.LoginResponseDTO;
import com.interview_tracking_system.backend.service.AuthService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for authentication endpoints.
 */
@RestController
@RequestMapping(ApiEndpoints.BASE_AUTH)
public class AuthController {

    /**
     * AuthService is injected via constructor
     */
    private final AuthService authService;

    /**
     * Constructor injection of AuthService
     *
     * @param authService
     */
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Login endpoint
     *
     * @param request
     * @return
     */
    @PostMapping(ApiEndpoints.LOGIN)
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(
            @Valid @RequestBody LoginRequestDTO loginRequestDTO) {

        LoginResponseDTO response = authService.login(loginRequestDTO);

        return ResponseEntity.ok(
                ApiResponse.success("Login successful", response));
    }

    /**
     * Refresh token endpoint
     *
     * @param refreshTokenRequestDTO
     * @return
     */
    @PostMapping(ApiEndpoints.REFRESH)
    public ResponseEntity<ApiResponse<LoginResponseDTO>> refresh(
            @Valid @RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {

        LoginResponseDTO response = authService.refreshToken(refreshTokenRequestDTO);

        return ResponseEntity.ok(
                ApiResponse.success("Token refreshed", response));
    }

    /**
     * Logout endpoint
     *
     * @param userDetails
     * @return
     */
    @PostMapping(ApiEndpoints.LOGOUT)
    public ResponseEntity<ApiResponse<Void>> logout(
            @AuthenticationPrincipal UserDetails userDetails) {

        authService.logout(userDetails.getUsername());

        return ResponseEntity.ok(
                ApiResponse.success("Logged out successfully", null));
    }

    /**
     * Activate account via token and set password
     *
     * @param changePasswordRequestDTO
     * @return
     */
    @PostMapping(ApiEndpoints.ACTIVATE)
    public ResponseEntity<ApiResponse<Void>> activate(
            @Valid @RequestBody ChangePasswordRequestDTO changePasswordRequestDTO) {

        authService.setPasswordViaActivationToken(changePasswordRequestDTO);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Account activated successfully. You can now login.",
                        null));
    }
}
