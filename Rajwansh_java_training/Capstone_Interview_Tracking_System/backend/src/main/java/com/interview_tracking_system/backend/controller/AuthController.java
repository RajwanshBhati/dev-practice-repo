package com.interview_tracking_system.backend.controller;

import com.interview_tracking_system.backend.constants.ApiEndpoints;
import com.interview_tracking_system.backend.dto.ChangePasswordRequestDTO;
import com.interview_tracking_system.backend.dto.LoginRequestDTO;
import com.interview_tracking_system.backend.dto.RefreshTokenRequestDTO;
import com.interview_tracking_system.backend.dto.ApiResponse;
import com.interview_tracking_system.backend.dto.LoginResponseDTO;
import com.interview_tracking_system.backend.service.AuthService;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for authentication endpoints.
 */
@RestController
@RequestMapping(ApiEndpoints.BASE_AUTH)
public class AuthController {

        // Logger for debugging and monitoring
        private static final Logger log = LoggerFactory.getLogger(AuthController.class);

        /**
         * AuthService is injected via constructor
         */
        private final AuthService authService;

        /**
         * Constructor injection of AuthService
         */
        public AuthController(AuthService authService) {
                this.authService = authService;
        }

        /**
         * Login endpoint
         */
        @PostMapping(ApiEndpoints.LOGIN)
        public ResponseEntity<ApiResponse<LoginResponseDTO>> login(
                        @Valid @RequestBody LoginRequestDTO loginRequestDTO) {

                log.info("Login attempt received for user: {}", loginRequestDTO.getEmail());

                LoginResponseDTO response = authService.login(loginRequestDTO);

                log.info("Login successful for user: {}", loginRequestDTO.getEmail());

                return ResponseEntity.ok(
                                ApiResponse.success("Login successful", response));
        }

        /**
         * Refresh token endpoint
         */
        @PostMapping(ApiEndpoints.REFRESH)
        public ResponseEntity<ApiResponse<LoginResponseDTO>> refresh(
                        @Valid @RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {

                log.info("Refresh token request received");

                LoginResponseDTO response = authService.refreshToken(refreshTokenRequestDTO);

                log.info("Token refreshed successfully");

                return ResponseEntity.ok(
                                ApiResponse.success("Token refreshed", response));
        }

        /**
         * Logout endpoint
         */
        @PostMapping(ApiEndpoints.LOGOUT)
        public ResponseEntity<ApiResponse<Void>> logout(
                        @AuthenticationPrincipal UserDetails userDetails) {

                String username = userDetails.getUsername();

                log.info("Logout request received for user: {}", username);

                authService.logout(username);

                log.info("Logout successful for user: {}", username);

                return ResponseEntity.ok(
                                ApiResponse.success("Logged out successfully", null));
        }

        /**
         * Activate account via token and set password
         */
        @PostMapping(ApiEndpoints.ACTIVATE)
        public ResponseEntity<ApiResponse<Void>> activate(
                        @Valid @RequestBody ChangePasswordRequestDTO changePasswordRequestDTO) {

                log.info("Account activation request received");

                authService.setPasswordViaActivationToken(changePasswordRequestDTO);

                log.info("Account activated successfully");

                return ResponseEntity.ok(
                                ApiResponse.success(
                                                "Account activated successfully. You can now login.",
                                                null));
        }
}
