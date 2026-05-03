package com.interview_tracking_system.backend.entity;

import com.interview_tracking_system.backend.enums.Stage;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Represents an interview scheduled for a candidate.
 */
@Entity
@Table(name = "interview", indexes = {
        @Index(name = "idx_candidate_id", columnList = "candidate_id")
})
public class Interview {

    /** Maximum length for the stage column. */
    private static final int STAGE_LENGTH = 50;

    /** Maximum length for the focus area column. */
    private static final int FOCUS_AREA_LENGTH = 255;

    /** Unique identifier for the interview. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Candidate ID for this interview. */
    @Column(name = "candidate_id", nullable = false)
    private Long candidateId;

    /** Current stage of the interview. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = STAGE_LENGTH)
    private Stage stage;

    /** Scheduled interview date. */
    @Column(nullable = false)
    private LocalDate date;

    /** Scheduled interview time. */
    @Column(nullable = false)
    private LocalTime time;

    /** Focus area such as DSA or System Design. */
    @Column(length = FOCUS_AREA_LENGTH)
    private String focusArea;

    /**
     * Returns interview ID.
     *
     * @return interview ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets interview ID.
     *
     * @param interviewId interview ID
     */
    public void setId(final Long interviewId) {
        this.id = interviewId;
    }

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
     * @param candidateRef candidate ID
     */
    public void setCandidateId(final Long candidateRef) {
        this.candidateId = candidateRef;
    }

    /**
     * Returns interview stage.
     *
     * @return stage
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Sets interview stage.
     *
     * @param interviewStage stage
     */
    public void setStage(final Stage interviewStage) {
        this.stage = interviewStage;
    }

    /**
     * Returns interview date.
     *
     * @return date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Sets interview date.
     *
     * @param interviewDate date
     */
    public void setDate(final LocalDate interviewDate) {
        this.date = interviewDate;
    }

    /**
     * Returns interview time.
     *
     * @return time
     */
    public LocalTime getTime() {
        return time;
    }

    /**
     * Sets interview time.
     *
     * @param interviewTime time
     */
    public void setTime(final LocalTime interviewTime) {
        this.time = interviewTime;
    }

    /**
     * Returns focus area.
     *
     * @return focus area
     */
    public String getFocusArea() {
        return focusArea;
    }

    /**
     * Sets focus area.
     *
     * @param focus focus area
     */
    public void setFocusArea(final String focus) {
        this.focusArea = focus;
    }
}
