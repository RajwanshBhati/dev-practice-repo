package com.interview_tracking_system.backend.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.interview_tracking_system.backend.enums.FeedbackStatus;

/**
 * Unit tests for Feedback entity .
 */
class FeedbackTest {

    @Test
    void testFeedbackFlow() {
        Feedback f = new Feedback();

        f.setComments("Good candidate");
        f.setStrength("Problem solving");
        f.setWeakness("System design");
        f.setRating(4);
        f.setStatus(FeedbackStatus.SELECTED);

        assertEquals("Good candidate", f.getComments());
        assertEquals(4, f.getRating());
        assertEquals(FeedbackStatus.SELECTED, f.getStatus());
    }
}