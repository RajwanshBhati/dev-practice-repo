package com.interview_tracking_system.backend.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.interview_tracking_system.backend.enums.JDStatus;
import com.interview_tracking_system.backend.enums.JobType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

/**
 * Unit test class for JobDescription entity.
 */
class JobDescriptionTest {

    /**
     * Tests all-args constructor and getter methods.
     */
    @Test
    void shouldCreateJobDescriptionUsingAllArgsConstructor() {
        UUID id = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = createdAt.plusDays(1);
        List<String> skills = List.of("Java", "Spring Boot");

        JobDescription jd = new JobDescription(
                id,
                "Java Developer",
                "Backend role",
                skills,
                2,
                5,
                new BigDecimal("500000.00"),
                new BigDecimal("900000.00"),
                "Pune",
                JobType.FULL_TIME,
                JDStatus.ACTIVE,
                createdAt,
                updatedAt);

        assertEquals(id, jd.getId());
        assertEquals("Java Developer", jd.getJobTitle());
        assertEquals("Backend role", jd.getJobDescription());
        assertEquals(skills, jd.getSkillsRequired());
        assertEquals(2, jd.getExperienceMin());
        assertEquals(5, jd.getExperienceMax());
        assertEquals(new BigDecimal("500000.00"), jd.getSalaryMin());
        assertEquals(new BigDecimal("900000.00"), jd.getSalaryMax());
        assertEquals("Pune", jd.getLocation());
        assertEquals(JobType.FULL_TIME, jd.getJobType());
        assertEquals(JDStatus.ACTIVE, jd.getStatus());
        assertEquals(createdAt, jd.getCreatedAt());
        assertEquals(updatedAt, jd.getUpdatedAt());
    }

    /**
     * Tests setter and getter methods.
     */
    @Test
    void shouldSetAndGetAllFields() {
        UUID id = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = createdAt.plusHours(2);

        JobDescription jd = new JobDescription();
        jd.setId(id);
        jd.setJobTitle("Frontend Developer");
        jd.setJobDescription("React role");
        jd.setSkillsRequired(List.of("React", "JavaScript"));
        jd.setExperienceMin(1);
        jd.setExperienceMax(3);
        jd.setSalaryMin(new BigDecimal("300000.00"));
        jd.setSalaryMax(new BigDecimal("600000.00"));
        jd.setLocation("Bangalore");
        jd.setJobType(JobType.FULL_TIME);
        jd.setStatus(JDStatus.CLOSED);
        jd.setCreatedAt(createdAt);
        jd.setUpdatedAt(updatedAt);

        assertEquals(id, jd.getId());
        assertEquals("Frontend Developer", jd.getJobTitle());
        assertEquals("React role", jd.getJobDescription());
        assertEquals(List.of("React", "JavaScript"), jd.getSkillsRequired());
        assertEquals(1, jd.getExperienceMin());
        assertEquals(3, jd.getExperienceMax());
        assertEquals(new BigDecimal("300000.00"), jd.getSalaryMin());
        assertEquals(new BigDecimal("600000.00"), jd.getSalaryMax());
        assertEquals("Bangalore", jd.getLocation());
        assertEquals(JobType.FULL_TIME, jd.getJobType());
        assertEquals(JDStatus.CLOSED, jd.getStatus());
        assertEquals(createdAt, jd.getCreatedAt());
        assertEquals(updatedAt, jd.getUpdatedAt());
    }

    /**
     * Tests default status value.
     */
    @Test
    void shouldHaveActiveStatusByDefault() {
        JobDescription jd = new JobDescription();

        assertEquals(JDStatus.ACTIVE, jd.getStatus());
    }

    /**
     * Tests defensive copy behavior for skills setter and getter.
     */
    @Test
    void shouldUseDefensiveCopyForSkillsRequired() {
        List<String> skills = new ArrayList<>();
        skills.add("Java");

        JobDescription jd = new JobDescription();
        jd.setSkillsRequired(skills);

        skills.add("Angular");

        List<String> returnedSkills = jd.getSkillsRequired();
        returnedSkills.add("React");

        assertEquals(List.of("Java"), jd.getSkillsRequired());
        assertNotSame(skills, jd.getSkillsRequired());
    }

    /**
     * Tests null handling for skills list.
     */
    @Test
    void shouldReturnNullWhenSkillsRequiredIsNull() {
        JobDescription jd = new JobDescription();

        jd.setSkillsRequired(null);

        assertNull(jd.getSkillsRequired());
    }

    /**
     * Tests builder method.
     */
    @Test
    void shouldBuildJobDescriptionUsingBuilder() {
        UUID id = UUID.randomUUID();

        JobDescription jd = JobDescription.builder()
                .id(id)
                .jobTitle("QA Engineer")
                .jobDescription("Testing role")
                .skillsRequired(List.of("Manual Testing", "Automation"))
                .minExperience(2)
                .maxExperience(4)
                .minSalary(new BigDecimal("400000.00"))
                .maxSalary(new BigDecimal("700000.00"))
                .location("Mumbai")
                .jobType(JobType.FULL_TIME)
                .status(JDStatus.INACTIVE)
                .build();

        assertEquals(id, jd.getId());
        assertEquals("QA Engineer", jd.getJobTitle());
        assertEquals("Testing role", jd.getJobDescription());
        assertEquals(List.of("Manual Testing", "Automation"), jd.getSkillsRequired());
        assertEquals(2, jd.getExperienceMin());
        assertEquals(4, jd.getExperienceMax());
        assertEquals(new BigDecimal("400000.00"), jd.getSalaryMin());
        assertEquals(new BigDecimal("700000.00"), jd.getSalaryMax());
        assertEquals("Mumbai", jd.getLocation());
        assertEquals(JobType.FULL_TIME, jd.getJobType());
        assertEquals(JDStatus.INACTIVE, jd.getStatus());
    }

    /**
     * Tests builder default status.
     */
    @Test
    void shouldUseActiveStatusByDefaultInBuilder() {
        JobDescription jd = JobDescription.builder()
                .jobTitle("DevOps Engineer")
                .build();

        assertEquals(JDStatus.ACTIVE, jd.getStatus());
    }
}
