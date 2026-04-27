package com.interview_tracking_system.backend.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PanelTest {

    /**
     * Test Panel entity getters and setters.
     */
    @Test
    void testPanelGettersSetters() {

        Panel panel = new Panel();

        panel.setFullName("HR Panel");
        panel.setEmail("hr@company.com");
        panel.setMobile("9999999999");
        panel.setOrganization("Tech Corp");
        panel.setDesignation("Senior HR");
        panel.setActive(true);

        assertEquals("HR Panel", panel.getFullName());
        assertEquals("hr@company.com", panel.getEmail());
        assertEquals("9999999999", panel.getMobile());
        assertEquals("Tech Corp", panel.getOrganization());
        assertEquals("Senior HR", panel.getDesignation());
        assertTrue(panel.isActive());
    }
}
