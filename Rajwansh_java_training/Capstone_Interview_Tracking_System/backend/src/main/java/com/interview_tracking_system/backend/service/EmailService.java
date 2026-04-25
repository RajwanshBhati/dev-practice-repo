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
}
