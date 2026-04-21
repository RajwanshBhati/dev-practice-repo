package com.interview_tracking_system.backend.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.interview_tracking_system.backend.enums.JobType;
import com.interview_tracking_system.backend.enums.JDStatus;

import java.math.BigDecimal;
import java.util.List;

/**
 * Unit tests for JobDescription entity .
 */
class JobDescriptionTest {

    @Test
    void testFullJobDescriptionFlow() {
        JobDescription jd = new JobDescription();

        jd.setJobTitle("Java Developer");
        jd.setJobDescription("Backend role");
        jd.setSkillsRequired(List.of("Java", "Spring"));
        jd.setExperienceMin(2);
        jd.setExperienceMax(5);
        jd.setSalaryMin(new BigDecimal("500000"));
        jd.setSalaryMax(new BigDecimal("1200000"));
        jd.setLocation("Pune");
        jd.setJobType(JobType.FULL_TIME);

        assertEquals("Java Developer", jd.getJobTitle());
        assertEquals(2, jd.getExperienceMin());
        assertEquals(JobType.FULL_TIME, jd.getJobType());
    }
}
