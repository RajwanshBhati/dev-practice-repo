package com.interview_tracking_system.backend.service.impl;

import com.interview_tracking_system.backend.constants.ErrorMessages;
import com.interview_tracking_system.backend.dto.ChangePasswordRequestDTO;
import com.interview_tracking_system.backend.dto.LoginRequestDTO;
import com.interview_tracking_system.backend.dto.LoginResponseDTO;
import com.interview_tracking_system.backend.dto.RefreshTokenRequestDTO;
import com.interview_tracking_system.backend.entity.RefreshToken;
import com.interview_tracking_system.backend.entity.User;
import com.interview_tracking_system.backend.enums.UserStatus;
import com.interview_tracking_system.backend.exception.InvalidRequestException;
import com.interview_tracking_system.backend.exception.ResourceNotFoundException;
import com.interview_tracking_system.backend.repository.RefreshTokenRepository;
import com.interview_tracking_system.backend.repository.UserRepository;
import com.interview_tracking_system.backend.security.JwtUtil;
import com.interview_tracking_system.backend.security.PasswordDecodeUtil;
import com.interview_tracking_system.backend.service.AuthService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.time.LocalDateTime;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementation of AuthService.
 */
@Service
public class AuthServiceImpl implements AuthService {

        /** Logger instance. */
        private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceImpl.class);

        // /** Authentication manager. */
        // private final AuthenticationManager authenticationManager;

        /** User repository. */
        private final UserRepository userRepository;

        /** Refresh token repository. */
        private final RefreshTokenRepository refreshTokenRepository;

        /** JWT utility. */
        private final JwtUtil jwtUtil;

        /** Password encoder. */
        private final PasswordEncoder passwordEncoder;

        /** Refresh token validity in days. */
        @Value("${jwt.refresh-token-days:7}")
        private int refreshTokenDays;

        /**
         * Constructs AuthServiceImpl.
         *
         * @param authManager authentication manager
         * @param userRepo    user repository
         * @param refreshRepo refresh token repository
         * @param jwtUtility  jwt utility
         * @param encoder     password encoder
         */
        @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "Spring dependency injection")
        public AuthServiceImpl(
                        // final AuthenticationManager authManager,
                        final UserRepository userRepo,
                        final RefreshTokenRepository refreshRepo,
                        final JwtUtil jwtUtility,
                        final PasswordEncoder encoder) {

                // this.authenticationManager = authManager;
                this.userRepository = userRepo;
                this.refreshTokenRepository = refreshRepo;
                this.jwtUtil = jwtUtility;
                this.passwordEncoder = encoder;
        }

        /**
         * Authenticates user.
         *
         * @param loginRequestDTO login request
         * @return login response
         */
        @Override
        public LoginResponseDTO login(final LoginRequestDTO loginRequestDTO) {

                LOGGER.info("Login attempt for email: {}",
                                loginRequestDTO.getEmail());

                String email = loginRequestDTO.getEmail().trim().toLowerCase();

                User user = userRepository.findByEmailIgnoreCase(email)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                ErrorMessages.USER_NOT_FOUND));

                String decodedPassword = PasswordDecodeUtil.decodeBase64Password(
                                loginRequestDTO.getPassword());

                if (!passwordEncoder.matches(decodedPassword, user.getPassword())) {
                        throw new InvalidRequestException("Invalid credentials");
                }

                if (!UserStatus.ACTIVE.equals(user.getStatus())) {
                        throw new InvalidRequestException(
                                        ErrorMessages.ACCOUNT_NOT_ACTIVE);
                }

                refreshTokenRepository.deleteByUser(user);

                String accessToken = jwtUtil.generateAccessToken(
                                user.getEmail(),
                                user.getRole().name());

                RefreshToken refreshToken = new RefreshToken();
                refreshToken.setToken(UUID.randomUUID().toString());
                refreshToken.setUser(user);
                refreshToken.setExpiryDate(
                                LocalDateTime.now().plusDays(refreshTokenDays));

                refreshTokenRepository.save(refreshToken);

                LoginResponseDTO response = new LoginResponseDTO();
                response.setAccessToken(accessToken);
                response.setRefreshToken(refreshToken.getToken());
                response.setName(user.getName());
                response.setEmail(user.getEmail());
                response.setRole(user.getRole());

                return response;
        }

        /**
         * Refresh token logic.
         *
         * @param request refresh request
         * @return login response
         */
        @Override
        public LoginResponseDTO refreshToken(
                        final RefreshTokenRequestDTO request) {

                RefreshToken storedToken = refreshTokenRepository.findByToken(
                                request.getRefreshToken())
                                .orElseThrow(() -> new InvalidRequestException(
                                                ErrorMessages.INVALID_REFRESH_TOKEN));

                if (storedToken.isExpired()) {
                        refreshTokenRepository.delete(storedToken);
                        throw new InvalidRequestException(
                                        ErrorMessages.REFRESH_TOKEN_EXPIRED);
                }

                User user = storedToken.getUser();

                String newAccessToken = jwtUtil.generateAccessToken(
                                user.getEmail(),
                                user.getRole().name());

                LoginResponseDTO response = new LoginResponseDTO();
                response.setAccessToken(newAccessToken);
                response.setRefreshToken(storedToken.getToken());
                response.setName(user.getName());
                response.setEmail(user.getEmail());
                response.setRole(user.getRole());

                return response;
        }

        /**
         * Logout logic.
         *
         * @param email user email
         */
        @Override
        @Transactional
        public void logout(final String email) {

                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                ErrorMessages.USER_NOT_FOUND));

                refreshTokenRepository.deleteByUser(user);
        }

        /**
         * Activation logic.
         *
         * @param request change password request
         */
        @Override
        @Transactional
        public void setPasswordViaActivationToken(
                        final ChangePasswordRequestDTO request) {

                User user = userRepository.findByActivationToken(
                                request.getToken())
                                .orElseThrow(() -> new InvalidRequestException(
                                                ErrorMessages.INVALID_ACTIVATION_TOKEN));

                if (user.getActivationTokenExpiry()
                                .isBefore(LocalDateTime.now())) {
                        throw new InvalidRequestException(
                                        ErrorMessages.ACTIVATION_TOKEN_EXPIRED);
                }
                String decodedPassword = PasswordDecodeUtil.decodeBase64Password(request.getNewPassword());
                String decodedConfirmPassword = PasswordDecodeUtil.decodeBase64Password(request.getConfirmPassword());

                if (!decodedPassword.equals(decodedConfirmPassword)) {
                        throw new InvalidRequestException(
                                        ErrorMessages.PASSWORD_MISMATCH);
                }

                user.setPassword(passwordEncoder.encode(decodedPassword));
                user.setStatus(UserStatus.ACTIVE);
                user.setActivationToken(null);
                user.setActivationTokenExpiry(null);

                userRepository.save(user);
        }
}
