package com.interview_tracking_system.backend.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.interview_tracking_system.backend.enums.Role;

/**
 * Unit test for User entity.
 */
class UserTest {

    /**
     * Test User entity getters and setters.
     */
    @Test
    void testUser() {
        User user = new User();

        user.setName("Raj");
        user.setEmail("raj@test.com");
        user.setPassword("12345");
        user.setRole(Role.HR);
        user.setActive(true);

        assertEquals("Raj", user.getName());
        assertEquals("raj@test.com", user.getEmail());
        assertEquals(Role.HR, user.getRole());
        assertTrue(user.isActive());
    }
}