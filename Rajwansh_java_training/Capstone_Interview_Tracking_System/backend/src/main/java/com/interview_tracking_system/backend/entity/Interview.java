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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    /** Unique identifier for interview. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Candidate associated with this interview. */
    @ManyToOne
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidate;

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
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Returns the associated candidate.
     *
     * @return the candidate
     */
    public Candidate getCandidate() {
        return candidate;
    }

    /**
     * Sets the associated candidate.
     *
     * @param interviewCandidate the candidate to set
     */
    public void setCandidate(final Candidate interviewCandidate) {
        this.candidate = interviewCandidate;
    }

    /**
     * Returns the interview stage.
     *
     * @return the stage
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Sets the interview stage.
     *
     * @param interviewStage the stage to set
     */
    public void setStage(final Stage interviewStage) {
        this.stage = interviewStage;
    }

    /**
     * Returns the interview date.
     *
     * @return the date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Sets the interview date.
     *
     * @param interviewDate the date to set
     */
    public void setDate(final LocalDate interviewDate) {
        this.date = interviewDate;
    }

    /**
     * Returns the interview time.
     *
     * @return the time
     */
    public LocalTime getTime() {
        return time;
    }

    /**
     * Sets the interview time.
     *
     * @param interviewTime the time to set
     */
    public void setTime(final LocalTime interviewTime) {
        this.time = interviewTime;
    }

    /**
     * Returns the focus area.
     *
     * @return the focus area
     */
    public String getFocusArea() {
        return focusArea;
    }

    /**
     * Sets the focus area.
     *
     * @param area the focus area to set
     */
    public void setFocusArea(final String area) {
        this.focusArea = area;
    }
}