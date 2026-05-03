package com.interview_tracking_system.backend.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.interview_tracking_system.backend.enums.Role;
import org.junit.jupiter.api.Test;

/**
 * Unit test class for LoginResponseDTO.
 */
class LoginResponseDTOTest {

    /**
     * Tests default token type value.
     */
    @Test
    void shouldHaveDefaultTokenType() {
        LoginResponseDTO dto = new LoginResponseDTO();

        assertEquals("Bearer", dto.getTokenType());
    }

    /**
     * Tests all-args constructor.
     */
    @Test
    void shouldCreateLoginResponseUsingAllArgsConstructor() {
        LoginResponseDTO dto = new LoginResponseDTO(
                "access-token",
                "refresh-token",
                "Bearer",
                "Raj",
                "raj@test.com",
                Role.CANDIDATE);

        assertEquals("access-token", dto.getAccessToken());
        assertEquals("refresh-token", dto.getRefreshToken());
        assertEquals("Bearer", dto.getTokenType());
        assertEquals("Raj", dto.getName());
        assertEquals("raj@test.com", dto.getEmail());
        assertEquals(Role.CANDIDATE, dto.getRole());
    }

    /**
     * Tests setter and getter methods.
     */
    @Test
    void shouldSetAndGetAllFields() {
        LoginResponseDTO dto = new LoginResponseDTO();

        dto.setAccessToken("access");
        dto.setRefreshToken("refresh");
        dto.setTokenType("Bearer");
        dto.setName("HR User");
        dto.setEmail("hr@test.com");
        dto.setRole(Role.HR);

        assertEquals("access", dto.getAccessToken());
        assertEquals("refresh", dto.getRefreshToken());
        assertEquals("Bearer", dto.getTokenType());
        assertEquals("HR User", dto.getName());
        assertEquals("hr@test.com", dto.getEmail());
        assertEquals(Role.HR, dto.getRole());
    }

    /**
     * Tests builder object creation.
     */
    @Test
    void shouldBuildLoginResponseUsingBuilder() {
        LoginResponseDTO dto = LoginResponseDTO.builder()
                .accessToken("access")
                .refreshToken("refresh")
                .tokenType("Bearer")
                .name("Panel User")
                .email("panel@test.com")
                .role(Role.PANEL)
                .build();

        assertEquals("access", dto.getAccessToken());
        assertEquals("refresh", dto.getRefreshToken());
        assertEquals("Bearer", dto.getTokenType());
        assertEquals("Panel User", dto.getName());
        assertEquals("panel@test.com", dto.getEmail());
        assertEquals(Role.PANEL, dto.getRole());
    }

    /**
     * Tests builder default token type.
     */
    @Test
    void shouldUseDefaultTokenTypeInBuilder() {
        LoginResponseDTO dto = LoginResponseDTO.builder()
                .accessToken("access")
                .build();

        assertEquals("Bearer", dto.getTokenType());
    }
}
