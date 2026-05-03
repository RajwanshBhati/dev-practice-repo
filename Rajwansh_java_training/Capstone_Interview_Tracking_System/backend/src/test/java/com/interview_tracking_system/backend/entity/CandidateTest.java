package com.interview_tracking_system.backend.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.interview_tracking_system.backend.enums.Stage;
import java.math.BigDecimal;

/**
 * Unit tests for Candidate entity.
 */
class CandidateTest {

    @Test
    void testCandidateFullFlow() {
        Candidate candidate = new Candidate();

        candidate.setName("Raj");
        candidate.setEmail("raj@gmail.com");
        candidate.setMobile("9999999999");
        candidate.setCurrentCompany("ABC Corp");
        candidate.setTotalExp(5);
        candidate.setRelevantExp(4);
        candidate.setCurrentCtc(new BigDecimal("500000"));
        candidate.setExpectedCtc(new BigDecimal("800000"));
        candidate.setNoticePeriod(30);
        candidate.setPreferredLocation("Pune");
        candidate.setStatus(Stage.PROFILING);

        assertEquals("Raj", candidate.getName());
        assertEquals("raj@gmail.com", candidate.getEmail());
        assertEquals("9999999999", candidate.getMobile());
        assertEquals("ABC Corp", candidate.getCurrentCompany());
        assertEquals(5, candidate.getTotalExp());
        assertEquals(4, candidate.getRelevantExp());
        assertEquals(new BigDecimal("500000"), candidate.getCurrentCtc());
        assertEquals(new BigDecimal("800000"), candidate.getExpectedCtc());
        assertEquals(30, candidate.getNoticePeriod());
        assertEquals("Pune", candidate.getPreferredLocation());
        assertEquals(Stage.PROFILING, candidate.getStatus());
    }

    @Test
    void testObjectCreation() {
        Candidate candidate = new Candidate();
        assertNotNull(candidate);
    }
}
