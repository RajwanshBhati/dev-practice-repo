package com.interview_tracking_system.backend.services.impl;

/**
 * Static imports for Mockito methods.
 */
import static org.mockito.Mockito.*;

/**
 * JUnit imports for test execution.
 */
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Mockito imports for mocking dependencies.
 */
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Spring mail imports used for email sending.
 */
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.interview_tracking_system.backend.service.impl.EmailServiceImpl;

/**
 * This class tests EmailServiceImpl.
 *
 * It verifies that all email sending methods
 * trigger JavaMailSender correctly.
 */
@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

    /**
     * Mocked mail sender.
     */
    @Mock
    private JavaMailSender mailSender;

    /**
     * Tests all email methods to ensure mail is sent.
     */
    @Test
    void allEmailMethodsShouldSendSimpleMail() {

        EmailServiceImpl service = new EmailServiceImpl(mailSender);

        service.sendProfilingCompletedEmail("a@test.com", "A");

        service.sendPanelActivationEmail("panel@test.com", "Panel", "token");

        service.sendCandidateInterviewScheduleEmail(
                "c@test.com",
                "Candidate",
                "L1",
                "03-05-2026 10:30");

        service.sendPanelInterviewAssignmentEmail(
                "p@test.com",
                "Panel",
                "Candidate",
                "L1",
                "03-05-2026 10:30");

        service.sendCandidateOnboardEmail(
                "c@test.com",
                "Candidate",
                "token");

        service.sendCandidateRegistrationEmail(
                "c@test.com",
                "Candidate",
                "token");

        verify(
                mailSender,
                times(6)).send(any(SimpleMailMessage.class));
    }
}
