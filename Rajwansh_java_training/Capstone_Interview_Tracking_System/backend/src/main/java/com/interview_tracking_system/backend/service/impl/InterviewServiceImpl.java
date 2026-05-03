package com.interview_tracking_system.backend.service.impl;

import com.interview_tracking_system.backend.dto.HRFeedbackDTO;
import com.interview_tracking_system.backend.dto.PanelInterviewDTO;
import com.interview_tracking_system.backend.dto.ScheduleInterviewRequestDTO;
import com.interview_tracking_system.backend.dto.SubmitFeedbackRequestDTO;
import com.interview_tracking_system.backend.dto.UpdateCandidateStatusDTO;
import com.interview_tracking_system.backend.entity.Candidate;
import com.interview_tracking_system.backend.entity.Feedback;
import com.interview_tracking_system.backend.entity.Interview;
import com.interview_tracking_system.backend.entity.InterviewPanel;
import com.interview_tracking_system.backend.enums.FeedbackStatus;
import com.interview_tracking_system.backend.enums.Stage;
import com.interview_tracking_system.backend.repository.CandidateRepository;
import com.interview_tracking_system.backend.repository.FeedbackRepository;
import com.interview_tracking_system.backend.repository.InterviewPanelRepository;
import com.interview_tracking_system.backend.repository.InterviewRepository;
import com.interview_tracking_system.backend.service.EmailService;
import com.interview_tracking_system.backend.service.InterviewService;
import com.interview_tracking_system.backend.entity.User;
import com.interview_tracking_system.backend.repository.UserRepository;
import com.interview_tracking_system.backend.entity.JobDescription;
import com.interview_tracking_system.backend.repository.JobDescriptionRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    private final UserRepository userRepository;
    private final FeedbackRepository feedbackRepository;
    private final EmailService emailService;
    private final JobDescriptionRepository jobDescriptionRepository;

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
            final FeedbackRepository feedbackRepository,
            final EmailService emailService,
            final UserRepository userRepository,
            final JobDescriptionRepository jobDescriptionRepository) {
        this.candidateRepository = candidateRepository;
        this.interviewRepository = interviewRepository;
        this.interviewPanelRepository = interviewPanelRepository;
        this.userRepository = userRepository;
        this.feedbackRepository = feedbackRepository;
        this.emailService = emailService;
        this.jobDescriptionRepository = jobDescriptionRepository;
    }

    private void validateScheduleStage(final Candidate candidate, final Stage requestedStage) {

        Stage currentStage = candidate.getStatus();

        if (requestedStage == Stage.L1_TECHNICAL && currentStage != Stage.SCREENING) {
            throw new RuntimeException("Candidate must be in Screening before scheduling L1 interview.");
        }

        if (requestedStage == Stage.L2_TECHNICAL) {
            if (currentStage != Stage.L1_TECHNICAL) {
                throw new RuntimeException("Candidate must complete L1 before scheduling L2 interview.");
            }

            validateLatestInterviewFeedback(candidate.getId(), Stage.L1_TECHNICAL);
        }

        if (requestedStage == Stage.HR_ROUND) {
            if (currentStage != Stage.L2_TECHNICAL) {
                throw new RuntimeException("Candidate must complete L2 before scheduling HR round.");
            }

            validateLatestInterviewFeedback(candidate.getId(), Stage.L2_TECHNICAL);
        }
    }

    private void validateLatestInterviewFeedback(final Long candidateId, final Stage completedStage) {

        List<Interview> interviews = interviewRepository.findByCandidateId(candidateId);

        Interview latestInterview = null;

        for (Interview interview : interviews) {
            if (interview.getStage() == completedStage) {
                latestInterview = interview;
            }
        }

        if (latestInterview == null) {
            throw new RuntimeException("Previous interview round is not scheduled.");
        }

        int assignedPanelCount = interviewPanelRepository
                .findByInterviewId(latestInterview.getId())
                .size();

        long submittedFeedbackCount = feedbackRepository
                .countByInterviewId(latestInterview.getId());

        if (submittedFeedbackCount < assignedPanelCount) {
            throw new RuntimeException("All assigned panel feedback is required before scheduling next round.");
        }
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
        validateScheduleStage(candidate, stage);

        List<Long> panelIds = request.getPanelIds();

        if (stage == Stage.HR_ROUND) {
            panelIds = new ArrayList<>();
        } else if (panelIds == null || panelIds.isEmpty() || panelIds.size() > 2) {
            throw new RuntimeException("Panel selection must be between 1 and 2");
        }
        Interview interview = new Interview();
        interview.setCandidateId(candidate.getId());
        interview.setStage(stage);
        interview.setDate(request.getInterviewTime().toLocalDate());
        interview.setTime(request.getInterviewTime().toLocalTime());
        interview.setFocusArea(request.getFocusAreas());

        Interview savedInterview = interviewRepository.save(interview);

        List<User> selectedPanels = new ArrayList<>();

        for (Long panelId : panelIds) {
            User panelUser = userRepository.findById(panelId)
                    .orElseThrow(() -> new RuntimeException("Panel not found"));

            selectedPanels.add(panelUser);

            InterviewPanel interviewPanel = new InterviewPanel();
            interviewPanel.setInterviewId(savedInterview.getId());
            interviewPanel.setPanelId(panelUser.getId());

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

        for (User panelUser : selectedPanels) {
            emailService.sendPanelInterviewAssignmentEmail(
                    panelUser.getEmail(),
                    panelUser.getName(),
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

        if (request.getCandidateId() == null) {
            throw new RuntimeException("Candidate id is required");
        }

        Candidate candidate = candidateRepository.findById(request.getCandidateId())
                .orElseThrow(() -> new RuntimeException("Candidate not found"));

        String statusValue = request.getDecision();

        if (statusValue == null || statusValue.isBlank()) {
            statusValue = request.getStage();
        }

        if (statusValue == null || statusValue.isBlank()) {
            throw new RuntimeException("Candidate status is required");
        }

        Stage status = Stage.valueOf(statusValue.trim().toUpperCase());

        // Check panel feedback before moving candidate to next stage
        List<Interview> interviews = interviewRepository.findByCandidateId(candidate.getId());

        Interview latestInterview = interviews.isEmpty()
                ? null
                : interviews.get(interviews.size() - 1);

        if (latestInterview != null
                && latestInterview.getStage() != Stage.HR_ROUND
                && status != Stage.REJECTED
                && status != Stage.SELECTED) {

            int assignedPanelCount = interviewPanelRepository
                    .findByInterviewId(latestInterview.getId())
                    .size();

            long submittedFeedbackCount = feedbackRepository
                    .countByInterviewId(latestInterview.getId());

            if (submittedFeedbackCount < assignedPanelCount) {
                throw new RuntimeException(
                        "All assigned panel feedback is required before moving candidate to next round.");
            }
        }

        if (status == Stage.REJECTED) {
            candidate.setRejectedStage(candidate.getStatus());
        } else {
            candidate.setRejectedStage(null);
        }

        candidate.setStatus(status);
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

        Interview interview = interviewRepository.findById(request.getInterviewId())
                .orElseThrow(() -> new RuntimeException("Interview not found"));

        LocalDateTime interviewDateTime = LocalDateTime.of(
                interview.getDate(),
                interview.getTime());

        if (LocalDateTime.now().isBefore(interviewDateTime)) {
            throw new RuntimeException("Feedback can be submitted only after interview time.");
        }

        userRepository.findById(panelId)
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
    public List<PanelInterviewDTO> getPanelInterviews(final Long panelId) {

        LOGGER.info("Fetching interviews for panel id {}", panelId);

        List<InterviewPanel> mappings = interviewPanelRepository.findByPanelId(panelId);

        List<PanelInterviewDTO> response = new ArrayList<>();

        for (InterviewPanel mapping : mappings) {

            Interview interview = interviewRepository.findById(mapping.getInterviewId())
                    .orElseThrow(() -> new RuntimeException("Interview not found"));

            Candidate candidate = candidateRepository.findById(interview.getCandidateId())
                    .orElseThrow(() -> new RuntimeException("Candidate not found"));

            PanelInterviewDTO dto = new PanelInterviewDTO();

            dto.setInterviewId(interview.getId());

            dto.setCandidateName(candidate.getName());
            dto.setCandidateEmail(candidate.getEmail());
            dto.setCandidateMobileNumber(candidate.getMobile());

            dto.setTotalExperience(candidate.getTotalExp());
            dto.setRelevantExperience(candidate.getRelevantExp());
            dto.setCurrentCompany(candidate.getCurrentCompany());

            String jobTitle = "-";

            if (candidate.getJdId() != null) {
                jobTitle = jobDescriptionRepository.findById(candidate.getJdId())
                        .map(JobDescription::getJobTitle)
                        .orElse("-");
            }

            dto.setJobTitle(jobTitle);

            dto.setStage(interview.getStage().name());

            dto.setInterviewDate(interview.getDate().toString());
            dto.setInterviewTime(interview.getTime().toString());

            dto.setFocusArea(interview.getFocusArea());
            dto.setResumeUrl(candidate.getResumeUrl());

            boolean alreadySubmitted = feedbackRepository
                    .findByInterviewIdAndPanelId(interview.getId(), panelId)
                    .isPresent();

            dto.setFeedbackSubmitted(alreadySubmitted);

            response.add(dto);
        }

        return response;
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

    @Override
    public List<HRFeedbackDTO> getFeedbackForCandidate(final Long candidateId) {

        LOGGER.info("Fetching feedback for candidate {}", candidateId);

        List<Interview> interviews = interviewRepository.findByCandidateId(candidateId);

        List<HRFeedbackDTO> response = new ArrayList<>();

        for (Interview interview : interviews) {

            List<Feedback> feedbacks = feedbackRepository.findByInterviewId(interview.getId());

            for (Feedback feedback : feedbacks) {

                User panelUser = userRepository.findById(feedback.getPanelId())
                        .orElse(null);

                Candidate candidate = candidateRepository.findById(candidateId)
                        .orElse(null);

                if (candidate == null)
                    continue;

                HRFeedbackDTO dto = new HRFeedbackDTO();

                dto.setFeedbackId(feedback.getId());
                dto.setInterviewId(interview.getId());
                dto.setCandidateId(candidateId);

                dto.setCandidateName(candidate.getName());

                dto.setPanelId(feedback.getPanelId());

                dto.setPanelName(
                        panelUser != null ? panelUser.getName() : "-");

                dto.setPanelEmail(
                        panelUser != null ? panelUser.getEmail() : "-");

                dto.setStage(interview.getStage().name());

                dto.setInterviewDate(
                        interview.getDate() != null ? interview.getDate().toString() : "-");

                dto.setInterviewTime(
                        interview.getTime() != null ? interview.getTime().toString() : "-");

                dto.setComments(feedback.getComments());
                dto.setStrengths(feedback.getStrength());
                dto.setWeaknesses(feedback.getWeakness());
                dto.setRating(feedback.getRating());
                dto.setDecision(
                        feedback.getStatus() != null
                                ? feedback.getStatus().name()
                                : "-");

                response.add(dto);
            }
        }

        return response;
    }
}
