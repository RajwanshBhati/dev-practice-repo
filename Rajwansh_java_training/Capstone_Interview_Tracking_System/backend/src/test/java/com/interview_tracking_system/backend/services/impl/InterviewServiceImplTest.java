package com.interview_tracking_system.backend.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.interview_tracking_system.backend.dto.ScheduleInterviewRequestDTO;
import com.interview_tracking_system.backend.dto.SubmitFeedbackRequestDTO;
import com.interview_tracking_system.backend.dto.UpdateCandidateStatusDTO;
import com.interview_tracking_system.backend.entity.Candidate;
import com.interview_tracking_system.backend.entity.Feedback;
import com.interview_tracking_system.backend.entity.Interview;
import com.interview_tracking_system.backend.entity.InterviewPanel;
import com.interview_tracking_system.backend.entity.JobDescription;
import com.interview_tracking_system.backend.entity.User;
import com.interview_tracking_system.backend.enums.FeedbackStatus;
import com.interview_tracking_system.backend.enums.Stage;
import com.interview_tracking_system.backend.repository.CandidateRepository;
import com.interview_tracking_system.backend.repository.FeedbackRepository;
import com.interview_tracking_system.backend.repository.InterviewPanelRepository;
import com.interview_tracking_system.backend.repository.InterviewRepository;
import com.interview_tracking_system.backend.repository.JobDescriptionRepository;
import com.interview_tracking_system.backend.repository.PanelRepository;
import com.interview_tracking_system.backend.repository.UserRepository;
import com.interview_tracking_system.backend.service.EmailService;
import com.interview_tracking_system.backend.service.impl.InterviewServiceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit test class for InterviewServiceImpl.
 */
@ExtendWith(MockitoExtension.class)
class InterviewServiceImplTest {

    @Mock
    private CandidateRepository candidateRepository;

    @Mock
    private InterviewRepository interviewRepository;

    @Mock
    private InterviewPanelRepository interviewPanelRepository;

    @Mock
    private PanelRepository panelRepository;

    @Mock
    private FeedbackRepository feedbackRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JobDescriptionRepository jobDescriptionRepository;

    private InterviewServiceImpl service;

    /**
     * Initializes service before each test.
     */
    @BeforeEach
    void setUp() {
        service = new InterviewServiceImpl(
                candidateRepository,
                interviewRepository,
                interviewPanelRepository,
                panelRepository,
                feedbackRepository,
                emailService,
                userRepository,
                jobDescriptionRepository);
    }

