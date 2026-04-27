package com.interview_tracking_system.backend.service;

/**
 * Service interface for sending email notifications.
 */
public interface EmailService {

    /**
     * Sends a profiling completed email to the candidate.
     *
     * @param toEmail       the recipient email address
     * @param candidateName the name of the candidate
     */
    void sendProfilingCompletedEmail(String toEmail, String candidateName);

    /**
     * Sends panel activation email.
     *
     * @param toEmail        panel email
     * @param fullName       panel full name
     * @param activationLink activation URL
     */
    void sendPanelActivationEmail(String toEmail, String fullName, String activationLink);

    /**
     * Sends interview schedule email to candidate.
     *
     * @param toEmail           candidate email
     * @param candidateName     candidate name
     * @param stage             interview stage
     * @param interviewDateTime scheduled interview date and time
     */
    void sendCandidateInterviewScheduleEmail(String toEmail,
            String candidateName,
            String stage,
            String interviewDateTime);

    /**
     * Sends interview assignment email to panel member.
     *
     * @param toEmail           panel email
     * @param panelName         panel full name
     * @param candidateName     candidate name
     * @param stage             interview stage
     * @param interviewDateTime scheduled interview date and time
     */
    void sendPanelInterviewAssignmentEmail(String toEmail,
            String panelName,
            String candidateName,
            String stage,
            String interviewDateTime);
}
