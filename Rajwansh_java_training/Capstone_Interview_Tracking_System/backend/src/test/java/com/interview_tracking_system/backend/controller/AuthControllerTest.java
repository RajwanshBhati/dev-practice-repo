package com.interview_tracking_system.backend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.interview_tracking_system.backend.dto.ChangePasswordRequestDTO;
import com.interview_tracking_system.backend.dto.LoginRequestDTO;
import com.interview_tracking_system.backend.dto.LoginResponseDTO;
import com.interview_tracking_system.backend.dto.RefreshTokenRequestDTO;
import com.interview_tracking_system.backend.enums.Role;
import com.interview_tracking_system.backend.service.AuthService;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Unit test class for {@link AuthController}.
 */
@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    /**
     * Mocked instance of {@link AuthService}.
     */
    @Mock
    private AuthService service;

    /**
     * Instance of {@link AuthController} under test.
     */
    private AuthController controller;

    /**
     * Initializes the controller before each test execution.
     */
    @BeforeEach
    void setUp() {
        controller = new AuthController(service);
        SecurityContextHolder.clearContext();
    }

    /**
     * Verifies that all authentication endpoints correctly delegate
     * to {@link AuthService} and return expected responses.
     */
    @Test
    void allAuthEndpointsShouldDelegateToService() {

        LoginResponseDTO response = new LoginResponseDTO();
        response.setEmail("hr@test.com");
        response.setRole(Role.HR);

        LoginRequestDTO login = new LoginRequestDTO();
        login.setEmail("hr@test.com");

        when(service.login(login)).thenReturn(response);

        assertEquals(
                "hr@test.com",
                controller.login(login).getBody().getData().getEmail());

        RefreshTokenRequestDTO refresh = new RefreshTokenRequestDTO();
        refresh.setRefreshToken("r");

        when(service.refreshToken(refresh)).thenReturn(response);

        assertEquals(
                Role.HR,
                controller.refresh(refresh).getBody().getData().getRole());

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "hr@test.com",
                        null,
                        List.of()));

        assertTrue(controller.logout().getBody().isSuccess());

        verify(service).logout("hr@test.com");

        SecurityContextHolder.clearContext();

        ChangePasswordRequestDTO change = new ChangePasswordRequestDTO();

        assertTrue(controller.activate(change).getBody().isSuccess());

        verify(service).setPasswordViaActivationToken(change);
    }
}