package com.interview_tracking_system.backend.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Unit tests for Interview ent.
 */
class InterviewTest {

    @Test
    void testInterviewFlow() {
        Interview i = new Interview();

        Candidate c = new Candidate();
        i.setCandidate(c);

        i.setDate(LocalDate.now());
        i.setTime(LocalTime.of(10, 30));

        assertNotNull(i.getCandidate());
        assertNotNull(i.getDate());
        assertNotNull(i.getTime());
    }
}
