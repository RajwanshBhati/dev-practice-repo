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

    /** Candidate ID for whom interview is being scheduled. */
    @NotNull(message = "Candidate id is required")
    private Long candidateId;

    /** Stage of interview (L1 or L2). */
    @NotBlank(message = "Interview stage is required")
    private String stage;

    /** List of panel member IDs (min 1, max 2). */
    private List<Long> panelIds;

    /** Scheduled date and time for interview. */
    @NotNull(message = "Interview time is required")
    @Future(message = "Interview time must be in future")
    private LocalDateTime interviewTime;

    /** Focus areas provided by HR for panel evaluation. */
    @NotBlank(message = "Focus area is required")
    private String focusAreas;

    /**
     * Returns candidate ID.
     *
     * @return candidate ID
     */
    public Long getCandidateId() {
        return candidateId;
    }

    /**
     * Sets candidate ID.
     *
     * @param id candidate ID
     */
    public void setCandidateId(final Long id) {
        this.candidateId = id;
    }

    /**
     * Returns interview stage.
     *
     * @return stage
     */
    public String getStage() {
        return stage;
    }

    /**
     * Sets interview stage.
     *
     * @param interviewStage stage
     */
    public void setStage(final String interviewStage) {
        this.stage = interviewStage;
    }

    /**
     * Returns panel IDs.
     *
     * @return panel IDs
     */
    public List<Long> getPanelIds() {
        return panelIds == null ? null : new ArrayList<>(panelIds);
    }

    /**
     * Sets panel IDs.
     *
     * @param ids panel IDs
     */
    public void setPanelIds(final List<Long> ids) {
        this.panelIds = ids == null ? null : new ArrayList<>(ids);
    }

    /**
     * Returns interview time.
     *
     * @return interview time
     */
    public LocalDateTime getInterviewTime() {
        return interviewTime;
    }

    /**
     * Sets interview time.
     *
     * @param time interview time
     */
    public void setInterviewTime(final LocalDateTime time) {
        this.interviewTime = time;
    }

    /**
     * Returns focus areas.
     *
     * @return focus areas
     */
    public String getFocusAreas() {
        return focusAreas;
    }

    /**
     * Sets focus areas.
     *
     * @param areas focus areas
     */
    public void setFocusAreas(final String areas) {
        this.focusAreas = areas;
    }
}
