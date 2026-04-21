package com.interview_tracking_system.backend.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test class for Candidate entity.
 */
class CandidateTest {

    /**
     * Test to verify Candidate object creation.
     */
    @Test
    void testCandidateCreation() {
        Candidate candidate = new Candidate();
        assertNotNull(candidate);
    }

    /**
     * Test getter and setter methods.
     */
    @Test
    void testGettersAndSetters() {
        Candidate candidate = new Candidate();

        candidate.setName("Raj");
        candidate.setEmail("raj@gmail.com");
        candidate.setMobile("9999999999");
        candidate.setPreferredLocation("Pune");

        assertEquals("Raj", candidate.getName());
        assertEquals("raj@gmail.com", candidate.getEmail());
        assertEquals("9999999999", candidate.getMobile());
        assertEquals("Pune", candidate.getPreferredLocation());
    }
}