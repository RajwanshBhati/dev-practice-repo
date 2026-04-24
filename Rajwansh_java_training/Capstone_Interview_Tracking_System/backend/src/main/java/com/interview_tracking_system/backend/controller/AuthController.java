package com.interview_tracking_system.backend.controller;

import com.interview_tracking_system.backend.constants.ApiEndpoints;
import com.interview_tracking_system.backend.constants.LogMessages;
import com.interview_tracking_system.backend.constants.SuccessMessages;
import com.interview_tracking_system.backend.dto.ApiResponse;
import com.interview_tracking_system.backend.dto.ChangePasswordRequestDTO;
import com.interview_tracking_system.backend.dto.LoginRequestDTO;
import com.interview_tracking_system.backend.service.AuthService;
import com.interview_tracking_system.backend.dto.LoginResponseDTO;
import com.interview_tracking_system.backend.dto.RefreshTokenRequestDTO;

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

        private static final Logger log = LoggerFactory.getLogger(AuthController.class);

        private final AuthService authService;

        public AuthController(AuthService authService) {
                this.authService = authService;
        }

        @PostMapping(ApiEndpoints.LOGIN)
        public ResponseEntity<ApiResponse<LoginResponseDTO>> login(
                        @Valid @RequestBody LoginRequestDTO loginRequestDTO) {

                log.info(LogMessages.LOGIN_ATTEMPT, loginRequestDTO.getEmail());

                LoginResponseDTO response = authService.login(loginRequestDTO);

                log.info(LogMessages.LOGIN_SUCCESS, loginRequestDTO.getEmail());

                return ResponseEntity.ok(
                                ApiResponse.success(SuccessMessages.LOGIN_SUCCESS, response));
        }

        @PostMapping(ApiEndpoints.REFRESH)
        public ResponseEntity<ApiResponse<LoginResponseDTO>> refresh(
                        @Valid @RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {

                log.info(LogMessages.REFRESH_REQUEST);

                LoginResponseDTO response = authService.refreshToken(refreshTokenRequestDTO);

                log.info(LogMessages.REFRESH_SUCCESS);

                return ResponseEntity.ok(
                                ApiResponse.success(SuccessMessages.TOKEN_REFRESHED, response));
        }

        @PostMapping(ApiEndpoints.LOGOUT)
        public ResponseEntity<ApiResponse<Void>> logout(
                        @AuthenticationPrincipal UserDetails userDetails) {

                String username = userDetails.getUsername();

                log.info(LogMessages.LOGOUT_REQUEST, username);

                authService.logout(username);

                log.info(LogMessages.LOGOUT_SUCCESS, username);

                return ResponseEntity.ok(
                                ApiResponse.success(SuccessMessages.LOGOUT_SUCCESS, null));
        }

        @PostMapping(ApiEndpoints.ACTIVATE)
        public ResponseEntity<ApiResponse<Void>> activate(
                        @Valid @RequestBody ChangePasswordRequestDTO changePasswordRequestDTO) {

                log.info(LogMessages.ACTIVATION_REQUEST);

                authService.setPasswordViaActivationToken(changePasswordRequestDTO);

                log.info(LogMessages.ACTIVATION_SUCCESS);

                return ResponseEntity.ok(
                                ApiResponse.success(SuccessMessages.ACCOUNT_ACTIVATED, null));
        }
}
