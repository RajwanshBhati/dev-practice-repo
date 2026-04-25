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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Implementation of AuthService.
 */
@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    /**
     * Dependencies for authentication, user management, token handling, and
     * password encoding.
     */
    private final AuthenticationManager authenticationManager;

    /**
     * Repositories and utilities for user and token management, JWT handling, and
     * password encoding.
     */
    private final UserRepository userRepository;

    /**
     * Repository for managing refresh tokens, including creation, validation, and
     * deletion of tokens associated with users.
     */
    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * Utility for generating and validating JWT tokens.
     */
    private final JwtUtil jwtUtil;

    /**
     * Encoder for hashing passwords.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * The number of days for which refresh tokens are valid.
     */
    @Value("${jwt.refresh-token-days:7}")
    private int refreshTokenDays;

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
     * Handles user login and generates access and refresh tokens.
     *
     * @param loginRequestDTO the login request containing email and password
     * @return the login response with tokens and user details
     */
    @Override
    @Transactional
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {

        log.info("Login attempt for email: {}", loginRequestDTO.getEmail());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.getEmail(),
                        loginRequestDTO.getPassword()));

        User user = userRepository.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(() -> {
                    log.error("User not found: {}", loginRequestDTO.getEmail());
                    return new ResourceNotFoundException(ErrorMessages.USER_NOT_FOUND);
                });

        if (!passwordEncoder.matches(
                loginRequestDTO.getPassword(),
                user.getPassword())) {
            throw new InvalidRequestException("Invalid credentials");
        }

        if (user.getStatus() != UserStatus.ACTIVE) {
            log.warn("Inactive account login attempt: {}", user.getEmail());
            throw new InvalidRequestException(ErrorMessages.ACCOUNT_NOT_ACTIVE);
        }

        refreshTokenRepository.deleteByUser(user);
        refreshTokenRepository.flush();

        String accessToken = jwtUtil.generateAccessToken(
                user.getEmail(), user.getRole().name());

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(LocalDateTime.now().plusDays(refreshTokenDays));

        refreshTokenRepository.save(refreshToken);

        log.info("Login successful for: {}", user.getEmail());

        LoginResponseDTO response = new LoginResponseDTO();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken.getToken());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());

        return response;
    }

    /**
     * Refreshes the access token using a valid refresh token.
     *
     * @param request the refresh token request containing the refresh token
     * @return the updated login response with new tokens and user details
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

        if (storedToken.isExpired()) {
            log.warn("Expired refresh token used: {}", storedToken.getToken());
            refreshTokenRepository.delete(storedToken);
            throw new InvalidRequestException(ErrorMessages.REFRESH_TOKEN_EXPIRED);
        }

        User user = storedToken.getUser();

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
     * Logs out a user by invalidating their refresh token.
     *
     * @param email the email of the user to log out
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
     * Sets a new password for a user using an activation token.
     *
     * @param request the change password request containing the activation token
     *                and new password
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

        if (user.getActivationTokenExpiry().isBefore(LocalDateTime.now())) {
            log.warn("Expired activation token for user: {}", user.getEmail());
            throw new InvalidRequestException(ErrorMessages.ACTIVATION_TOKEN_EXPIRED);
        }

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
