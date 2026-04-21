package com.interview_tracking_system.backend.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Unit test class for Interview entity.
 */
class InterviewTest {

    @Test
    void testDateAndTime() {
        Interview interview = new Interview();

        interview.setDate(LocalDate.now());
        interview.setTime(LocalTime.NOON);

        assertNotNull(interview.getDate());
        assertNotNull(interview.getTime());
    }
}