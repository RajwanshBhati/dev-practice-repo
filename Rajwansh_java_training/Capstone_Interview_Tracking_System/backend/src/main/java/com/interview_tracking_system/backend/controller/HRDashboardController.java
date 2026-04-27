package com.interview_tracking_system.backend.controller;

import com.interview_tracking_system.backend.dto.HRCandidateFullDTO;
import com.interview_tracking_system.backend.service.HRDashboardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.interview_tracking_system.backend.dto.HRFeedbackDTO;
import com.interview_tracking_system.backend.service.InterviewService;
import org.springframework.web.bind.annotation.PathVariable;
import com.interview_tracking_system.backend.constants.ApiEndpoints;

import java.util.List;

/**
 * Controller for HR dashboard operations.
 * Provides endpoints for HR candidate management view.
 */
@RestController
@RequestMapping(ApiEndpoints.SHOWHR)
public class HRDashboardController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HRDashboardController.class);

    private final HRDashboardService hrDashboardService;
    private final InterviewService interviewService;

    /**
     * Constructor injection.
     */
    public HRDashboardController(final HRDashboardService hrDashboardService,
            final InterviewService interviewService) {
        this.hrDashboardService = hrDashboardService;
        this.interviewService = interviewService;
    }

    /**
     * Fetch all candidates for HR dashboard.
     *
     * @return list of candidate details
     */
    @GetMapping(ApiEndpoints.SHOWCANDIDATE)
    public ResponseEntity<List<HRCandidateFullDTO>> getCandidates() {

        LOGGER.info("HR dashboard request received for candidate list");

        List<HRCandidateFullDTO> response = hrDashboardService.getAllCandidatesForHR();

        return ResponseEntity.ok(response);
    }

    /**
     * Fetches panel feedback for a candidate.
     *
     * @param candidateId candidate id
     * @return feedback list
     */
    @GetMapping(ApiEndpoints.SHOWFEED + "/{candidateId}")
    public List<HRFeedbackDTO> getCandidateFeedback(@PathVariable final Long candidateId) {

        LOGGER.info("Fetching feedback for candidate {}", candidateId);

        return interviewService.getFeedbackForCandidate(candidateId);
    }
}
