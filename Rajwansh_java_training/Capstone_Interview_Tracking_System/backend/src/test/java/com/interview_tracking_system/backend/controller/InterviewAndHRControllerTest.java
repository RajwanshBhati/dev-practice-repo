package com.interview_tracking_system.backend.controller;

/**
 * Static imports for assertions and mocking.
 */
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.nullable;

/**
 * DTO imports used in testing.
 */
import com.interview_tracking_system.backend.dto.HRCandidateFullDTO;
import com.interview_tracking_system.backend.dto.HRFeedbackDTO;
import com.interview_tracking_system.backend.dto.PanelInterviewDTO;
import com.interview_tracking_system.backend.dto.ScheduleInterviewRequestDTO;
import com.interview_tracking_system.backend.dto.SubmitFeedbackRequestDTO;
import com.interview_tracking_system.backend.dto.UpdateCandidateStatusDTO;

/**
 * Entity and repository imports.
 */
import com.interview_tracking_system.backend.entity.User;
import com.interview_tracking_system.backend.repository.UserRepository;

/**
 * Service layer imports.
 */
import com.interview_tracking_system.backend.service.HRDashboardService;
import com.interview_tracking_system.backend.service.InterviewService;

/**
 * Java utility imports.
 */
import java.util.List;
import java.util.Optional;

/**
 * Testing framework imports.
 */
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Mockito related imports.
 */
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Spring security import for authentication handling.
 */
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * This class tests InterviewController and HRDashboardController.
 *
 * It verifies that controller methods correctly call service layer
 * and return expected results.
 */
@ExtendWith(MockitoExtension.class)
class InterviewAndHRControllerTest {

        /**
         * Mocked dependencies.
         */
        @Mock
        private InterviewService interviewService;
        @Mock
        private UserRepository userRepository;
        @Mock
        private HRDashboardService hrDashboardService;

        /**
         * Controllers under test.
         */
        private InterviewController interviewController;
        private HRDashboardController hrController;

        /**
         * Initializes controllers before each test.
         */
        @BeforeEach
        void setUp() {
                interviewController = new InterviewController(interviewService, userRepository);
                hrController = new HRDashboardController(hrDashboardService, interviewService);
        }

        /**
         * Tests all InterviewController methods.
         */
        @Test
        void interviewControllerMethodsShouldDelegate() {

                ScheduleInterviewRequestDTO schedule = new ScheduleInterviewRequestDTO();
                interviewController.scheduleInterview(schedule);
                verify(interviewService).scheduleInterview(schedule);

                UpdateCandidateStatusDTO status = new UpdateCandidateStatusDTO();
                interviewController.updateCandidateStatus(status);
                verify(interviewService).updateCandidateStatus(status);

                User panel = new User();
                panel.setEmail("panel@test.com");

                when(userRepository.findByEmailIgnoreCase("panel@test.com"))
                                .thenReturn(Optional.of(panel));

                SubmitFeedbackRequestDTO feedback = new SubmitFeedbackRequestDTO();
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken("panel@test.com",
                                "x");

                interviewController.submitFeedback(auth, feedback);

                verify(interviewService).submitFeedback(nullable(Long.class), eq(feedback));

                PanelInterviewDTO dto = new PanelInterviewDTO();
                dto.setInterviewId(10L);

                when(interviewService.getPanelInterviews(anyLong()))
                                .thenReturn(List.of(dto));

                assertEquals(
                                10L,
                                interviewController.getPanelInterviews(2L).get(0).getInterviewId());

                when(interviewService.getCandidateInterviews(1L))
                                .thenReturn(List.of(10L));

                assertEquals(
                                List.of(10L),
                                interviewController.getCandidateInterviews(1L));
        }

        /**
         * Tests all HRDashboardController methods.
         */
        @Test
        void hrControllerMethodsShouldDelegate() {

                HRCandidateFullDTO candidate = new HRCandidateFullDTO();
                candidate.setId(1L);

                when(hrDashboardService.getAllCandidatesForHR())
                                .thenReturn(List.of(candidate));

                assertEquals(
                                1,
                                hrController.getCandidates().getBody().size());

                HRFeedbackDTO feedback = new HRFeedbackDTO();
                feedback.setCandidateId(1L);

                when(interviewService.getFeedbackForCandidate(1L))
                                .thenReturn(List.of(feedback));

                assertEquals(
                                1,
                                hrController.getCandidateFeedback(1L).size());
        }
}
