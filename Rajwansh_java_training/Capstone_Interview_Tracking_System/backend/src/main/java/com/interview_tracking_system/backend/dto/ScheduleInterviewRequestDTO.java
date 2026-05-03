package com.interview_tracking_system.backend.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO used by HR to schedule an interview.
 */
public class ScheduleInterviewRequestDTO {

    /**
     * Candidate ID for whom interview is being scheduled.
     */
    @NotNull(message = "Candidate id is required")
    private Long candidateId;

    /**
     * Stage of interview (L1 or L2).
     */
    @NotBlank(message = "Interview stage is required")
    private String stage;

    /**
     * List of panel member IDs (min 1, max 2).
     */
    private List<Long> panelIds;

    /**
     * Scheduled date and time for interview.
     */
    @NotNull(message = "Interview time is required")
    @Future(message = "Interview time must be in future")
    private LocalDateTime interviewTime;

    /**
     * Focus areas provided by HR for panel evaluation.
     */
    @NotBlank(message = "Focus area is required")
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
        return panelIds == null ? null : new ArrayList<>(panelIds);
    }

    public void setPanelIds(List<Long> panelIds) {
        this.panelIds = panelIds == null ? null : new ArrayList<>(panelIds);
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
