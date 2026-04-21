package com.interview_tracking_system.backend.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test class for JobDescription entity.
 */
class JobDescriptionTest {

    @Test
    void testJobFields() {
        JobDescription jd = new JobDescription();

        jd.setJobTitle("Backend Developer");
        jd.setLocation("Pune");

        assertEquals("Backend Developer", jd.getJobTitle());
        assertEquals("Pune", jd.getLocation());
    }
}