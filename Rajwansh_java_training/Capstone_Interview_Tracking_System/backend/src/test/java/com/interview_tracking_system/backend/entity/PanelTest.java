package com.interview_tracking_system.backend.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

/**
 * Unit test class for Panel entity.
 */
class PanelTest {

    /**
     * Tests default values for a newly created Panel.
     */
    @Test
    void shouldHaveDefaultValues() {
        Panel panel = new Panel();

        assertFalse(panel.isActive());
        assertNull(panel.getId());
        assertNull(panel.getFullName());
        assertNull(panel.getEmail());
    }

    /**
     * Tests setter and getter methods for all fields.
     */
    @Test
    void shouldSetAndGetAllFields() {
        Panel panel = new Panel();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiry = now.plusDays(1);

        panel.setFullName("Panel User");
        panel.setEmail("panel@test.com");
        panel.setMobile("9876543210");
        panel.setOrganization("ABC Corp");
        panel.setDesignation("Senior Engineer");
        panel.setPassword("encodedPassword");
        panel.setActive(true);
        panel.setActivationToken("token123");
        panel.setTokenExpiry(expiry);
        panel.setCreatedAt(now);
        panel.setUpdatedAt(expiry);

        assertEquals("Panel User", panel.getFullName());
        assertEquals("panel@test.com", panel.getEmail());
        assertEquals("9876543210", panel.getMobile());
        assertEquals("ABC Corp", panel.getOrganization());
        assertEquals("Senior Engineer", panel.getDesignation());
        assertEquals("encodedPassword", panel.getPassword());
        assertTrue(panel.isActive());
        assertEquals("token123", panel.getActivationToken());
        assertEquals(expiry, panel.getTokenExpiry());
        assertEquals(now, panel.getCreatedAt());
        assertEquals(expiry, panel.getUpdatedAt());
    }

    /**
     * Tests activation status toggle.
     */
    @Test
    void shouldUpdateActiveStatus() {
        Panel panel = new Panel();

        panel.setActive(true);
        assertTrue(panel.isActive());

        panel.setActive(false);
        assertFalse(panel.isActive());
    }

    /**
     * Tests null handling for optional fields.
     */
    @Test
    void shouldAllowNullValuesForOptionalFields() {
        Panel panel = new Panel();

        panel.setMobile(null);
        panel.setOrganization(null);
        panel.setDesignation(null);
        panel.setPassword(null);
        panel.setActivationToken(null);
        panel.setTokenExpiry(null);

        assertNull(panel.getMobile());
        assertNull(panel.getOrganization());
        assertNull(panel.getDesignation());
        assertNull(panel.getPassword());
        assertNull(panel.getActivationToken());
        assertNull(panel.getTokenExpiry());
    }

    /**
     * Tests timestamp fields before persistence.
     */
    @Test
    void shouldHaveNullTimestampsInitially() {
        Panel panel = new Panel();

        assertNull(panel.getCreatedAt());
        assertNull(panel.getUpdatedAt());
    }
}
