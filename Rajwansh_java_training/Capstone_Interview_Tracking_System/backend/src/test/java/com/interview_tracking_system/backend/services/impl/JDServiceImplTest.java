package com.interview_tracking_system.backend.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.interview_tracking_system.backend.dto.JDRequestDTO;
import com.interview_tracking_system.backend.dto.JDResponseDTO;
import com.interview_tracking_system.backend.entity.JobDescription;
import com.interview_tracking_system.backend.enums.JDStatus;
import com.interview_tracking_system.backend.enums.JobType;
import com.interview_tracking_system.backend.exception.InvalidRequestException;
import com.interview_tracking_system.backend.exception.ResourceNotFoundException;
import com.interview_tracking_system.backend.mapper.JDMapper;
import com.interview_tracking_system.backend.repository.JobDescriptionRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * This class tests JDServiceImpl.
 */
@ExtendWith(MockitoExtension.class)
class JDServiceImplTest {

    /**
     * Mocked repository.
     */
    @Mock
    private JobDescriptionRepository repository;

    /**
     * Service under test.
     */
    private com.interview_tracking_system.backend.service.impl.JDServiceImpl service;

    /**
     * Initializes test setup.
     */
    @BeforeEach
    void setUp() {
        service = new com.interview_tracking_system.backend.service.impl.JDServiceImpl(
                repository, new JDMapper());
    }

    /**
     * Tests positive JD operations.
     */
    @Test
    void createUpdateSearchAndDeleteShouldWork() {

        JDRequestDTO request = request();
        UUID jdId = UUID.randomUUID();

        // FIX: createJD uses saveAndFlush, so mock saveAndFlush (not save)
        when(repository.saveAndFlush(any(JobDescription.class)))
                .thenAnswer(invocation -> {
                    JobDescription jd = invocation.getArgument(0);
                    if (jd.getId() == null) {
                        jd.setId(jdId);
                    }
                    if (jd.getStatus() == null) {
                        jd.setStatus(JDStatus.ACTIVE);
                    }
                    return jd;
                });

        JDResponseDTO created = service.createJD(request);

        assertEquals("Java Developer", created.getJobTitle());

        // Build the saved entity for subsequent mocks
        JobDescription saved = new JDMapper().toEntity(request);
        saved.setId(jdId);
        saved.setStatus(JDStatus.ACTIVE);

        // FIX: updateJD, updateJDStatus use save(), so mock save separately
        when(repository.save(any(JobDescription.class)))
                .thenAnswer(invocation -> {
                    JobDescription jd = invocation.getArgument(0);
                    if (jd.getId() == null) {
                        jd.setId(jdId);
                    }
                    if (jd.getStatus() == null) {
                        jd.setStatus(JDStatus.ACTIVE);
                    }
                    return jd;
                });

        when(repository.findById(jdId))
                .thenReturn(Optional.of(saved));

        when(repository.findAllByOrderByCreatedAtDesc())
                .thenReturn(List.of(saved));

        when(repository.findByStatusOrderByCreatedAtDesc(JDStatus.ACTIVE))
                .thenReturn(List.of(saved));

        when(repository.searchJDs(
                JDStatus.ACTIVE,
                JobType.FULL_TIME,
                "Pune",
                "Java"))
                .thenReturn(List.of(saved));

        assertEquals(
                "Java Developer",
                service.getJDById(jdId).getJobTitle());

        assertEquals(
                1,
                service.getAllJDs().size());

        assertEquals(
                1,
                service.getActiveJDs().size());

        assertEquals(
                1,
                service.searchJDs(
                        JDStatus.ACTIVE,
                        JobType.FULL_TIME,
                        "Pune",
                        "Java").size());

        assertEquals(
                JDStatus.CLOSED,
                service.updateJDStatus(jdId, JDStatus.CLOSED).getStatus());

        assertEquals(
                "Java Developer",
                service.updateJD(jdId, request).getJobTitle());

        service.deleteJD(jdId);

        verify(repository).delete(saved);
    }

    /**
     * Tests invalid ranges and missing JD.
     */
    @Test
    void invalidRangesAndMissingJDShouldThrow() {

        JDRequestDTO invalidExp = request();
        invalidExp.setMinExperience(6);
        invalidExp.setMaxExperience(2);

        assertThrows(
                InvalidRequestException.class,
                () -> service.createJD(invalidExp));

        JDRequestDTO invalidSalary = request();
        invalidSalary.setMinSalary(new BigDecimal("200"));
        invalidSalary.setMaxSalary(new BigDecimal("100"));

        assertThrows(
                InvalidRequestException.class,
                () -> service.createJD(invalidSalary));

        UUID id = UUID.randomUUID();

        when(repository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.getJDById(id));
    }

    /**
     * Creates test request.
     *
     * @return request DTO
     */
    private JDRequestDTO request() {

        JDRequestDTO request = new JDRequestDTO();
        request.setJobTitle("Java Developer");
        request.setJobDescription("Backend role");
        request.setSkillsRequired(List.of("Java", "Spring"));
        request.setMinExperience(2);
        request.setMaxExperience(5);
        request.setMinSalary(new BigDecimal("500000"));
        request.setMaxSalary(new BigDecimal("1200000"));
        request.setLocation("Pune");
        request.setJobType(JobType.FULL_TIME);

        return request;
    }
}