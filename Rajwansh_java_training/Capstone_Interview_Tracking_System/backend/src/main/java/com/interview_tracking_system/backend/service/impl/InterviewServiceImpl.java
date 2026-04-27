package com.interview_tracking_system.backend.service.impl;

import com.interview_tracking_system.backend.dto.ScheduleInterviewRequestDTO;
import com.interview_tracking_system.backend.dto.SubmitFeedbackRequestDTO;
import com.interview_tracking_system.backend.dto.UpdateCandidateStatusDTO;
import com.interview_tracking_system.backend.entity.Candidate;
import com.interview_tracking_system.backend.entity.Feedback;
import com.interview_tracking_system.backend.entity.Interview;
import com.interview_tracking_system.backend.entity.InterviewPanel;
import com.interview_tracking_system.backend.entity.Panel;
import com.interview_tracking_system.backend.enums.FeedbackStatus;
import com.interview_tracking_system.backend.enums.Stage;
import com.interview_tracking_system.backend.repository.CandidateRepository;
import com.interview_tracking_system.backend.repository.FeedbackRepository;
import com.interview_tracking_system.backend.repository.InterviewPanelRepository;
import com.interview_tracking_system.backend.repository.InterviewRepository;
import com.interview_tracking_system.backend.repository.PanelRepository;
import com.interview_tracking_system.backend.service.EmailService;
import com.interview_tracking_system.backend.service.InterviewService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Handles interview scheduling, candidate status update, and panel feedback
 * flow.
 */