    /**
     * Tests successful L1 interview scheduling.
     */
    @Test
    void shouldScheduleL1InterviewSuccessfully() {
        Candidate candidate = createCandidate(Stage.SCREENING);
        User panel = createPanelUser();
        Interview savedInterview = createInterview(10L, Stage.L1_TECHNICAL);

        ScheduleInterviewRequestDTO request = new ScheduleInterviewRequestDTO();
        request.setCandidateId(1L);
        request.setStage("L1_TECHNICAL");
        request.setPanelIds(List.of(2L));
        request.setInterviewTime(LocalDateTime.now().plusDays(1));
        request.setFocusAreas("Java");

        when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidate));
        when(interviewRepository.save(any(Interview.class))).thenReturn(savedInterview);
        when(userRepository.findById(2L)).thenReturn(Optional.of(panel));

        service.scheduleInterview(request);

        verify(interviewRepository).save(any(Interview.class));
        verify(interviewPanelRepository).save(any(InterviewPanel.class));
        verify(candidateRepository).save(candidate);
        verify(emailService).sendCandidateInterviewScheduleEmail(
                any(), any(), any(), any());
        verify(emailService).sendPanelInterviewAssignmentEmail(
                any(), any(), any(), any(), any());
        assertEquals(Stage.L1_TECHNICAL, candidate.getStatus());
    }

    /**
     * Tests schedule interview failure when candidate is missing.
     */
    @Test
    void shouldThrowExceptionWhenCandidateNotFoundForSchedule() {
        ScheduleInterviewRequestDTO request = new ScheduleInterviewRequestDTO();
        request.setCandidateId(1L);
        request.setStage("L1_TECHNICAL");

        when(candidateRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.scheduleInterview(request));
    }

    /**
     * Tests schedule interview failure for invalid panel selection.
     */
    @Test
    void shouldThrowExceptionWhenPanelSelectionIsInvalid() {
        Candidate candidate = createCandidate(Stage.SCREENING);

        ScheduleInterviewRequestDTO request = new ScheduleInterviewRequestDTO();
        request.setCandidateId(1L);
        request.setStage("L1_TECHNICAL");
        request.setPanelIds(List.of());
        request.setInterviewTime(LocalDateTime.now().plusDays(1));

        when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidate));

        assertThrows(RuntimeException.class, () -> service.scheduleInterview(request));
    }

    /**
     * Tests successful candidate status update.
     */
    @Test
    void shouldUpdateCandidateStatusSuccessfully() {
        Candidate candidate = createCandidate(Stage.PROFILING);

        UpdateCandidateStatusDTO request = new UpdateCandidateStatusDTO();
        request.setCandidateId(1L);
        request.setDecision("SCREENING");

        when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidate));
        when(interviewRepository.findByCandidateId(1L)).thenReturn(List.of());

        service.updateCandidateStatus(request);

        assertEquals(Stage.SCREENING, candidate.getStatus());
        verify(candidateRepository).save(candidate);
    }

    /**
     * Tests rejected candidate status update.
     */
    @Test
    void shouldSetRejectedStageWhenCandidateIsRejected() {
        Candidate candidate = createCandidate(Stage.L1_TECHNICAL);

        UpdateCandidateStatusDTO request = new UpdateCandidateStatusDTO();
        request.setCandidateId(1L);
        request.setDecision("REJECTED");

        when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidate));
        when(interviewRepository.findByCandidateId(1L)).thenReturn(List.of());

        service.updateCandidateStatus(request);

        assertEquals(Stage.REJECTED, candidate.getStatus());
        assertEquals(Stage.L1_TECHNICAL, candidate.getRejectedStage());
    }

    /**
     * Tests status update failure when candidate id is missing.
     */
    @Test
    void shouldThrowExceptionWhenCandidateIdIsMissing() {
        UpdateCandidateStatusDTO request = new UpdateCandidateStatusDTO();

        assertThrows(RuntimeException.class, () -> service.updateCandidateStatus(request));
    }

    /**
     * Tests successful feedback submission.
     */
    @Test
    void shouldSubmitFeedbackSuccessfully() {
        Interview interview = createInterview(10L, Stage.L1_TECHNICAL);
        interview.setDate(LocalDate.now().minusDays(1));
        interview.setTime(LocalTime.now());

        SubmitFeedbackRequestDTO request = new SubmitFeedbackRequestDTO();
        request.setInterviewId(10L);
        request.setComments("Good");
        request.setStrengths("Java");
        request.setWeaknesses("Communication");
        request.setRating(4);
        request.setDecision("SELECTED");

        when(feedbackRepository.findByInterviewIdAndPanelId(10L, 2L))
                .thenReturn(Optional.empty());
        when(interviewRepository.findById(10L)).thenReturn(Optional.of(interview));
        when(userRepository.findById(2L)).thenReturn(Optional.of(createPanelUser()));

        service.submitFeedback(2L, request);

        ArgumentCaptor<Feedback> captor = ArgumentCaptor.forClass(Feedback.class);
        verify(feedbackRepository).save(captor.capture());
        assertEquals(FeedbackStatus.SELECTED, captor.getValue().getStatus());
    }

    /**
     * Tests feedback duplicate submission failure.
     */
    @Test
    void shouldThrowExceptionWhenFeedbackAlreadySubmitted() {
        SubmitFeedbackRequestDTO request = new SubmitFeedbackRequestDTO();
        request.setInterviewId(10L);

        when(feedbackRepository.findByInterviewIdAndPanelId(10L, 2L))
                .thenReturn(Optional.of(new Feedback()));

        assertThrows(RuntimeException.class, () -> service.submitFeedback(2L, request));
    }

    /**
     * Tests candidate interview id retrieval.
     */
    @Test
    void shouldReturnCandidateInterviewIds() {
        Interview first = createInterview(10L, Stage.L1_TECHNICAL);
        Interview second = createInterview(11L, Stage.L2_TECHNICAL);

        when(interviewRepository.findByCandidateId(1L)).thenReturn(List.of(first, second));

        List<Long> result = service.getCandidateInterviews(1L);

        assertEquals(List.of(10L, 11L), result);
    }

    /**
     * Tests panel interview DTO retrieval.
     */
    @Test
    void shouldReturnPanelInterviews() {
        UUID jdId = UUID.randomUUID();
        Candidate candidate = createCandidate(Stage.L1_TECHNICAL);
        candidate.setJdId(jdId);

        Interview interview = createInterview(10L, Stage.L1_TECHNICAL);

        InterviewPanel mapping = new InterviewPanel();
        mapping.setInterviewId(10L);
        mapping.setPanelId(2L);

        JobDescription jd = new JobDescription();
        jd.setJobTitle("Java Developer");

        when(interviewPanelRepository.findByPanelId(2L)).thenReturn(List.of(mapping));
        when(interviewRepository.findById(10L)).thenReturn(Optional.of(interview));
        when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidate));
        when(jobDescriptionRepository.findById(jdId)).thenReturn(Optional.of(jd));
        when(feedbackRepository.findByInterviewIdAndPanelId(10L, 2L))
                .thenReturn(Optional.empty());

        assertEquals(1, service.getPanelInterviews(2L).size());
        assertEquals("Java Developer", service.getPanelInterviews(2L).get(0).getJobTitle());
    }

    /**
     * Creates candidate test data.
     *
     * @param stage candidate stage
     * @return candidate entity
     */
    private Candidate createCandidate(Stage stage) {
        Candidate candidate = new Candidate();
        candidate.setId(1L);
        candidate.setName("Raj");
        candidate.setEmail("raj@test.com");
        candidate.setMobile("9876543210");
        candidate.setTotalExp(5);
        candidate.setRelevantExp(4);
        candidate.setCurrentCompany("ABC");
        candidate.setResumeUrl("/api/resumes/raj.pdf");
        candidate.setStatus(stage);
        return candidate;
    }

    /**
     * Creates panel user test data.
     *
     * @return user entity
     */
    private User createPanelUser() {
        User user = new User();
        user.setId(2L);
        user.setName("Panel User");
        user.setEmail("panel@test.com");
        return user;
    }

    /**
     * Creates interview test data.
     *
     * @param id    interview id
     * @param stage interview stage
     * @return interview entity
     */
    private Interview createInterview(Long id, Stage stage) {
        Interview interview = new Interview();
        interview.setId(id);
        interview.setCandidateId(1L);
        interview.setStage(stage);
        interview.setDate(LocalDate.now());
        interview.setTime(LocalTime.now());
        interview.setFocusArea("Java");
        return interview;
    }

    /**
     * Tests feedback retrieval for candidate with panel details.
     */
    @Test
    void shouldReturnFeedbackForCandidate() {
        Candidate candidate = createCandidate(Stage.L1_TECHNICAL);

        Interview interview = createInterview(10L, Stage.L1_TECHNICAL);

        Feedback feedback = new Feedback();
        feedback.setId(100L);
        feedback.setInterviewId(10L);
        feedback.setPanelId(2L);
        feedback.setComments("Good");
        feedback.setStrength("Java");
        feedback.setWeakness("Communication");
        feedback.setRating(4);
        feedback.setStatus(FeedbackStatus.SELECTED);

        User panel = createPanelUser();

        when(interviewRepository.findByCandidateId(1L))
                .thenReturn(List.of(interview));

        when(feedbackRepository.findByInterviewId(10L))
                .thenReturn(List.of(feedback));

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(panel));

        when(candidateRepository.findById(1L))
                .thenReturn(Optional.of(candidate));

        assertEquals(1, service.getFeedbackForCandidate(1L).size());
        assertEquals("Raj", service.getFeedbackForCandidate(1L).get(0).getCandidateName());
        assertEquals("Panel User", service.getFeedbackForCandidate(1L).get(0).getPanelName());
        assertEquals("SELECTED", service.getFeedbackForCandidate(1L).get(0).getDecision());
    }

    /**
     * Tests feedback retrieval when panel user is missing.
     */
    @Test
    void shouldReturnFeedbackWithDefaultPanelWhenPanelMissing() {
        Candidate candidate = createCandidate(Stage.L1_TECHNICAL);

        Interview interview = createInterview(10L, Stage.L1_TECHNICAL);

        Feedback feedback = new Feedback();
        feedback.setId(100L);
        feedback.setInterviewId(10L);
        feedback.setPanelId(2L);
        feedback.setComments("Average");
        feedback.setRating(3);

        when(interviewRepository.findByCandidateId(1L))
                .thenReturn(List.of(interview));

        when(feedbackRepository.findByInterviewId(10L))
                .thenReturn(List.of(feedback));

        when(userRepository.findById(2L))
                .thenReturn(Optional.empty());

        when(candidateRepository.findById(1L))
                .thenReturn(Optional.of(candidate));

        assertEquals("-", service.getFeedbackForCandidate(1L).get(0).getPanelName());
        assertEquals("-", service.getFeedbackForCandidate(1L).get(0).getPanelEmail());
        assertEquals("-", service.getFeedbackForCandidate(1L).get(0).getDecision());
    }

    /**
     * Tests feedback retrieval skips record when candidate is missing.
     */
    @Test
    void shouldSkipFeedbackWhenCandidateMissing() {
        Interview interview = createInterview(10L, Stage.L1_TECHNICAL);

        Feedback feedback = new Feedback();
        feedback.setId(100L);
        feedback.setInterviewId(10L);
        feedback.setPanelId(2L);

        when(interviewRepository.findByCandidateId(1L))
                .thenReturn(List.of(interview));

        when(feedbackRepository.findByInterviewId(10L))
                .thenReturn(List.of(feedback));

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(createPanelUser()));

        when(candidateRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertEquals(0, service.getFeedbackForCandidate(1L).size());
    }

    /**
     * Tests L2 schedule failure when previous L1 interview is missing.
     */
    @Test
    void shouldThrowExceptionWhenPreviousInterviewIsNotScheduled() {
        Candidate candidate = createCandidate(Stage.L1_TECHNICAL);

        ScheduleInterviewRequestDTO request = new ScheduleInterviewRequestDTO();
        request.setCandidateId(1L);
        request.setStage("L2_TECHNICAL");
        request.setPanelIds(List.of(2L));
        request.setInterviewTime(LocalDateTime.now().plusDays(1));

        when(candidateRepository.findById(1L))
                .thenReturn(Optional.of(candidate));

        when(interviewRepository.findByCandidateId(1L))
                .thenReturn(List.of());

        assertThrows(RuntimeException.class, () -> service.scheduleInterview(request));
    }

    /**
     * Tests L2 schedule failure when previous feedback is incomplete.
     */
    @Test
    void shouldThrowExceptionWhenPreviousFeedbackIsIncomplete() {
        Candidate candidate = createCandidate(Stage.L1_TECHNICAL);
        Interview l1Interview = createInterview(10L, Stage.L1_TECHNICAL);

        InterviewPanel panelOne = new InterviewPanel();
        panelOne.setInterviewId(10L);
        panelOne.setPanelId(2L);

        InterviewPanel panelTwo = new InterviewPanel();
        panelTwo.setInterviewId(10L);
        panelTwo.setPanelId(3L);

        ScheduleInterviewRequestDTO request = new ScheduleInterviewRequestDTO();
        request.setCandidateId(1L);
        request.setStage("L2_TECHNICAL");
        request.setPanelIds(List.of(2L));
        request.setInterviewTime(LocalDateTime.now().plusDays(1));

        when(candidateRepository.findById(1L))
                .thenReturn(Optional.of(candidate));

        when(interviewRepository.findByCandidateId(1L))
                .thenReturn(List.of(l1Interview));

        when(interviewPanelRepository.findByInterviewId(10L))
                .thenReturn(List.of(panelOne, panelTwo));

        when(feedbackRepository.countByInterviewId(10L))
                .thenReturn(1L);

        assertThrows(RuntimeException.class, () -> service.scheduleInterview(request));
    }

    /**
     * Tests successful L2 scheduling after previous feedback is complete.
     */
    @Test
    void shouldScheduleL2InterviewWhenPreviousFeedbackIsComplete() {
        Candidate candidate = createCandidate(Stage.L1_TECHNICAL);
        Interview l1Interview = createInterview(10L, Stage.L1_TECHNICAL);
        Interview savedInterview = createInterview(11L, Stage.L2_TECHNICAL);

        InterviewPanel previousPanel = new InterviewPanel();
        previousPanel.setInterviewId(10L);
        previousPanel.setPanelId(2L);

        ScheduleInterviewRequestDTO request = new ScheduleInterviewRequestDTO();
        request.setCandidateId(1L);
        request.setStage("L2_TECHNICAL");
        request.setPanelIds(List.of(2L));
        request.setInterviewTime(LocalDateTime.now().plusDays(1));
        request.setFocusAreas("System Design");

        when(candidateRepository.findById(1L))
                .thenReturn(Optional.of(candidate));

        when(interviewRepository.findByCandidateId(1L))
                .thenReturn(List.of(l1Interview));

        when(interviewPanelRepository.findByInterviewId(10L))
                .thenReturn(List.of(previousPanel));

        when(feedbackRepository.countByInterviewId(10L))
                .thenReturn(1L);

        when(interviewRepository.save(any(Interview.class)))
                .thenReturn(savedInterview);

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(createPanelUser()));

        service.scheduleInterview(request);

        assertEquals(Stage.L2_TECHNICAL, candidate.getStatus());
        verify(candidateRepository).save(candidate);
    }
}
