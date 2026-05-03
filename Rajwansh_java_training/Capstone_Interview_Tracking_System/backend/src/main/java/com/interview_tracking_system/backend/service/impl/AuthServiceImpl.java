package com.interview_tracking_system.backend.service.impl;

import com.interview_tracking_system.backend.constants.ErrorMessages;
import com.interview_tracking_system.backend.dto.ChangePasswordRequestDTO;
import com.interview_tracking_system.backend.dto.LoginRequestDTO;
import com.interview_tracking_system.backend.dto.LoginResponseDTO;
import com.interview_tracking_system.backend.entity.RefreshToken;
import com.interview_tracking_system.backend.entity.User;
import com.interview_tracking_system.backend.enums.UserStatus;
import com.interview_tracking_system.backend.exception.InvalidRequestException;
import com.interview_tracking_system.backend.exception.ResourceNotFoundException;
import com.interview_tracking_system.backend.repository.RefreshTokenRepository;
import com.interview_tracking_system.backend.repository.UserRepository;
import com.interview_tracking_system.backend.security.JwtUtil;
import com.interview_tracking_system.backend.service.AuthService;
import com.interview_tracking_system.backend.dto.RefreshTokenRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Service implementation of {@link AuthService} that handles
 * authentication operations such as login, logout, token refresh,
 * and account activation.
 */
@Service
public class AuthServiceImpl implements AuthService {

    /**
     * Logger for tracking authentication events.
     */
    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    /**
     * Authentication manager for handling authentication flow.
     */
    private final AuthenticationManager authenticationManager;

    /**
     * Repository for managing user-related database operations.
     */
    private final UserRepository userRepository;

    /**
     * Repository for managing refresh tokens.
     */
    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * Utility class for JWT token generation and validation.
     */
    private final JwtUtil jwtUtil;

    /**
     * Encoder for hashing and verifying passwords.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Number of days a refresh token remains valid.
     */
    @Value("${jwt.refresh-token-days:7}")
    private int refreshTokenDays;

    /**
     * Constructor for dependency injection.
     *
     * @param authenticationManager  authentication manager
     * @param userRepository         user repository
     * @param refreshTokenRepository refresh token repository
     * @param jwtUtil                JWT utility
     * @param passwordEncoder        password encoder
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "Spring dependency injection stores framework-managed beans.")
    public AuthServiceImpl(AuthenticationManager authenticationManager,
            UserRepository userRepository,
            RefreshTokenRepository refreshTokenRepository,
            JwtUtil jwtUtil,
            PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Authenticates a user and generates access and refresh tokens.
     *
     * @param loginRequestDTO login request containing credentials
     * @return login response containing tokens and user details
     */
    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {

        log.info("Login attempt for email: {}", loginRequestDTO.getEmail());

        String email = loginRequestDTO.getEmail().trim().toLowerCase();

        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> {
                    log.error("User not found: {}", loginRequestDTO.getEmail());
                    return new ResourceNotFoundException(ErrorMessages.USER_NOT_FOUND);
                });

        /** Validates password against stored hash */
        if (!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())) {
            throw new InvalidRequestException("Invalid credentials");
        }

        /** Checks if account is active */
        if (!UserStatus.ACTIVE.equals(user.getStatus())) {
            log.warn("Inactive account login attempt: {}", user.getEmail());
            throw new InvalidRequestException(ErrorMessages.ACCOUNT_NOT_ACTIVE);
        }

        /** Deletes any existing refresh tokens for the user */
        refreshTokenRepository.deleteByUser(user);

        /** Generates access token */
        String accessToken = jwtUtil.generateAccessToken(
                user.getEmail(),
                user.getRole().name());

        /** Creates and stores a new refresh token */
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(LocalDateTime.now().plusDays(refreshTokenDays));

        refreshTokenRepository.save(refreshToken);

        log.info("Login successful for: {}", user.getEmail());

        /** Builds response object */
        LoginResponseDTO response = new LoginResponseDTO();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken.getToken());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());

        return response;
    }

    /**
     * Refreshes access token using a valid refresh token.
     *
     * @param request refresh token request DTO
     * @return updated login response
     */
    @Override
    public LoginResponseDTO refreshToken(RefreshTokenRequestDTO request) {

        log.info("Refresh token request received");

        RefreshToken storedToken = refreshTokenRepository
                .findByToken(request.getRefreshToken())
                .orElseThrow(() -> {
                    log.error("Invalid refresh token: {}", request.getRefreshToken());
                    return new InvalidRequestException(ErrorMessages.INVALID_REFRESH_TOKEN);
                });

        /** Checks if refresh token is expired */
        if (storedToken.isExpired()) {
            log.warn("Expired refresh token used: {}", storedToken.getToken());
            refreshTokenRepository.delete(storedToken);
            throw new InvalidRequestException(ErrorMessages.REFRESH_TOKEN_EXPIRED);
        }

        User user = storedToken.getUser();

        /** Generates new access token */
        String newAccessToken = jwtUtil.generateAccessToken(
                user.getEmail(), user.getRole().name());

        log.info("Token refreshed for user: {}", user.getEmail());

        LoginResponseDTO response = new LoginResponseDTO();
        response.setAccessToken(newAccessToken);
        response.setRefreshToken(storedToken.getToken());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());

        return response;
    }

    /**
     * Logs out user by deleting associated refresh tokens.
     *
     * @param email user email
     */
    @Override
    @Transactional
    public void logout(String email) {

        log.info("Logout request for: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("Logout failed - user not found: {}", email);
                    return new ResourceNotFoundException(ErrorMessages.USER_NOT_FOUND);
                });

        refreshTokenRepository.deleteByUser(user);

        log.info("Logout successful for: {}", email);
    }

    /**
     * Activates user account and sets new password using activation token.
     *
     * @param request change password request DTO
     */
    @Override
    @Transactional
    public void setPasswordViaActivationToken(ChangePasswordRequestDTO request) {

        log.info("Activation attempt with token: {}", request.getToken());

        User user = userRepository.findByActivationToken(request.getToken())
                .orElseThrow(() -> {
                    log.error("Invalid activation token: {}", request.getToken());
                    return new InvalidRequestException(ErrorMessages.INVALID_ACTIVATION_TOKEN);
                });

        /** Checks if activation token is expired */
        if (user.getActivationTokenExpiry().isBefore(LocalDateTime.now())) {
            log.warn("Expired activation token for user: {}", user.getEmail());
            throw new InvalidRequestException(ErrorMessages.ACTIVATION_TOKEN_EXPIRED);
        }

        /** Validates password and confirm password match */
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            log.warn("Password mismatch during activation for user: {}", user.getEmail());
            throw new InvalidRequestException(ErrorMessages.PASSWORD_MISMATCH);
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setStatus(UserStatus.ACTIVE);
        user.setActivationToken(null);
        user.setActivationTokenExpiry(null);

        userRepository.save(user);

        log.info("Account activated successfully for: {}", user.getEmail());
    }
}
