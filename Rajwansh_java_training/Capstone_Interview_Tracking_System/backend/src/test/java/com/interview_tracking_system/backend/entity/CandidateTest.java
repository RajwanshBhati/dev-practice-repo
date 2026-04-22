package com.interview_tracking_system.backend.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.interview_tracking_system.backend.enums.Stage;
import java.math.BigDecimal;

/**
 * Unit tests for Candidate entity .
 */
class CandidateTest {

    @Test
    void testCandidateFullFlow() {
        Candidate c = new Candidate();

        c.setName("Raj");
        c.setEmail("raj@gmail.com");
        c.setMobile("9999999999");
        c.setCurrentCompany("ABC Corp");
        c.setTotalExp(5);
        c.setRelevantExp(4);
        c.setCurrentCtc(new BigDecimal("500000"));
        c.setExpectedCtc(new BigDecimal("800000"));
        c.setNoticePeriod(30);
        c.setPreferredLocation("Pune");
        c.setStatus(Stage.PROFILING);

        assertEquals("Raj", c.getName());
        assertEquals("raj@gmail.com", c.getEmail());
        assertEquals("9999999999", c.getMobile());
        assertEquals("ABC Corp", c.getCurrentCompany());
        assertEquals(5, c.getTotalExp());
        assertEquals(4, c.getRelevantExp());
         assertEquals(new BigDecimal("500000"), c.getCurrentCtc());
        assertEquals(new BigDecimal("800000"), c.getExpectedCtc());
        assertEquals(30, c.getNoticePeriod());
        assertEquals("Pune", c.getPreferredLocation());
        assertEquals(Stage.PROFILING, c.getStatus());
    }

    @Test
    void testObjectCreation() {
        assertNotNull(new Candidate());
    }
}
