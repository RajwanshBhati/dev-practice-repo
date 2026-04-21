package com.interview_tracking_system.backend.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Mapping entity test.
 */
class InterviewPanelTest {

    @Test
    void testMapping() {
        InterviewPanel ip = new InterviewPanel();

        Interview i = new Interview();
        Panel p = new Panel();

        ip.setInterview(i);
        ip.setPanel(p);

        assertNotNull(ip.getInterview());
        assertNotNull(ip.getPanel());
    }
}
