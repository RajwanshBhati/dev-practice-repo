package com.interview_tracking_system.backend.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.interview_tracking_system.backend.enums.Gender;
import com.interview_tracking_system.backend.enums.Role;
import com.interview_tracking_system.backend.enums.UserStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

/**
 * Unit test class for User entity.
 */
class UserTest {

    /**
     * Tests default constructor and default status value.
     */
    @Test
    void shouldCreateUserWithDefaultStatus() {
        User user = new User();

        assertEquals(UserStatus.ACTIVE, user.getStatus());
    }

    /**
     * Tests parameterized constructor with explicit status.
     */
    @Test
    void shouldCreateUserUsingParameterizedConstructor() {
        LocalDateTime expiry = LocalDateTime.now().plusDays(1);

        User user = new User(
                "Raj",
                "raj@test.com",
                "9876543210",
                "password",
                Role.CANDIDATE,
                UserStatus.INACTIVE,
                "ABC",
                "Developer",
                "token",
                expiry);

        assertEquals("Raj", user.getName());
        assertEquals("raj@test.com", user.getEmail());
        assertEquals("9876543210", user.getMobile());
        assertEquals("password", user.getPassword());
        assertEquals(Role.CANDIDATE, user.getRole());
        assertEquals(UserStatus.INACTIVE, user.getStatus());
        assertEquals("ABC", user.getOrganisation());
        assertEquals("Developer", user.getDesignation());
        assertEquals("token", user.getActivationToken());
        assertEquals(expiry, user.getActivationTokenExpiry());
    }

    /**
     * Tests parameterized constructor fallback status.
     */
    @Test
    void shouldUseActiveStatusWhenConstructorStatusIsNull() {
        User user = new User(
                "Panel",
                "panel@test.com",
                "9876543211",
                "password",
                Role.PANEL,
                null,
                "ABC",
                "Interviewer",
                "token",
                LocalDateTime.now());

        assertEquals(UserStatus.ACTIVE, user.getStatus());
    }

    /**
     * Tests setter and getter methods.
     */
    @Test
    void shouldSetAndGetAllFields() {
        LocalDate dob = LocalDate.of(2000, 1, 1);
        LocalDateTime expiry = LocalDateTime.now().plusHours(5);

        User user = new User();
        user.setId(1L);
        user.setName("HR User");
        user.setEmail("hr@test.com");
        user.setDateOfBirth(dob);
        user.setGender(Gender.MALE);
        user.setMobile("9876543212");
        user.setPassword("encodedPassword");
        user.setRole(Role.HR);
        user.setStatus(UserStatus.LOCKED);
        user.setOrganisation("TalentBridge");
        user.setDesignation("HR Manager");
        user.setActivationToken("activation-token");
        user.setActivationTokenExpiry(expiry);

        assertEquals(1L, user.getId());
        assertEquals("HR User", user.getName());
        assertEquals("hr@test.com", user.getEmail());
        assertEquals(dob, user.getDateOfBirth());
        assertEquals(Gender.MALE, user.getGender());
        assertEquals("9876543212", user.getMobile());
        assertEquals("encodedPassword", user.getPassword());
        assertEquals(Role.HR, user.getRole());
        assertEquals(UserStatus.LOCKED, user.getStatus());
        assertEquals("TalentBridge", user.getOrganisation());
        assertEquals("HR Manager", user.getDesignation());
        assertEquals("activation-token", user.getActivationToken());
        assertEquals(expiry, user.getActivationTokenExpiry());
    }

    /**
     * Tests nullable timestamp fields before persistence.
     */
    @Test
    void shouldHaveNullAuditTimestampsBeforePersistence() {
        User user = new User();

        assertNull(user.getCreatedAt());
        assertNull(user.getUpdatedAt());
    }
}
