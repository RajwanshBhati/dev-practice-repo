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
@Table(
    name = "interview",
    indexes = {
        @Index(name = "idx_candidate_id", columnList = "candidate_id")
    }
)
public class Interview {

    /** Maximum length for the stage column. */
    private static final int STAGE_LENGTH = 50;

    /** Maximum length for the focus area column. */
    private static final int FOCUS_AREA_LENGTH = 255;

    /** Unique identifier for the interview. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Foreign key referencing the candidate for this interview. */
    @Column(name = "candidate_id", nullable = false)
    private Long candidateId;

    /** Current stage of the interview. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = STAGE_LENGTH)
    private Stage stage;

    /** Scheduled date of the interview. */
    @Column(nullable = false)
    private LocalDate date;

    /** Scheduled time of the interview. */
    @Column(nullable = false)
    private LocalTime time;

    /** Focus area such as DSA or System Design. */
    @Column(length = FOCUS_AREA_LENGTH)
    private String focusArea;

    /**
     * Default constructor for JPA.
     */
    public Interview() {
    }

    /**
     * Returns the interview id.
     */
    public Long getId() {
        return id;
    }

    /**
     * Returns the foreign key ID of the associated candidate.
     */
    public Long getCandidateId() {
        return candidateId;
    }

    /**
     * Sets the foreign key ID of the associated candidate.
     */
    public void setCandidateId(final Long interviewCandidateId) {
        this.candidateId = interviewCandidateId;
    }

    /**
     * Returns the interview stage.
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Sets the interview stage.
     */
    public void setStage(final Stage interviewStage) {
        this.stage = interviewStage;
    }

    /**
     * Returns the interview date.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Sets the interview date.
     */
    public void setDate(final LocalDate interviewDate) {
        this.date = interviewDate;
    }

    /**
     * Returns the interview time.
     */
    public LocalTime getTime() {
        return time;
    }

    /**
     * Sets the interview time.
     */
    public void setTime(final LocalTime interviewTime) {
        this.time = interviewTime;
    }

    /**
     * Returns the focus area.
     */
    public String getFocusArea() {
        return focusArea;
    }

    /**
     * Sets the focus area.
     */
    public void setFocusArea(final String area) {
        this.focusArea = area;
    }
}