package com.interview_tracking_system.backend.services.impl;

/**
 * Static imports for assertions and Mockito methods.
 */
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * DTO imports used for JD service testing.
 */
import com.interview_tracking_system.backend.dto.JDRequestDTO;
import com.interview_tracking_system.backend.dto.JDResponseDTO;

/**
 * Entity import used for JD test data.
 */
import com.interview_tracking_system.backend.entity.JobDescription;

/**
 * Enum imports used for JD status and job type.
 */
import com.interview_tracking_system.backend.enums.JDStatus;
import com.interview_tracking_system.backend.enums.JobType;

/**
 * Exception imports used for validation testing.
 */
import com.interview_tracking_system.backend.exception.InvalidRequestException;
import com.interview_tracking_system.backend.exception.ResourceNotFoundException;

/**
 * Mapper import used for DTO and entity conversion.
 */
import com.interview_tracking_system.backend.mapper.JDMapper;

/**
 * Repository import used for mocking database calls.
 */
import com.interview_tracking_system.backend.repository.JobDescriptionRepository;
import com.interview_tracking_system.backend.service.impl.JDServiceImpl;

/**
 * Java utility imports.
 */
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * JUnit imports used for testing.
 */
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Mockito imports used for mocking dependencies.
 */
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * This class tests JDServiceImpl.
 *
 * It verifies JD create, update, search, status update,
 * delete and validation failure scenarios.
 */
@ExtendWith(MockitoExtension.class)
class JDServiceImplTest {

    /**
     * Mocked JD repository.
     */
    @Mock
    private JobDescriptionRepository repository;

    /**
     * Service under test.
     */
    private JDServiceImpl service;

    /**
     * Initializes service before each test.
     */
    @BeforeEach
    void setUp() {
        service = new JDServiceImpl(repository, new JDMapper());
    }

    /**
     * Tests positive JD service flow.
     */
    @Test
    void createUpdateSearchAndDeleteShouldWork() {
        JDRequestDTO request = request();

        JobDescription saved = new JDMapper().toEntity(request);
        saved.setId(UUID.randomUUID());
        saved.setStatus(JDStatus.ACTIVE);

        when(repository.save(any(JobDescription.class)))
                .thenReturn(saved);

        when(repository.findById(saved.getId()))
                .thenReturn(Optional.of(saved));

        when(repository.findAllByOrderByCreatedAtDesc())
                .thenReturn(List.of(saved));

        when(repository.findByStatusOrderByCreatedAtDesc(JDStatus.ACTIVE))
                .thenReturn(List.of(saved));

        when(repository.searchJDs(JDStatus.ACTIVE, JobType.FULL_TIME, "Pune", "Java"))
                .thenReturn(List.of(saved));

        JDResponseDTO created = service.createJD(request);

        assertEquals("Java Developer", created.getJobTitle());
        assertEquals("Java Developer", service.getJDById(saved.getId()).getJobTitle());
        assertEquals(1, service.getAllJDs().size());
        assertEquals(1, service.getActiveJDs().size());

        assertEquals(
                1,
                service.searchJDs(JDStatus.ACTIVE, JobType.FULL_TIME, "Pune", "Java").size());

        assertEquals(
                JDStatus.CLOSED,
                service.updateJDStatus(saved.getId(), JDStatus.CLOSED).getStatus());

        assertEquals(
                "Java Developer",
                service.updateJD(saved.getId(), request).getJobTitle());

        service.deleteJD(saved.getId());

        verify(repository).delete(saved);
    }

    /**
     * Tests validation failures and missing JD scenarios.
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
     * Creates test JD request data.
     *
     * @return test JD request
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
