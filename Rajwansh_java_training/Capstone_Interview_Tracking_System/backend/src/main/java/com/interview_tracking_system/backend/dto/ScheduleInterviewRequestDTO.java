package com.interview_tracking_system.backend.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Request DTO used by HR to schedule an interview.
 */
public class ScheduleInterviewRequestDTO {

    /**
     * Candidate ID for whom interview is being scheduled.
     */
    private Long candidateId;

    /**
     * Stage of interview (L1 or L2).
     */
    private String stage;

    /**
     * List of panel member IDs (min 1, max 2).
     */
    private List<Long> panelIds;

    /**
     * Scheduled date and time for interview.
     */
    private LocalDateTime interviewTime;

    /**
     * Focus areas provided by HR for panel evaluation.
     */
    private String focusAreas;

    public Long getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(Long candidateId) {
        this.candidateId = candidateId;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public List<Long> getPanelIds() {
        return panelIds;
    }

    public void setPanelIds(List<Long> panelIds) {
        this.panelIds = panelIds;
    }

    public LocalDateTime getInterviewTime() {
        return interviewTime;
    }

    public void setInterviewTime(LocalDateTime interviewTime) {
        this.interviewTime = interviewTime;
    }

    public String getFocusAreas() {
        return focusAreas;
    }

    public void setFocusAreas(String focusAreas) {
        this.focusAreas = focusAreas;
    }
}
