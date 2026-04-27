package com.interview_tracking_system.backend.service.impl;

import com.interview_tracking_system.backend.service.EmailService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Handles all email related operations.
 */
@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final JavaMailSender mailSender;

    /**
     * Constructor injection for mail sender.
     *
     * @param mailSender spring mail sender
     */
    public EmailServiceImpl(final JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Sends profiling completion email.
     */
    @Override
    public void sendProfilingCompletedEmail(final String toEmail, final String candidateName) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(toEmail);
        message.setSubject("Profile Submitted Successfully");

        message.setText(
                "Hi " + candidateName + ",\n\n"
                        + "Your profile has been successfully submitted.\n"
                        + "Our HR team will review your application soon.\n\n"
                        + "Thanks,\nRecruitment Team");

        mailSender.send(message);

        LOGGER.info("Profiling email sent to {}", toEmail);
    }

    /**
     * Sends panel activation email.
     */
    @Override
    public void sendPanelActivationEmail(final String toEmail,
            final String fullName,
            final String activationLink) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(toEmail);
        message.setSubject("Panel Account Activation");

        message.setText(
                "Hi " + fullName + ",\n\n"
                        + "You have been added as a panel member.\n"
                        + "Please activate your account using the link below:\n\n"
                        + activationLink + "\n\n"
                        + "Thanks,\nRecruitment Team");

        mailSender.send(message);

        LOGGER.info("Panel activation mail sent to {}", toEmail);
    }

    /**
     * Sends interview schedule mail to candidate.
     */
    @Override
    public void sendCandidateInterviewScheduleEmail(final String toEmail,
            final String candidateName,
            final String stage,
            final String interviewDateTime) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(toEmail);
        message.setSubject("Interview Scheduled");

        message.setText(
                "Hi " + candidateName + ",\n\n"
                        + "Your interview has been scheduled.\n\n"
                        + "Stage: " + stage + "\n"
                        + "Date & Time: " + interviewDateTime + "\n\n"
                        + "Please be available on time.\n\n"
                        + "Best of luck!\nRecruitment Team");

        mailSender.send(message);

        LOGGER.info("Interview mail sent to candidate {}", toEmail);
    }

    /**
     * Sends interview assignment mail to panel.
     */
    @Override
    public void sendPanelInterviewAssignmentEmail(final String toEmail,
            final String panelName,
            final String candidateName,
            final String stage,
            final String interviewDateTime) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(toEmail);
        message.setSubject("Interview Assigned");

        message.setText(
                "Hi " + panelName + ",\n\n"
                        + "You have been assigned an interview.\n\n"
                        + "Candidate: " + candidateName + "\n"
                        + "Stage: " + stage + "\n"
                        + "Date & Time: " + interviewDateTime + "\n\n"
                        + "Please review candidate details before interview.\n\n"
                        + "Thanks,\nRecruitment Team");

        mailSender.send(message);

        LOGGER.info("Interview assignment mail sent to panel {}", toEmail);
    }
}
