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
                                "Dear " + candidateName + ",\n\n"
                                                + "We are pleased to inform you that your profile has been successfully submitted.\n\n"
                                                + "Our recruitment team is currently reviewing your application. "
                                                + "If your profile matches our requirements, we will reach out to you for the next steps.\n\n"
                                                + "We appreciate your interest in joining our organization.\n\n"
                                                + "Warm regards,\n"
                                                + "Recruitment Team");

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

                String activationUrl = "http://127.0.0.1:5500/Rajwansh_java_training/Capstone_Interview_Tracking_System/frontend/pages/activate.html?token="
                                + activationLink;

                message.setText(
                                "Dear " + fullName + ",\n\n"
                                                + "You have been added as a panel member.\n\n"
                                                + "Please click the below link to set your password:\n\n"
                                                + activationUrl + "\n\n"
                                                + "This link is valid for 24 hours.\n\n"
                                                + "After setting your password, you can login using your email and new password.\n\n"
                                                + "Best regards,\n"
                                                + "Recruitment Team");

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
                                "Dear " + candidateName + ",\n\n"
                                                + "We are pleased to inform you that your interview has been scheduled.\n\n"
                                                + "Interview Details:\n"
                                                + "---------------------------------\n"
                                                + "Stage        : " + stage + "\n"
                                                + "Date & Time  : " + interviewDateTime + "\n"
                                                + "---------------------------------\n\n"
                                                + "Kindly ensure your availability at the scheduled time.\n"
                                                + "Please reach out to us in case of any queries.\n\n"
                                                + "We wish you all the best for your interview.\n\n"
                                                + "Warm regards,\n"
                                                + "Recruitment Team");

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
                                "Dear " + panelName + ",\n\n"
                                                + "You have been assigned to conduct an interview as per the details below:\n\n"
                                                + "Interview Details:\n"
                                                + "---------------------------------\n"
                                                + "Candidate    : " + candidateName + "\n"
                                                + "Stage        : " + stage + "\n"
                                                + "Date & Time  : " + interviewDateTime + "\n"
                                                + "---------------------------------\n\n"
                                                + "Kindly review the candidate's profile before the interview and share your feedback post evaluation.\n\n"
                                                + "Thank you for your support.\n\n"
                                                + "Best regards,\n"
                                                + "Recruitment Team");

                mailSender.send(message);

                LOGGER.info("Interview assignment mail sent to panel {}", toEmail);
        }

        /**
         * Send Candiadte Onboard mail
         */
        @Override
        public void sendCandidateOnboardEmail(
                        final String toEmail,
                        final String candidateName,
                        final String activationToken) {

                SimpleMailMessage message = new SimpleMailMessage();

                String activationUrl = "http://127.0.0.1:5500/Rajwansh_java_training/Capstone_Interview_Tracking_System/frontend/pages/activate.html?token="
                                + activationToken;

                message.setTo(toEmail);
                message.setSubject("Activate Your TalentBridge Account");

                message.setText(
                                "Dear " + candidateName + ",\n\n"
                                                + "You have been onboarded by the HR team on TalentBridge.\n\n"
                                                + "Login Email: " + toEmail + "\n\n"
                                                + "Please click the link below to activate your account and set your password:\n\n"
                                                + activationUrl + "\n\n"
                                                + "This link is valid for 24 hours.\n\n"
                                                + "After setting your password, you can login using your email and new password.\n\n"
                                                + "Best regards,\n"
                                                + "TalentBridge Team");

                mailSender.send(message);
        }

        @Override
        public void sendCandidateRegistrationEmail(
                        final String toEmail,
                        final String candidateName,
                        final String activationToken) {

                SimpleMailMessage message = new SimpleMailMessage();

                String activationUrl = "http://127.0.0.1:5500/Rajwansh_java_training/Capstone_Interview_Tracking_System/frontend/pages/activate.html?token="
                                + activationToken;

                message.setTo(toEmail);
                message.setSubject("Activate Your TalentBridge Account");

                message.setText(
                                "Dear " + candidateName + ",\n\n"
                                                + "Your account has been successfully created on TalentBridge.\n\n"
                                                + "Login Email: " + toEmail + "\n\n"
                                                + "Please click the link below to activate your account and set your password:\n\n"
                                                + activationUrl + "\n\n"
                                                + "This link is valid for 24 hours.\n\n"
                                                + "After activation, you can login and start applying for jobs.\n\n"
                                                + "If you did not register, please ignore this email.\n\n"
                                                + "Best regards,\n"
                                                + "TalentBridge Team");

                mailSender.send(message);
        }
}
