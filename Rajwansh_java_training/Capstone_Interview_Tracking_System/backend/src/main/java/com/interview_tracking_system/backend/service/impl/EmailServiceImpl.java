package com.interview_tracking_system.backend.service.impl;

import com.interview_tracking_system.backend.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Implementation of EmailService for sending email notifications.
 */
@Service
public class EmailServiceImpl implements EmailService {

    /** Logger for this class. */
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);

    /** Subject for profiling completed email. */
    private static final String PROFILING_SUBJECT = "Application Received - Profiling Completed";

    /** Body template for profiling completed email. */
    private static final String PROFILING_BODY_TEMPLATE = "Dear %s,\n\nYour application has been successfully submitted."
            + "\nYour profiling is now marked as Completed."
            + "\nOur HR team will review your profile and get back to you.\n\nRegards,\nHR Team";

    /** The mail sender instance. */
    private final JavaMailSender mailSender;

    /**
     * Constructs EmailServiceImpl with required dependencies.
     *
     * @param sender the JavaMailSender to use
     */
    public EmailServiceImpl(final JavaMailSender sender) {
        this.mailSender = sender;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendProfilingCompletedEmail(final String toEmail, final String candidateName) {
        LOGGER.info("Sending profiling completed email to: {}", toEmail);
        final SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(PROFILING_SUBJECT);
        message.setText(String.format(PROFILING_BODY_TEMPLATE, candidateName));
        mailSender.send(message);
        LOGGER.info("Profiling completed email sent successfully to: {}", toEmail);
    }
}
