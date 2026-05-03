package com.interview_tracking_system.backend.service;

import com.interview_tracking_system.backend.dto.HRCandidateFullDTO;
import com.interview_tracking_system.backend.entity.Candidate;
import com.interview_tracking_system.backend.repository.CandidateRepository;
import com.interview_tracking_system.backend.repository.JobDescriptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class for HR dashboard operations.
 */
@Service
public class HRDashboardService {

        /** Logger for this service. */
        private static final Logger LOGGER = LoggerFactory.getLogger(HRDashboardService.class);

        /** Repository for candidate data. */
        private final CandidateRepository candidateRepository;

        /** Repository for job description data. */
        private final JobDescriptionRepository jobDescriptionRepository;

        /**
         * Constructor for dependency injection.
         *
         * @param candidateRepository      candidate repository
         * @param jobDescriptionRepository job description repository
         */
        public HRDashboardService(final CandidateRepository candidateRepository,
                        final JobDescriptionRepository jobDescriptionRepository) {
                this.candidateRepository = candidateRepository;
                this.jobDescriptionRepository = jobDescriptionRepository;
        }

        /**
         * Fetches all candidates for HR dashboard view.
         *
         * @return list of candidate DTOs for HR
         */
        public List<HRCandidateFullDTO> getAllCandidatesForHR() {

                LOGGER.info("Fetching all candidates for HR dashboard");

                List<Candidate> candidates = candidateRepository.findAllByOrderByIdDesc();
                List<HRCandidateFullDTO> result = new ArrayList<>();

                for (Candidate c : candidates) {

                        HRCandidateFullDTO dto = new HRCandidateFullDTO();
                        dto.setId(c.getId());
                        dto.setName(c.getName());
                        dto.setEmail(c.getEmail());
                        dto.setMobileNumber(c.getMobile());

                        dto.setCurrentCompany(c.getCurrentCompany());

                        dto.setCurrentCtc(
                                        c.getCurrentCtc() != null
                                                        ? c.getCurrentCtc().doubleValue()
                                                        : null);

                        dto.setExpectedCtc(
                                        c.getExpectedCtc() != null
                                                        ? c.getExpectedCtc().doubleValue()
                                                        : null);

                        dto.setTotalExperience((int) c.getTotalExp());
                        dto.setRelevantExperience((int) c.getRelevantExp());

                        dto.setPreferredLocation(c.getPreferredLocation());
                        dto.setNoticePeriod(c.getNoticePeriod());
                        dto.setSource(c.getSource());
                        dto.setResumeUrl(c.getResumeUrl());

                        dto.setStatus(
                                        c.getStatus() != null
                                                        ? c.getStatus().name()
                                                        : null);

                        if (c.getJdId() != null) {
                                jobDescriptionRepository.findById(c.getJdId())
                                                .ifPresent(jd -> dto.setJobTitle(jd.getJobTitle()));
                        }

                        result.add(dto);
                }

                LOGGER.info("Total candidates fetched for HR dashboard: {}",
                                result.size());

                return result;
        }
}
