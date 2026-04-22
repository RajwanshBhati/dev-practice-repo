package com.interview_tracking_system.backend.service;

import com.interview_tracking_system.backend.dto.ChangePasswordRequestDTO;
import com.interview_tracking_system.backend.dto.LoginRequestDTO;
import com.interview_tracking_system.backend.dto.RefreshTokenRequestDTO;
import com.interview_tracking_system.backend.dto.LoginResponseDTO;
import com.interview_tracking_system.backend.entity.RefreshToken;
import com.interview_tracking_system.backend.entity.User;
import com.interview_tracking_system.backend.enums.UserStatus;
import com.interview_tracking_system.backend.exception.InvalidRequestException;
import com.interview_tracking_system.backend.exception.ResourceNotFoundException;
import com.interview_tracking_system.backend.repository.RefreshTokenRepository;
import com.interview_tracking_system.backend.repository.UserRepository;
import com.interview_tracking_system.backend.security.JwtUtil;

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

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    /**
     * Refresh token validity in days, configurable via application properties.
     */
    @Value("${jwt.refresh-token-days:7}")
    private int refreshTokenDays;

    /**
     * Constructor injection of dependencies
     * 
     * @param authenticationManager
     * @param userRepository
     * @param refreshTokenRepository
     * @param jwtUtil
     * @param passwordEncoder
     */
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

    @Override
    @Transactional
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.getEmail(), loginRequestDTO.getPassword()));

        User user = userRepository.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new InvalidRequestException("Account is not active");
        }

        /**
         * Delete existing refresh tokens for user to prevent multiple active sessions
         * with old tokens.
         */
        refreshTokenRepository.deleteByUser(user);

        /**
         * Generate new access token using JwtUtil. The token will contain user's email
         * and role as claims.
         */
        String accessToken = jwtUtil.generateAccessToken(
                user.getEmail(), user.getRole().name());

        /**
         * Create and save new refresh token in database. The token is a random UUID
         * string with expiry date set to current time + configured days.
         */
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(LocalDateTime.now().plusDays(refreshTokenDays));

        refreshTokenRepository.save(refreshToken);

        /**
         * Create login response with access and refresh tokens.
         */
        LoginResponseDTO response = new LoginResponseDTO();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken.getToken());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());

        return response;
    }

    /**
     * Validate refresh token and return new access + refresh tokens.
     *
     * @param request
     * @return
     */
    @Override
    public LoginResponseDTO refreshToken(RefreshTokenRequestDTO request) {

        RefreshToken storedToken = refreshTokenRepository
                .findByToken(request.getRefreshToken())
                .orElseThrow(() -> new InvalidRequestException("Invalid refresh token"));

        if (storedToken.isExpired()) {
            refreshTokenRepository.delete(storedToken);
            throw new InvalidRequestException("Refresh token expired");
        }

        User user = storedToken.getUser();

        String newAccessToken = jwtUtil.generateAccessToken(
                user.getEmail(), user.getRole().name());

        LoginResponseDTO response = new LoginResponseDTO();
        response.setAccessToken(newAccessToken);
        response.setRefreshToken(storedToken.getToken());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());

        return response;
    }

    /**
     * Invalidate all refresh tokens for the user to effectively log them out.
     *
     * @param email
     */
    @Override
    @Transactional
    public void logout(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        refreshTokenRepository.deleteByUser(user);
    }

    /**
     * Set password for user using activation token.
     *
     * @param request
     */
    @Override
    @Transactional
    public void setPasswordViaActivationToken(ChangePasswordRequestDTO request) {

        User user = userRepository.findByActivationToken(request.getToken())
                .orElseThrow(() -> new InvalidRequestException("Invalid token"));

        if (user.getActivationTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new InvalidRequestException("Token expired");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new InvalidRequestException("Passwords do not match");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setStatus(UserStatus.ACTIVE);
        user.setActivationToken(null);
        user.setActivationTokenExpiry(null);

        userRepository.save(user);
    }
}