@Service
public class InterviewServiceImpl implements InterviewService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InterviewServiceImpl.class);

    private final CandidateRepository candidateRepository;
    private final InterviewRepository interviewRepository;
    private final InterviewPanelRepository interviewPanelRepository;
    private final PanelRepository panelRepository;
    private final FeedbackRepository feedbackRepository;
    private final EmailService emailService;

    /**
     * Creates interview workflow service with required repositories and email
     * service.
     *
     * @param candidateRepository      candidate repository
     * @param interviewRepository      interview repository
     * @param interviewPanelRepository interview panel mapping repository
     * @param panelRepository          panel repository
     * @param feedbackRepository       feedback repository
     * @param emailService             email service
     */
    public InterviewServiceImpl(final CandidateRepository candidateRepository,
            final InterviewRepository interviewRepository,
            final InterviewPanelRepository interviewPanelRepository,
            final PanelRepository panelRepository,
            final FeedbackRepository feedbackRepository,
            final EmailService emailService) {
        this.candidateRepository = candidateRepository;
        this.interviewRepository = interviewRepository;
        this.interviewPanelRepository = interviewPanelRepository;
        this.panelRepository = panelRepository;
        this.feedbackRepository = feedbackRepository;
        this.emailService = emailService;
    }

    /**
     * Schedules L1 or L2 interview and assigns selected panels.
     *
     * @param request interview schedule request
     */
    @Override
    public void scheduleInterview(final ScheduleInterviewRequestDTO request) {

        LOGGER.info("Scheduling interview for candidate id {}", request.getCandidateId());

        Candidate candidate = candidateRepository.findById(request.getCandidateId())
                .orElseThrow(() -> new RuntimeException("Candidate not found"));

        Stage stage = Stage.valueOf(request.getStage());

        List<Long> panelIds = request.getPanelIds();

        if (panelIds == null || panelIds.isEmpty() || panelIds.size() > 2) {
            throw new RuntimeException("Panel selection must be between 1 and 2");
        }

        Interview interview = new Interview();
        interview.setCandidateId(candidate.getId());
        interview.setStage(stage);
        interview.setDate(request.getInterviewTime().toLocalDate());
        interview.setTime(request.getInterviewTime().toLocalTime());
        interview.setFocusArea(request.getFocusAreas());

        Interview savedInterview = interviewRepository.save(interview);

        List<Panel> selectedPanels = new ArrayList<>();

        for (Long panelId : panelIds) {
            Panel panel = panelRepository.findById(panelId)
                    .orElseThrow(() -> new RuntimeException("Panel not found"));

            selectedPanels.add(panel);

            InterviewPanel interviewPanel = new InterviewPanel();
            interviewPanel.setInterviewId(savedInterview.getId());
            interviewPanel.setPanelId(panel.getId());

            interviewPanelRepository.save(interviewPanel);
        }

        candidate.setStatus(stage);
        candidateRepository.save(candidate);

        String formattedDateTime = request.getInterviewTime()
                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));

        emailService.sendCandidateInterviewScheduleEmail(
                candidate.getEmail(),
                candidate.getName(),
                stage.name(),
                formattedDateTime);

        for (Panel panel : selectedPanels) {
            emailService.sendPanelInterviewAssignmentEmail(
                    panel.getEmail(),
                    panel.getFullName(),
                    candidate.getName(),
                    stage.name(),
                    formattedDateTime);
        }

        LOGGER.info("Interview scheduled successfully for candidate id {}", candidate.getId());
    }

    /**
     * Updates candidate current status from HR side.
     *
     * @param request candidate status update request
     */
    @Override
    public void updateCandidateStatus(final UpdateCandidateStatusDTO request) {

        LOGGER.info("Updating candidate status for id {}", request.getCandidateId());

        Candidate candidate = candidateRepository.findById(request.getCandidateId())
                .orElseThrow(() -> new RuntimeException("Candidate not found"));

        if (request.getDecision() != null && !request.getDecision().isBlank()) {
            Stage decision = Stage.valueOf(request.getDecision());
            candidate.setStatus(decision);
        } else if (request.getStage() != null && !request.getStage().isBlank()) {
            Stage stage = Stage.valueOf(request.getStage());
            candidate.setStatus(stage);
        }

        candidateRepository.save(candidate);

        LOGGER.info("Candidate status updated for id {}", candidate.getId());
    }

    /**
     * Saves panel feedback for a scheduled interview.
     *
     * @param panelId logged-in panel id
     * @param request feedback request
     */
    @Override
    public void submitFeedback(final Long panelId, final SubmitFeedbackRequestDTO request) {

        LOGGER.info("Submitting feedback for interview id {}", request.getInterviewId());

        Optional<Feedback> existingFeedback = feedbackRepository
                .findByInterviewIdAndPanelId(request.getInterviewId(), panelId);

        if (existingFeedback.isPresent()) {
            throw new RuntimeException("Feedback already submitted");
        }

        interviewRepository.findById(request.getInterviewId())
                .orElseThrow(() -> new RuntimeException("Interview not found"));

        panelRepository.findById(panelId)
                .orElseThrow(() -> new RuntimeException("Panel not found"));

        Feedback feedback = new Feedback();
        feedback.setInterviewId(request.getInterviewId());
        feedback.setPanelId(panelId);
        feedback.setComments(request.getComments());
        feedback.setStrength(request.getStrengths());
        feedback.setWeakness(request.getWeaknesses());
        feedback.setRating(request.getRating());
        feedback.setStatus(FeedbackStatus.valueOf(request.getDecision()));

        feedbackRepository.save(feedback);

        LOGGER.info("Feedback submitted for interview id {}", request.getInterviewId());
    }

    /**
     * Returns interview ids assigned to a panel.
     *
     * @param panelId panel id
     * @return interview id list
     */
    @Override
    public List<Long> getPanelInterviews(final Long panelId) {

        List<InterviewPanel> mappings = interviewPanelRepository.findByPanelId(panelId);

        List<Long> interviewIds = new ArrayList<>();

        for (InterviewPanel mapping : mappings) {
            interviewIds.add(mapping.getInterviewId());
        }

        return interviewIds;
    }

    /**
     * Returns interview ids scheduled for a candidate.
     *
     * @param candidateId candidate id
     * @return interview id list
     */
    @Override
    public List<Long> getCandidateInterviews(final Long candidateId) {

        List<Interview> interviews = interviewRepository.findByCandidateId(candidateId);

        List<Long> interviewIds = new ArrayList<>();

        for (Interview interview : interviews) {
            interviewIds.add(interview.getId());
        }

        return interviewIds;
    }
}
