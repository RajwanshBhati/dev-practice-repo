package com.interview_tracking_system.backend.controller;

import com.interview_tracking_system.backend.dto.ScheduleInterviewRequestDTO;
import com.interview_tracking_system.backend.dto.SubmitFeedbackRequestDTO;
import com.interview_tracking_system.backend.dto.UpdateCandidateStatusDTO;
import com.interview_tracking_system.backend.service.InterviewService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for interview workflow APIs.
 */
@RestController
@RequestMapping("/api/interview")
public class InterviewController {

    private static final Logger LOGGER = LoggerFactory.getLogger(InterviewController.class);

    private final InterviewService interviewService;

    /**
     * Constructor injection for interview service.
     *
     * @param interviewService service layer
     */
    public InterviewController(final InterviewService interviewService) {
        this.interviewService = interviewService;
    }

    /**
     * HR schedules interview for candidate.
     *
     * @param request schedule request
     */
    @PostMapping("/schedule")
    public void scheduleInterview(@RequestBody final ScheduleInterviewRequestDTO request) {

        LOGGER.info("Received request to schedule interview");

        interviewService.scheduleInterview(request);
    }

    /**
     * HR updates candidate status (reject / next stage).
     *
     * @param request update request
     */
    @PostMapping("/status")
    public void updateCandidateStatus(@RequestBody final UpdateCandidateStatusDTO request) {

        LOGGER.info("Received request to update candidate status");

        interviewService.updateCandidateStatus(request);
    }

    /**
     * Panel submits feedback after interview.
     *
     * @param panelId panel id (from frontend/session)
     * @param request feedback request
     */
    @PostMapping("/feedback/{panelId}")
    public void submitFeedback(@PathVariable final Long panelId,
            @RequestBody final SubmitFeedbackRequestDTO request) {

        LOGGER.info("Panel {} submitting feedback", panelId);

        interviewService.submitFeedback(panelId, request);
    }

    /**
     * Get interviews assigned to panel.
     *
     * @param panelId panel id
     * @return list of interview ids
     */
    @GetMapping("/panel/{panelId}")
    public List<Long> getPanelInterviews(@PathVariable final Long panelId) {

        LOGGER.info("Fetching panel interviews for panel {}", panelId);

        return interviewService.getPanelInterviews(panelId);
    }

    /**
     * Get interviews for candidate.
     *
     * @param candidateId candidate id
     * @return list of interview ids
     */
    @GetMapping("/candidate/{candidateId}")
    public List<Long> getCandidateInterviews(@PathVariable final Long candidateId) {

        LOGGER.info("Fetching interviews for candidate {}", candidateId);

        return interviewService.getCandidateInterviews(candidateId);
    }
}
