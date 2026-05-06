package com.interview_tracking_system.backend.controller;

import com.interview_tracking_system.backend.dto.ScheduleInterviewRequestDTO;
import com.interview_tracking_system.backend.dto.SubmitFeedbackRequestDTO;
import com.interview_tracking_system.backend.dto.UpdateCandidateStatusDTO;
import com.interview_tracking_system.backend.service.InterviewService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.interview_tracking_system.backend.entity.User;
import com.interview_tracking_system.backend.repository.UserRepository;
import org.springframework.security.core.Authentication;
import com.interview_tracking_system.backend.constants.ApiEndpoints;

import com.interview_tracking_system.backend.dto.PanelInterviewDTO;
import jakarta.validation.Valid;

import java.util.List;

/**
 * Controller for interview workflow APIs.
 */
@RestController
@RequestMapping(ApiEndpoints.INTERVIEW)
public class InterviewController {

    /** Logger instance. */
    private static final Logger LOGGER = LoggerFactory.getLogger(InterviewController.class);

    /** Interview service. */
    private final InterviewService interviewService;

    /** User repository. */
    private final UserRepository userRepository;

    /**
     * Constructor injection for interview service.
     *
     * @param interviewService service layer
     * @param userRepository   user repository
     */
    public InterviewController(final InterviewService interviewService, final UserRepository userRepository) {
        this.interviewService = interviewService;
        this.userRepository = userRepository;

    }

    /**
     * HR schedules interview for candidate.
     *
     * @param request schedule request
     */
    @PostMapping(ApiEndpoints.INTERVIEW_SCHEDULE)
    public void scheduleInterview(@Valid @RequestBody final ScheduleInterviewRequestDTO request) {

        LOGGER.info("Received request to schedule interview");

        interviewService.scheduleInterview(request);
    }

    /**
     * HR updates candidate status (reject / next stage).
     *
     * @param request update request
     */
    @PostMapping(ApiEndpoints.INTERVIEW_STATUS)
    public void updateCandidateStatus(@Valid @RequestBody final UpdateCandidateStatusDTO request) {

        LOGGER.info("Received request to update candidate status");

        interviewService.updateCandidateStatus(request);
    }

    /**
     * Panel submits feedback after interview.
     *
     * @param authentication spring security authentication
     * @param request        feedback request
     */
    @PostMapping(ApiEndpoints.INTERVIEW_FEEDBACK)
    public void submitFeedback(final Authentication authentication,
            @Valid @RequestBody final SubmitFeedbackRequestDTO request) {

        String email = authentication.getName();

        User panelUser = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("Panel not found"));

        LOGGER.info("Panel {} submitting feedback", panelUser.getId());

        interviewService.submitFeedback(panelUser.getId(), request);
    }

    /**
     * Get interviews assigned to panel.
     *
     * @param panelId panel id
     * @return list of interview ids
     */
    @GetMapping(ApiEndpoints.INTERVIEW_PANEL + "/{panelId}")
    public List<PanelInterviewDTO> getPanelInterviews(@PathVariable final Long panelId) {

        LOGGER.info("Fetching panel interviews for panel {}", panelId);

        return interviewService.getPanelInterviews(panelId);
    }

    /**
     * Get interviews for candidate.
     *
     * @param candidateId candidate id
     * @return list of interview ids
     */
    @GetMapping(ApiEndpoints.INTERVIEW_CANDIDATE + "/{candidateId}")
    public List<Long> getCandidateInterviews(@PathVariable final Long candidateId) {

        LOGGER.info("Fetching interviews for candidate {}", candidateId);

        return interviewService.getCandidateInterviews(candidateId);
    }
}
