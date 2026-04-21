package com.interview_tracking_system.backend.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.interview_tracking_system.backend.enums.Role;

/**
 * Unit test class for User entity.
 */
class UserTest {

    /**
     * Test parameterized constructor.
     */
    @Test
    void testUserConstructor() {
        User user = new User("Raj", "raj@gmail.com", "pass", Role.HR, true);

        assertEquals("Raj", user.getName());
        assertEquals("raj@gmail.com", user.getEmail());
        assertEquals(Role.HR, user.getRole());
        assertTrue(user.isActive());
    }

    /**
     * Test setters.
     */
    @Test
    void testSetters() {
        User user = new User();
        user.setName("Amit");

        assertEquals("Amit", user.getName());
    }
}