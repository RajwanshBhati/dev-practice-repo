package com.interview_tracking_system.backend.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.interview_tracking_system.backend.dto.JDRequestDTO;
import com.interview_tracking_system.backend.dto.JDResponseDTO;
import com.interview_tracking_system.backend.entity.JobDescription;
import com.interview_tracking_system.backend.enums.JDStatus;
import com.interview_tracking_system.backend.enums.JobType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class JDMapperTest {

    private final JDMapper mapper = new JDMapper();

    @Test
    void toEntityShouldMapAllFields() {
        JDRequestDTO request = createRequest();

        JobDescription entity = mapper.toEntity(request);

        assertNotNull(entity);
        assertEquals("Java Developer", entity.getJobTitle());
        assertEquals("Backend role", entity.getJobDescription());
        assertEquals(List.of("Java", "Spring Boot"), entity.getSkillsRequired());
        assertEquals(2, entity.getExperienceMin());
        assertEquals(5, entity.getExperienceMax());
        assertEquals(new BigDecimal("500000"), entity.getSalaryMin());
        assertEquals(new BigDecimal("1200000"), entity.getSalaryMax());
        assertEquals("Pune", entity.getLocation());
        assertEquals(JobType.FULL_TIME, entity.getJobType());
        assertNotSame(request.getSkillsRequired(), entity.getSkillsRequired());
    }

    @Test
    void toResponseDTOShouldMapAllFields() {
        JobDescription entity = createEntity();

        JDResponseDTO response = mapper.toResponseDTO(entity);

        assertNotNull(response);
        assertEquals(entity.getId(), response.getId());
        assertEquals("Java Developer", response.getJobTitle());
        assertEquals("Backend role", response.getJobDescription());
        assertEquals(List.of("Java", "Spring Boot"), response.getSkillsRequired());
        assertEquals(2, response.getMinExperience());
        assertEquals(5, response.getMaxExperience());
        assertEquals(new BigDecimal("500000"), response.getMinSalary());
        assertEquals(new BigDecimal("1200000"), response.getMaxSalary());
        assertEquals("Pune", response.getLocation());
        assertEquals(JobType.FULL_TIME, response.getJobType());
        assertEquals(JDStatus.ACTIVE, response.getStatus());
        assertEquals(entity.getCreatedAt(), response.getCreatedAt());
        assertEquals(entity.getUpdatedAt(), response.getUpdatedAt());
        assertNotSame(entity.getSkillsRequired(), response.getSkillsRequired());
    }

    @Test
    void toEntityShouldReturnNullWhenRequestIsNull() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    void toResponseDTOShouldReturnNullWhenEntityIsNull() {
        assertNull(mapper.toResponseDTO(null));
    }

    @Test
    void toEntityShouldSetEmptySkillsWhenSkillsAreNull() {
        JDRequestDTO request = new JDRequestDTO();
        request.setSkillsRequired(null);

        JobDescription entity = mapper.toEntity(request);

        assertNotNull(entity);
        assertNotNull(entity.getSkillsRequired());
        assertTrue(entity.getSkillsRequired().isEmpty());
    }

    @Test
    void toResponseDTOShouldSetEmptySkillsWhenSkillsAreNull() {
        JobDescription entity = new JobDescription();
        entity.setSkillsRequired(null);

        JDResponseDTO response = mapper.toResponseDTO(entity);

        assertNotNull(response);
        assertNotNull(response.getSkillsRequired());
        assertTrue(response.getSkillsRequired().isEmpty());
    }

    private JDRequestDTO createRequest() {
        JDRequestDTO request = new JDRequestDTO();
        request.setJobTitle("Java Developer");
        request.setJobDescription("Backend role");
        request.setSkillsRequired(List.of("Java", "Spring Boot"));
        request.setMinExperience(2);
        request.setMaxExperience(5);
        request.setMinSalary(new BigDecimal("500000"));
        request.setMaxSalary(new BigDecimal("1200000"));
        request.setLocation("Pune");
        request.setJobType(JobType.FULL_TIME);
        return request;
    }

    private JobDescription createEntity() {
        JobDescription entity = new JobDescription();
        entity.setId(UUID.randomUUID());
        entity.setJobTitle("Java Developer");
        entity.setJobDescription("Backend role");
        entity.setSkillsRequired(List.of("Java", "Spring Boot"));
        entity.setExperienceMin(2);
        entity.setExperienceMax(5);
        entity.setSalaryMin(new BigDecimal("500000"));
        entity.setSalaryMax(new BigDecimal("1200000"));
        entity.setLocation("Pune");
        entity.setJobType(JobType.FULL_TIME);
        entity.setStatus(JDStatus.ACTIVE);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }
}
