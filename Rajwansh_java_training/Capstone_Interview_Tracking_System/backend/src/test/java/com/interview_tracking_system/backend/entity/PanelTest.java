package com.interview_tracking_system.backend.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test class for Panel entity.
 */
class PanelTest {

    @Test
    void testPanelFields() {
        Panel panel = new Panel();

        panel.setName("Interviewer");
        panel.setEmail("panel@gmail.com");

        assertEquals("Interviewer", panel.getName());
        assertEquals("panel@gmail.com", panel.getEmail());
    }
}