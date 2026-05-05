package com.interview_tracking_system.backend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import com.interview_tracking_system.backend.dto.ChangePasswordRequestDTO;
import com.interview_tracking_system.backend.dto.LoginRequestDTO;
import com.interview_tracking_system.backend.dto.LoginResponseDTO;
import com.interview_tracking_system.backend.dto.RefreshTokenRequestDTO;
import com.interview_tracking_system.backend.enums.Role;
import com.interview_tracking_system.backend.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

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
    }

    /**
     * Verifies that all authentication endpoints:
     * correctly delegate to {@link AuthService} and return expected responses.
     */
    @Test
    void allAuthEndpointsShouldDelegateToService() {

        /**
         * Test login endpoint delegation.
         */
        LoginResponseDTO response = new LoginResponseDTO();
        response.setEmail("hr@test.com");
        response.setRole(Role.HR);

        LoginRequestDTO login = new LoginRequestDTO();
        login.setEmail("hr@test.com");

        when(service.login(login)).thenReturn(response);

        assertEquals(
                "hr@test.com",
                controller.login(login).getBody().getData().getEmail());

        /**
         * Test refresh token endpoint delegation.
         */
        RefreshTokenRequestDTO refresh = new RefreshTokenRequestDTO();
        refresh.setRefreshToken("r");

        when(service.refreshToken(refresh)).thenReturn(response);

        assertEquals(
                Role.HR,
                controller.refresh(refresh).getBody().getData().getRole());

        /**
         * Test logout endpoint delegation.
         */
        UserDetails principal = User.withUsername("hr@test.com")
                .password("x")
                .roles("HR")
                .build();

        assertTrue(controller.logout().getBody().isSuccess());

        verify(service).logout("hr@test.com");

        /**
         * Test activation (set password via token) delegation.
         */
        ChangePasswordRequestDTO change = new ChangePasswordRequestDTO();

        assertTrue(controller.activate(change).getBody().isSuccess());

        verify(service).setPasswordViaActivationToken(change);
    }
}
