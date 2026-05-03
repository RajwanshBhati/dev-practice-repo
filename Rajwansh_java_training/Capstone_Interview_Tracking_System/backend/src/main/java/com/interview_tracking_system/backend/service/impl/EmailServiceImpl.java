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

        /**
         * Activation page base URL.
         */
        private static final String ACTIVATION_BASE_URL = "http://127.0.0.1:5500/Rajwansh_java_training/"
                        + "Capstone_Interview_Tracking_System/frontend/pages/"
                        + "activate.html?token=";

        /**
         * Recruitment team name.
         */
        private static final String RECRUITMENT_TEAM = "Recruitment Team";

        /**
         * TalentBridge team name.
         */
        private static final String TALENT_BRIDGE_TEAM = "TalentBridge Team";

        /**
         * Logger instance.
         */
        private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);

        /**
         * Java mail sender.
         */
        private final JavaMailSender mailSender;

        /**
         * Constructs email service implementation.
         *
         * @param injectedMailSender spring mail sender
         */
        public EmailServiceImpl(final JavaMailSender injectedMailSender) {
                this.mailSender = injectedMailSender;
        }

        /**
         * Sends profiling completion email.
         *
         * @param toEmail       receiver email
         * @param candidateName candidate name
         */
        @Override
        public void sendProfilingCompletedEmail(
                        final String toEmail,
                        final String candidateName) {

                SimpleMailMessage message = new SimpleMailMessage();

                message.setTo(toEmail);
                message.setSubject("Profile Submitted Successfully");

                String emailBody = String.format(
                                "Dear %s,%n%n"
                                                + "We are pleased to inform you that your profile has "
                                                + "been successfully submitted.%n%n"
                                                + "Our recruitment team is currently reviewing your "
                                                + "application. If your profile matches our "
                                                + "requirements, we will reach out to you for the next "
                                                + "steps.%n%n"
                                                + "We appreciate your interest in joining our "
                                                + "organization.%n%n"
                                                + "Warm regards,%n"
                                                + "%s",
                                candidateName,
                                RECRUITMENT_TEAM);

                message.setText(emailBody);

                mailSender.send(message);

                LOGGER.info("Profiling email sent to {}", toEmail);
        }

        /**
         * Sends panel activation email.
         *
         * @param toEmail        receiver email
         * @param fullName       panel full name
         * @param activationLink activation token
         */
        @Override
        public void sendPanelActivationEmail(
                        final String toEmail,
                        final String fullName,
                        final String activationLink) {

                SimpleMailMessage message = new SimpleMailMessage();

                message.setTo(toEmail);
                message.setSubject("Panel Account Activation");

                String activationUrl = ACTIVATION_BASE_URL + activationLink;

                String emailBody = String.format(
                                "Dear %s,%n%n"
                                                + "You have been added as a panel member.%n%n"
                                                + "Please click the below link to set your password:%n%n"
                                                + "%s%n%n"
                                                + "This link is valid for 24 hours.%n%n"
                                                + "After setting your password, you can login using "
                                                + "your email and new password.%n%n"
                                                + "Best regards,%n"
                                                + "%s",
                                fullName,
                                activationUrl,
                                RECRUITMENT_TEAM);

                message.setText(emailBody);

                mailSender.send(message);

                LOGGER.info("Panel activation mail sent to {}", toEmail);
        }

        /**
         * Sends interview schedule email to candidate.
         *
         * @param toEmail           receiver email
         * @param candidateName     candidate name
         * @param stage             interview stage
         * @param interviewDateTime interview date and time
         */
        @Override
        public void sendCandidateInterviewScheduleEmail(
                        final String toEmail,
                        final String candidateName,
                        final String stage,
                        final String interviewDateTime) {

                SimpleMailMessage message = new SimpleMailMessage();

                message.setTo(toEmail);
                message.setSubject("Interview Scheduled");

                String emailBody = String.format(
                                "Dear %s,%n%n"
                                                + "We are pleased to inform you that your interview "
                                                + "has been scheduled.%n%n"
                                                + "Interview Details:%n"
                                                + "---------------------------------%n"
                                                + "Stage        : %s%n"
                                                + "Date & Time  : %s%n"
                                                + "---------------------------------%n%n"
                                                + "Kindly ensure your availability at the scheduled "
                                                + "time.%n"
                                                + "Please reach out to us in case of any queries.%n%n"
                                                + "We wish you all the best for your interview.%n%n"
                                                + "Warm regards,%n"
                                                + "%s",
                                candidateName,
                                stage,
                                interviewDateTime,
                                RECRUITMENT_TEAM);

                message.setText(emailBody);

                mailSender.send(message);

                LOGGER.info("Interview mail sent to candidate {}", toEmail);
        }

        /**
         * Sends interview assignment email to panel.
         *
         * @param toEmail           receiver email
         * @param panelName         panel name
         * @param candidateName     candidate name
         * @param stage             interview stage
         * @param interviewDateTime interview date and time
         */
        @Override
        public void sendPanelInterviewAssignmentEmail(
                        final String toEmail,
                        final String panelName,
                        final String candidateName,
                        final String stage,
                        final String interviewDateTime) {

                SimpleMailMessage message = new SimpleMailMessage();

                message.setTo(toEmail);
                message.setSubject("Interview Assigned");

                String emailBody = String.format(
                                "Dear %s,%n%n"
                                                + "You have been assigned to conduct an interview as "
                                                + "per the details below:%n%n"
                                                + "Interview Details:%n"
                                                + "---------------------------------%n"
                                                + "Candidate    : %s%n"
                                                + "Stage        : %s%n"
                                                + "Date & Time  : %s%n"
                                                + "---------------------------------%n%n"
                                                + "Kindly review the candidate's profile before the "
                                                + "interview and share your feedback post evaluation.%n%n"
                                                + "Thank you for your support.%n%n"
                                                + "Best regards,%n"
                                                + "%s",
                                panelName,
                                candidateName,
                                stage,
                                interviewDateTime,
                                RECRUITMENT_TEAM);

                message.setText(emailBody);

                mailSender.send(message);

                LOGGER.info("Interview assignment mail sent to panel {}", toEmail);
        }

        /**
         * Sends candidate onboarding email.
         *
         * @param toEmail         receiver email
         * @param candidateName   candidate name
         * @param activationToken activation token
         */
        @Override
        public void sendCandidateOnboardEmail(
                        final String toEmail,
                        final String candidateName,
                        final String activationToken) {

                SimpleMailMessage message = new SimpleMailMessage();

                String activationUrl = ACTIVATION_BASE_URL + activationToken;

                message.setTo(toEmail);
                message.setSubject("Activate Your TalentBridge Account");

                String emailBody = String.format(
                                "Dear %s,%n%n"
                                                + "You have been onboarded by the HR team on "
                                                + "TalentBridge.%n%n"
                                                + "Login Email: %s%n%n"
                                                + "Please click the link below to activate your "
                                                + "account and set your password:%n%n"
                                                + "%s%n%n"
                                                + "This link is valid for 24 hours.%n%n"
                                                + "After setting your password, you can login using "
                                                + "your email and new password.%n%n"
                                                + "Best regards,%n"
                                                + "%s",
                                candidateName,
                                toEmail,
                                activationUrl,
                                TALENT_BRIDGE_TEAM);

                message.setText(emailBody);

                mailSender.send(message);
        }

        /**
         * Sends candidate registration email.
         *
         * @param toEmail         receiver email
         * @param candidateName   candidate name
         * @param activationToken activation token
         */
        @Override
        public void sendCandidateRegistrationEmail(
                        final String toEmail,
                        final String candidateName,
                        final String activationToken) {

                SimpleMailMessage message = new SimpleMailMessage();

                String activationUrl = ACTIVATION_BASE_URL + activationToken;

                message.setTo(toEmail);
                message.setSubject("Activate Your TalentBridge Account");

                String emailBody = String.format(
                                "Dear %s,%n%n"
                                                + "Your account has been successfully created on "
                                                + "TalentBridge.%n%n"
                                                + "Login Email: %s%n%n"
                                                + "Please click the link below to activate your "
                                                + "account and set your password:%n%n"
                                                + "%s%n%n"
                                                + "This link is valid for 24 hours.%n%n"
                                                + "After activation, you can login and start applying "
                                                + "for jobs.%n%n"
                                                + "If you did not register, please ignore this email.%n%n"
                                                + "Best regards,%n"
                                                + "%s",
                                candidateName,
                                toEmail,
                                activationUrl,
                                TALENT_BRIDGE_TEAM);

                message.setText(emailBody);

                mailSender.send(message);
        }
}
