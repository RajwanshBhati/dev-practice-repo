package com.interview_tracking_system.backend.service;

import com.interview_tracking_system.backend.dto.ScheduleInterviewRequestDTO;
import com.interview_tracking_system.backend.dto.SubmitFeedbackRequestDTO;
import com.interview_tracking_system.backend.dto.UpdateCandidateStatusDTO;

import java.util.List;

/**
 * Service interface for handling interview workflow.
 */
public interface InterviewService {

    /**
     * HR schedules interview for candidate (L1 / L2).
     *
     * @param request request DTO
     */
    void scheduleInterview(ScheduleInterviewRequestDTO request);

    /**
     * HR updates candidate stage or decision.
     *
     * @param request request DTO
     */
    void updateCandidateStatus(UpdateCandidateStatusDTO request);

    /**
     * Panel submits feedback after interview.
     *
     * @param panelId panel id (from logged in user)
     * @param request request DTO
     */
    void submitFeedback(Long panelId, SubmitFeedbackRequestDTO request);

    /**
     * Fetch interviews assigned to a panel.
     *
     * @param panelId panel id
     * @return list of interview ids (or simple response)
     */
    List<Long> getPanelInterviews(Long panelId);

    /**
     * Fetch interview details for a candidate.
     *
     * @param candidateId candidate id
     * @return list of interview ids (or simple response)
     */
    List<Long> getCandidateInterviews(Long candidateId);
}
