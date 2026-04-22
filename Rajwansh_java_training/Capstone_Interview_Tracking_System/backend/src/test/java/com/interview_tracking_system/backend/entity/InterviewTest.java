package com.interview_tracking_system.backend.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;

import com.interview_tracking_system.backend.enums.Stage;

/**
 * Unit tests for Interview entity.
 * Validates field mapping and getter/setter behavior.
 */
class InterviewTest {

    /**
     * Test Interview entity full flow.
     */
    @Test
    void testInterviewFlow() {

        Interview interview = new Interview();

        // Candidate mapping via FK
        Long candidateId = 101L;
        interview.setCandidateId(candidateId);

        // Stage
        interview.setStage(Stage.L1_TECHNICAL);

        // Date & Time
        LocalDate date = LocalDate.of(2026, 4, 22);
        LocalTime time = LocalTime.of(10, 30);

        interview.setDate(date);
        interview.setTime(time);

        // Focus area
        interview.setFocusArea("DSA + System Design");

        // Assertions
        assertEquals(candidateId, interview.getCandidateId());
        assertEquals(Stage.L1_TECHNICAL, interview.getStage());
        assertEquals(date, interview.getDate());
        assertEquals(time, interview.getTime());
        assertEquals("DSA + System Design", interview.getFocusArea());
    }

    /**
     * Test object creation.
     */
    @Test
    void testObjectCreation() {
        Interview interview = new Interview();
        assertNotNull(interview);
    }
}