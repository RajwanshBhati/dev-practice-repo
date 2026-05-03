package com.interview_tracking_system.backend.services.impl;

import com.interview_tracking_system.backend.dto.ChangePasswordRequestDTO;
import com.interview_tracking_system.backend.dto.LoginRequestDTO;
import com.interview_tracking_system.backend.dto.LoginResponseDTO;
import com.interview_tracking_system.backend.dto.RefreshTokenRequestDTO;
import com.interview_tracking_system.backend.entity.RefreshToken;
import com.interview_tracking_system.backend.entity.User;
import com.interview_tracking_system.backend.enums.Role;
import com.interview_tracking_system.backend.enums.UserStatus;
import com.interview_tracking_system.backend.exception.InvalidRequestException;
import com.interview_tracking_system.backend.exception.ResourceNotFoundException;
import com.interview_tracking_system.backend.repository.RefreshTokenRepository;
import com.interview_tracking_system.backend.repository.UserRepository;
import com.interview_tracking_system.backend.security.JwtUtil;
import com.interview_tracking_system.backend.service.impl.AuthServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;

/**
 * Unit test class for AuthServiceImpl.
 * Covers login, refresh token, logout, and activation flows.
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    private static final String TEST_EMAIL = "test@example.com";

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    private User user;

    /**
     * Initializes test data before each test execution.
     */
    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail(TEST_EMAIL);
        user.setPassword("encodedPass");
        user.setStatus(UserStatus.ACTIVE);
        user.setName("Test User");
        user.setRole(Role.CANDIDATE);
    }

    /**
     * Tests successful login scenario.
     */
    @Test
    void loginsuccess() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail(TEST_EMAIL);
        request.setPassword("123");

        when(userRepository.findByEmailIgnoreCase(anyString()))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("123", "encodedPass"))
                .thenReturn(true);

        when(jwtUtil.generateAccessToken(anyString(), anyString()))
                .thenReturn("access-token");

        LoginResponseDTO response = authService.login(request);

        assertNotNull(response);
        assertEquals("access-token", response.getAccessToken());
        verify(refreshTokenRepository).deleteByUser(user);
        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }

    /**
     * Tests login failure when user is not found.
     */
    @Test
    void loginuserNotFound() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail(TEST_EMAIL);

        when(userRepository.findByEmailIgnoreCase(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> authService.login(request));
    }

    /**
     * Tests login failure due to invalid password.
     */
    @Test
    void logininvalidPassword() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail(TEST_EMAIL);
        request.setPassword("wrong");

        when(userRepository.findByEmailIgnoreCase(anyString()))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(anyString(), anyString()))
                .thenReturn(false);

        assertThrows(InvalidRequestException.class,
                () -> authService.login(request));
    }

    /**
     * Tests login failure for inactive user.
     */
    @Test
    void logininactiveUser() {
        user.setStatus(UserStatus.INACTIVE);

        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail(TEST_EMAIL);
        request.setPassword("123");

        when(userRepository.findByEmailIgnoreCase(anyString()))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(anyString(), anyString()))
                .thenReturn(true);

        assertThrows(InvalidRequestException.class,
                () -> authService.login(request));
    }

    /**
     * Tests successful refresh token flow.
     */
    @Test
    void refreshTokensuccess() {
        RefreshToken token = new RefreshToken();
        token.setToken("token123");
        token.setUser(user);
        token.setExpiryDate(LocalDateTime.now().plusDays(1));

        RefreshTokenRequestDTO request = new RefreshTokenRequestDTO();
        request.setRefreshToken("token123");

        when(refreshTokenRepository.findByToken("token123"))
                .thenReturn(Optional.of(token));

        when(jwtUtil.generateAccessToken(anyString(), anyString()))
                .thenReturn("new-access");

        LoginResponseDTO response = authService.refreshToken(request);

        assertEquals("new-access", response.getAccessToken());
    }

    /**
     * Tests refresh token failure for invalid token.
     */
    @Test
    void refreshTokeninvalidToken() {
        RefreshTokenRequestDTO request = new RefreshTokenRequestDTO();
        request.setRefreshToken("invalid");

        when(refreshTokenRepository.findByToken(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(InvalidRequestException.class,
                () -> authService.refreshToken(request));
    }

    /**
     * Tests refresh token failure for expired token.
     */
    @Test
    void refreshTokenexpired() {
        RefreshToken token = new RefreshToken();
        token.setToken("expired");
        token.setUser(user);
        token.setExpiryDate(LocalDateTime.now().minusDays(1));

        RefreshTokenRequestDTO request = new RefreshTokenRequestDTO();
        request.setRefreshToken("expired");

        when(refreshTokenRepository.findByToken(anyString()))
                .thenReturn(Optional.of(token));

        assertThrows(InvalidRequestException.class,
                () -> authService.refreshToken(request));

        verify(refreshTokenRepository).delete(token);
    }

    /**
     * Tests successful logout operation.
     */
    @Test
    void logoutsuccess() {
        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(user));

        authService.logout(TEST_EMAIL);

        verify(refreshTokenRepository).deleteByUser(user);
    }

    /**
     * Tests logout failure when user not found.
     */
    @Test
    void logoutuserNotFound() {
        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> authService.logout(TEST_EMAIL));
    }

    /**
     * Tests successful password activation flow.
     */
    @Test
    void setPasswordsuccess() {
        user.setActivationToken("token");
        user.setActivationTokenExpiry(LocalDateTime.now().plusDays(1));

        ChangePasswordRequestDTO request = new ChangePasswordRequestDTO();
        request.setToken("token");
        request.setNewPassword("123");
        request.setConfirmPassword("123");

        when(userRepository.findByActivationToken("token"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.encode(anyString()))
                .thenReturn("encoded");

        authService.setPasswordViaActivationToken(request);

        verify(userRepository).save(user);
        assertEquals(UserStatus.ACTIVE, user.getStatus());
    }

    /**
     * Tests activation failure due to invalid token.
     */
    @Test
    void setPasswordinvalidToken() {
        ChangePasswordRequestDTO request = new ChangePasswordRequestDTO();
        request.setToken("invalid");

        when(userRepository.findByActivationToken(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(InvalidRequestException.class,
                () -> authService.setPasswordViaActivationToken(request));
    }

    /**
     * Tests activation failure due to expired token.
     */
    @Test
    void setPasswordexpiredToken() {
        user.setActivationTokenExpiry(LocalDateTime.now().minusDays(1));

        ChangePasswordRequestDTO request = new ChangePasswordRequestDTO();
        request.setToken("token");

        when(userRepository.findByActivationToken(anyString()))
                .thenReturn(Optional.of(user));

        assertThrows(InvalidRequestException.class,
                () -> authService.setPasswordViaActivationToken(request));
    }

    /**
     * Tests activation failure due to password mismatch.
     */
    @Test
    void setPasswordmismatch() {
        user.setActivationTokenExpiry(LocalDateTime.now().plusDays(1));

        ChangePasswordRequestDTO request = new ChangePasswordRequestDTO();
        request.setToken("token");
        request.setNewPassword("123");
        request.setConfirmPassword("456");

        when(userRepository.findByActivationToken(anyString()))
                .thenReturn(Optional.of(user));

        assertThrows(InvalidRequestException.class,
                () -> authService.setPasswordViaActivationToken(request));
    }
}
