package com.interview_tracking_system.backend.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for InterviewPanel mapping entity.
 * Validates interview-panel relationship mapping fields.
 */
class InterviewPanelTest {

    /**
     * Test InterviewPanel entity basic flow.
     */
    @Test
    void testInterviewPanelFlow() {

        InterviewPanel mapping = new InterviewPanel();

        // Set IDs
        Long interviewId = 101L;
        Long panelId = 202L;

        mapping.setInterviewId(interviewId);
        mapping.setPanelId(panelId);

        // Assertions
        assertEquals(interviewId, mapping.getInterviewId());
        assertEquals(panelId, mapping.getPanelId());
    }

    /**
     * Test object creation.
     */
    @Test
    void testObjectCreation() {
        InterviewPanel mapping = new InterviewPanel();
        assertNotNull(mapping);
    }
}
