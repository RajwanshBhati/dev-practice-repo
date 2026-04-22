package com.interview_tracking_system.backend.entity;

import com.interview_tracking_system.backend.enums.FeedbackStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Represents feedback submitted by a panel member for an interview.
 */
@Entity
@Table(name = "feedback")
public class Feedback {

    /** Maximum length for comments field. */
    private static final int COMMENTS_LENGTH = 1000;

    /**
     * Unique identifier for the feedback.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Foreign key referencing the interview this feedback belongs to. */
    @Column(name = "interview_id", nullable = false)
    private Long interviewId;

    /** Foreign key referencing the panel member who submitted this feedback. */
    @Column(name = "panel_id", nullable = false)
    private Long panelId;

    /** General comments about the candidate. */
    @Column(length = COMMENTS_LENGTH)
    private String comments;

    /** Strengths observed in the candidate. */
    private String strength;

    /** Weaknesses observed in the candidate. */
    private String weakness;

    /** Numeric rating given by the panel member. */
    @Column(nullable = false)
    private int rating;

    /** Current status of the feedback. */
    @Enumerated(EnumType.STRING)
    private FeedbackStatus status;

    /**
     * Default constructor required by JPA.
     */
    public Feedback() {
    }

    /**
     * Returns the unique identifier.
     */
    public Long getId() {
        return id;
    }

    /**
     * Returns the foreign key ID of the associated interview.
     */
    public Long getInterviewId() {
        return interviewId;
    }

    /**
     * Sets the foreign key ID of the associated interview.
     */
    public void setInterviewId(final Long interviewRef) {
        this.interviewId = interviewRef;
    }

    /**
     * Returns the foreign key ID of the panel member.
     */
    public Long getPanelId() {
        return panelId;
    }

    /**
     * Sets the foreign key ID of the panel member.
     */
    public void setPanelId(final Long panelRef) {
        this.panelId = panelRef;
    }

    /**
     * Returns the comments.
     */
    public String getComments() {
        return comments;
    }

    /**
     * Sets the comments.
     */
    public void setComments(final String feedbackComments) {
        this.comments = feedbackComments;
    }

    /**
     * Returns the candidate's strengths.
     */
    public String getStrength() {
        return strength;
    }

    /**
     * Sets the candidate's strengths.
     */
    public void setStrength(final String candidateStrength) {
        this.strength = candidateStrength;
    }

    /**
     * Returns the candidate's weaknesses.
     */
    public String getWeakness() {
        return weakness;
    }

    /**
     * Sets the candidate's weaknesses.
     */
    public void setWeakness(final String candidateWeakness) {
        this.weakness = candidateWeakness;
    }

    /**
     * Returns the numeric rating.
     */
    public int getRating() {
        return rating;
    }

    /**
     * Sets the numeric rating.
     */
    public void setRating(final int feedbackRating) {
        this.rating = feedbackRating;
    }

    /**
     * Returns the feedback status.
     */
    public FeedbackStatus getStatus() {
        return status;
    }

    /**
     * Sets the feedback status.
     */
    public void setStatus(final FeedbackStatus feedbackStatus) {
        this.status = feedbackStatus;
    }
}