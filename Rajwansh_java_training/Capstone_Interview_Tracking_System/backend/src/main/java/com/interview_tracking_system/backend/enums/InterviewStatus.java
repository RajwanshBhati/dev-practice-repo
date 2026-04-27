package com.interview_tracking_system.backend.enums;

/**
 * Represents the current status of an interview schedule.
 */
public enum InterviewStatus {

    /**
     * Interview is scheduled and waiting for panel action.
     */
    SCHEDULED,

    /**
     * Panel has submitted feedback for the interview.
     */
    FEEDBACK_SUBMITTED,

    /**
     * Interview was cancelled by HR.
     */
    CANCELLED
}
