package com.interview_tracking_system.backend.controller;

import com.interview_tracking_system.backend.constants.ApiEndpoints;
import com.interview_tracking_system.backend.constants.LogMessages;
import com.interview_tracking_system.backend.constants.SuccessMessages;
import com.interview_tracking_system.backend.dto.ApiResponse;
import com.interview_tracking_system.backend.dto.ChangePasswordRequestDTO;
import com.interview_tracking_system.backend.dto.LoginRequestDTO;
import com.interview_tracking_system.backend.dto.LoginResponseDTO;
import com.interview_tracking_system.backend.dto.RefreshTokenRequestDTO;
import com.interview_tracking_system.backend.service.AuthService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
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
public final class AuthController {

        /**
         * Logger instance.
         */
        private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

        /**
         * Authentication service.
         */
        private final AuthService authService;

        /**
         * Creates an authentication controller.
         *
         * @param injectedAuthService the authentication service
         */
        @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "Spring dependency injection stores"
                        + " framework-managed beans.")
        public AuthController(final AuthService injectedAuthService) {
                this.authService = injectedAuthService;
        }

        /**
         * Authenticates a user and returns login tokens.
         *
         * @param loginRequestDTO the login request
         * @return the login response
         */
        @PostMapping(ApiEndpoints.LOGIN)
        public ResponseEntity<ApiResponse<LoginResponseDTO>> login(
                        @Valid @RequestBody final LoginRequestDTO loginRequestDTO) {

                LOGGER.info(LogMessages.LOGIN_ATTEMPT, loginRequestDTO.getEmail());

                LoginResponseDTO response = authService.login(loginRequestDTO);

                LOGGER.info(LogMessages.LOGIN_SUCCESS, loginRequestDTO.getEmail());

                return ResponseEntity.ok(
                                ApiResponse.success(SuccessMessages.LOGIN_SUCCESS, response));
        }

        /**
         * Refreshes authentication tokens.
         *
         * @param refreshTokenRequestDTO the refresh token request
         * @return the refreshed token response
         */
        @PostMapping(ApiEndpoints.REFRESH)
        public ResponseEntity<ApiResponse<LoginResponseDTO>> refresh(
                        @Valid @RequestBody final RefreshTokenRequestDTO refreshTokenRequestDTO) {

                LOGGER.info(LogMessages.REFRESH_REQUEST);

                LoginResponseDTO response = authService.refreshToken(refreshTokenRequestDTO);

                LOGGER.info(LogMessages.REFRESH_SUCCESS);

                return ResponseEntity.ok(
                                ApiResponse.success(SuccessMessages.TOKEN_REFRESHED, response));
        }

        /**
         * Logs out an authenticated user.
         *
         * @param userDetails the authenticated user details
         * @return the logout response
         */
        @PostMapping(ApiEndpoints.LOGOUT)
        public ResponseEntity<ApiResponse<Void>> logout(
                        @AuthenticationPrincipal final UserDetails userDetails) {

                String username = userDetails.getUsername();

                LOGGER.info(LogMessages.LOGOUT_REQUEST, username);

                authService.logout(username);

                LOGGER.info(LogMessages.LOGOUT_SUCCESS, username);

                return ResponseEntity.ok(
                                ApiResponse.success(SuccessMessages.LOGOUT_SUCCESS, null));
        }

        /**
         * Activates an account using activation token.
         *
         * @param changePasswordRequestDTO the change password request
         * @return the activation response
         */
        @PostMapping(ApiEndpoints.ACTIVATE)
        public ResponseEntity<ApiResponse<Void>> activate(
                        @Valid @RequestBody final ChangePasswordRequestDTO changePasswordRequestDTO) {

                LOGGER.info(LogMessages.ACTIVATION_REQUEST);

                authService.setPasswordViaActivationToken(changePasswordRequestDTO);

                LOGGER.info(LogMessages.ACTIVATION_SUCCESS);

                return ResponseEntity.ok(
                                ApiResponse.success(SuccessMessages.ACCOUNT_ACTIVATED, null));
        }
}
