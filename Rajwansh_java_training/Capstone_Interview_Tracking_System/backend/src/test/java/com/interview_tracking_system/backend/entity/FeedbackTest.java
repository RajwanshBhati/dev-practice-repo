package com.interview_tracking_system.backend.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test class for Feedback entity.
 */
class FeedbackTest {

    @Test
    void testFeedbackFields() {
        Feedback feedback = new Feedback();

        feedback.setComments("Good candidate");
        feedback.setRating(5);

        assertEquals("Good candidate", feedback.getComments());
        assertEquals(5, feedback.getRating());
    }
}