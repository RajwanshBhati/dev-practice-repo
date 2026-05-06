package com.interview_tracking_system.backend.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.interview_tracking_system.backend.enums.JDStatus;
import com.interview_tracking_system.backend.enums.JobType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

/**
 * Unit test class for JDResponseDTO.
 */
class JDResponseDTOTest {

    /**
     * Tests all-args constructor.
     */
    @Test
    void shouldCreateJDResponseUsingAllArgsConstructor() {
        UUID id = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = createdAt.plusDays(1);

        JDResponseDTO dto = new JDResponseDTO(
                id,
                "Java Developer",
                "Backend development role",
                List.of("Java", "Spring Boot"),
                2,
                5,
                new BigDecimal("500000.00"),
                new BigDecimal("900000.00"),
                "Pune",
                JobType.FULL_TIME,
                JDStatus.ACTIVE,
                createdAt,
                updatedAt);

        assertEquals(id, dto.getId());
        assertEquals("Java Developer", dto.getJobTitle());
        assertEquals("Backend development role", dto.getJobDescription());
        assertEquals(List.of("Java", "Spring Boot"), dto.getSkillsRequired());
        assertEquals(2, dto.getMinExperience());
        assertEquals(5, dto.getMaxExperience());
        assertEquals(new BigDecimal("500000.00"), dto.getMinSalary());
        assertEquals(new BigDecimal("900000.00"), dto.getMaxSalary());
        assertEquals("Pune", dto.getLocation());
        assertEquals(JobType.FULL_TIME, dto.getJobType());
        assertEquals(JDStatus.ACTIVE, dto.getStatus());
        assertEquals(createdAt, dto.getCreatedAt());
        assertEquals(updatedAt, dto.getUpdatedAt());
    }

    /**
     * Tests setter and getter methods.
     */
    @Test
    void shouldSetAndGetAllFields() {
        UUID id = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = createdAt.plusHours(1);

        JDResponseDTO dto = new JDResponseDTO();
        dto.setId(id);
        dto.setJobTitle("QA Engineer");
        dto.setJobDescription("Testing role");
        dto.setSkillsRequired(List.of("Manual Testing", "Automation"));
        dto.setMinExperience(1);
        dto.setMaxExperience(3);
        dto.setMinSalary(new BigDecimal("300000.00"));
        dto.setMaxSalary(new BigDecimal("600000.00"));
        dto.setLocation("Mumbai");
        dto.setJobType(JobType.FULL_TIME);
        dto.setStatus(JDStatus.CLOSED);
        dto.setCreatedAt(createdAt);
        dto.setUpdatedAt(updatedAt);

        assertEquals(id, dto.getId());
        assertEquals("QA Engineer", dto.getJobTitle());
        assertEquals("Testing role", dto.getJobDescription());
        assertEquals(List.of("Manual Testing", "Automation"), dto.getSkillsRequired());
        assertEquals(1, dto.getMinExperience());
        assertEquals(3, dto.getMaxExperience());
        assertEquals(new BigDecimal("300000.00"), dto.getMinSalary());
        assertEquals(new BigDecimal("600000.00"), dto.getMaxSalary());
        assertEquals("Mumbai", dto.getLocation());
        assertEquals(JobType.FULL_TIME, dto.getJobType());
        assertEquals(JDStatus.CLOSED, dto.getStatus());
        assertEquals(createdAt, dto.getCreatedAt());
        assertEquals(updatedAt, dto.getUpdatedAt());
    }
}
