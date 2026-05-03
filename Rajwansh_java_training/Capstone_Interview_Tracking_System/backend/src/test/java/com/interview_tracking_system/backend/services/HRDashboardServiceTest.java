package com.interview_tracking_system.backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.interview_tracking_system.backend.dto.HRCandidateFullDTO;
import com.interview_tracking_system.backend.entity.Candidate;
import com.interview_tracking_system.backend.entity.JobDescription;
import com.interview_tracking_system.backend.enums.Stage;
import com.interview_tracking_system.backend.repository.CandidateRepository;
import com.interview_tracking_system.backend.repository.JobDescriptionRepository;
import com.interview_tracking_system.backend.service.HRDashboardService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit test class for HRDashboardService.
 * Validates candidate and job description mapping logic.
 */
@ExtendWith(MockitoExtension.class)
class HRDashboardServiceTest {

    @Mock
    private CandidateRepository candidateRepository;

    @Mock
    private JobDescriptionRepository jobDescriptionRepository;

    /**
     * Tests retrieval of all candidates for HR and verifies
     * correct mapping of candidate and job description details.
     */
    @Test
    void getAllCandidatesForHRShouldMapCandidateAndJD() {

        UUID jdId = UUID.randomUUID();

        Candidate candidate = new Candidate();
        candidate.setId(1L);
        candidate.setName("Raj");
        candidate.setEmail("raj@test.com");
        candidate.setMobile("9876543210");
        candidate.setCurrentCompany("ABC");
        candidate.setCurrentCtc(new BigDecimal("5"));
        candidate.setExpectedCtc(new BigDecimal("8"));
        candidate.setTotalExp(5);
        candidate.setRelevantExp(4);
        candidate.setPreferredLocation("Pune");
        candidate.setNoticePeriod(30);
        candidate.setSource("LinkedIn");
        candidate.setResumeUrl("/api/resumes/r.pdf");
        candidate.setStatus(Stage.PROFILING);
        candidate.setJdId(jdId);

        JobDescription jd = new JobDescription();
        jd.setJobTitle("Java Developer");

        when(candidateRepository.findAllByOrderByIdDesc())
                .thenReturn(List.of(candidate));

        when(jobDescriptionRepository.findById(jdId))
                .thenReturn(Optional.of(jd));

        HRDashboardService service = new HRDashboardService(candidateRepository, jobDescriptionRepository);

        List<HRCandidateFullDTO> result = service.getAllCandidatesForHR();

        assertEquals(1, result.size());
        assertEquals("Java Developer", result.get(0).getJobTitle());
        assertEquals("PROFILING", result.get(0).getStatus());
    }
}
