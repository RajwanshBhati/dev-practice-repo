package com.interview_tracking_system.backend.service.impl;

import com.interview_tracking_system.backend.dto.LoginRequestDTO;
import com.interview_tracking_system.backend.dto.CandidateProfileRequest;
import com.interview_tracking_system.backend.dto.CandidateRegisterRequest;
import com.interview_tracking_system.backend.dto.CandidateResponseDTO;
import com.interview_tracking_system.backend.entity.Candidate;
import com.interview_tracking_system.backend.entity.CandidateUser;
import com.interview_tracking_system.backend.enums.Stage;
import com.interview_tracking_system.backend.repository.CandidateRepository;
import com.interview_tracking_system.backend.repository.CandidateUserRepository;
import com.interview_tracking_system.backend.service.CandidateService;
import com.interview_tracking_system.backend.service.EmailService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static com.interview_tracking_system.backend.constants.CandidateConstants.ERROR_EMAIL_EXISTS;
import static com.interview_tracking_system.backend.constants.CandidateConstants.ERROR_PASSWORD_MISMATCH;
import static com.interview_tracking_system.backend.constants.CandidateConstants.ERROR_INVALID_CREDENTIALS;
import static com.interview_tracking_system.backend.constants.CandidateConstants.ERROR_ALREADY_APPLIED;
import static com.interview_tracking_system.backend.constants.CandidateConstants.ERROR_NOT_LOGGED_IN;
import static com.interview_tracking_system.backend.constants.CandidateConstants.ERROR_NO_APPLICATION;

@Service
public class CandidateServiceImpl implements CandidateService {

    /** Logger instance for logging service activity */
    private static final Logger LOGGER = LoggerFactory.getLogger(CandidateServiceImpl.class);

    /** Repository for candidate users */
    private final CandidateUserRepository candidateUserRepository;

    /** Repository for candidate job applications */
    private final CandidateRepository candidateRepository;

    /** Email service for sending notifications */
    private final EmailService emailService;

    /** Password encoder for hashing passwords */
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Constructor for dependency injection.
     *
     * @param candidateUserRepository repository for candidate users
     * @param candidateRepository     repository for candidate data
     * @param emailService            email service
     */
    public CandidateServiceImpl(
            final CandidateUserRepository candidateUserRepository,
            final CandidateRepository candidateRepository,
            final EmailService emailService) {

        this.candidateUserRepository = candidateUserRepository;
        this.candidateRepository = candidateRepository;
        this.emailService = emailService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Registers a new candidate user.
     *
     * @param request the candidate registration request
     * @throws IllegalArgumentException if validation fails
     */
    @Override
    public void register(final CandidateRegisterRequest request) {

        LOGGER.info("Register request received for email: {}", request.getEmail());

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            LOGGER.error("Password mismatch for email: {}", request.getEmail());
            throw new IllegalArgumentException(ERROR_PASSWORD_MISMATCH);
        }

        if (candidateUserRepository.existsByEmail(request.getEmail())) {
            LOGGER.error("Email already exists: {}", request.getEmail());
            throw new IllegalArgumentException(ERROR_EMAIL_EXISTS);
        }

        CandidateUser user = new CandidateUser();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        candidateUserRepository.save(user);

        LOGGER.info("Candidate registered successfully: {}", request.getEmail());
    }

    /**
     * Authenticates a candidate user.
     *
     * @param request login request containing email and password
     * @return authenticated CandidateUser
     * @throws IllegalArgumentException if credentials are invalid
     */
    @Override
    public CandidateUser login(final LoginRequestDTO request) {

        LOGGER.info("Login attempt for email: {}", request.getEmail());

        CandidateUser user = candidateUserRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    LOGGER.error("User not found: {}", request.getEmail());
                    return new IllegalArgumentException(ERROR_INVALID_CREDENTIALS);
                });

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            LOGGER.error("Invalid password for email: {}", request.getEmail());
            throw new IllegalArgumentException(ERROR_INVALID_CREDENTIALS);
        }

        LOGGER.info("Login successful for email: {}", request.getEmail());
        return user;
    }

    /**
     * Applies the candidate to a job using profile details.
     *
     * @param request candidate profile details
     * @param email   authenticated user's email (extracted from JWT)
     * @return CandidateResponseDTO containing saved application data
     */
    @Override
    public CandidateResponseDTO applyToJob(
            final CandidateProfileRequest request,
            final String email) {

        LOGGER.info("Apply job request received for email: {}", email);

        CandidateUser candidateUser = candidateUserRepository.findByEmail(email)
                .orElseThrow(() -> {
                    LOGGER.error("User not found: {}", email);
                    return new IllegalStateException(ERROR_NOT_LOGGED_IN);
                });

        if (candidateRepository.findByCandidateUserId(candidateUser.getId()).isPresent()) {
            LOGGER.error("Candidate already applied: {}", email);
            throw new IllegalStateException(ERROR_ALREADY_APPLIED);
        }

        Candidate candidate = new Candidate();

        candidate.setName(request.getName());
        candidate.setEmail(request.getEmail());

        String fullMobile = request.getMobileCode() + request.getMobileNumber();
        candidate.setMobile(fullMobile);

        candidate.setDateOfBirth(request.getDateOfBirth());
        candidate.setResumeUrl(request.getResumeUrl());
        candidate.setCurrentCompany(request.getCurrentCompany());
        candidate.setTotalExp(request.getTotalExp());
        candidate.setRelevantExp(request.getRelevantExp());
        candidate.setCurrentCtc(request.getCurrentCtc());
        candidate.setExpectedCtc(request.getExpectedCtc());
        candidate.setNoticePeriod(request.getNoticePeriod());
        candidate.setPreferredLocation(request.getPreferredLocation());
        candidate.setSource(request.getSource());
        candidate.setJdId(request.getJdId());
        candidate.setStatus(Stage.PROFILING);

        candidate.setCandidateUser(candidateUser);

        Candidate saved = candidateRepository.save(candidate);

        emailService.sendProfilingCompletedEmail(email, request.getName());

        LOGGER.info("Candidate applied successfully for JD: {}", request.getJdId());

        return mapToResponse(saved);
    }

    /**
     * Retrieves the current application status of the candidate.
     *
     * @param email authenticated user's email (from JWT)
     * @return CandidateResponseDTO containing application status
     */
    @Override
    public CandidateResponseDTO getMyStatus(final String email) {

        LOGGER.info("Fetching status for email: {}", email);

        CandidateUser user = candidateUserRepository.findByEmail(email)
                .orElseThrow(() -> {
                    LOGGER.error("User not found: {}", email);
                    return new IllegalStateException(ERROR_NOT_LOGGED_IN);
                });

        Candidate candidate = candidateRepository.findByCandidateUserId(user.getId())
                .orElseThrow(() -> {
                    LOGGER.error("No application found for user: {}", email);
                    return new IllegalStateException(ERROR_NO_APPLICATION);
                });

        LOGGER.info("Status fetched successfully for email: {}", email);

        return mapToResponse(candidate);
    }

    /**
     * Maps Candidate entity to response DTO.
     *
     * @param candidate entity object
     * @return CandidateResponseDTO
     */
    private CandidateResponseDTO mapToResponse(final Candidate candidate) {

        CandidateResponseDTO dto = new CandidateResponseDTO();
        dto.setId(candidate.getId());
        dto.setName(candidate.getName());
        dto.setEmail(candidate.getEmail());
        dto.setStatus(candidate.getStatus());
        dto.setJdId(candidate.getJdId());

        return dto;
    }
}
