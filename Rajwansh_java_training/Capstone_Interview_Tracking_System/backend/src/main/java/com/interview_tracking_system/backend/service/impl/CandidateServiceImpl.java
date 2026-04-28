package com.interview_tracking_system.backend.service.impl;

import com.interview_tracking_system.backend.dto.LoginRequestDTO;
import com.interview_tracking_system.backend.dto.CandidateProfileRequest;
import com.interview_tracking_system.backend.dto.CandidateRegisterRequest;
import com.interview_tracking_system.backend.dto.CandidateResponseDTO;
import com.interview_tracking_system.backend.entity.Candidate;
import com.interview_tracking_system.backend.entity.User;
import com.interview_tracking_system.backend.enums.Role;
import com.interview_tracking_system.backend.enums.Stage;
import com.interview_tracking_system.backend.enums.UserStatus;
import com.interview_tracking_system.backend.repository.CandidateRepository;
import com.interview_tracking_system.backend.repository.UserRepository;
import com.interview_tracking_system.backend.service.CandidateService;
import com.interview_tracking_system.backend.service.EmailService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import static com.interview_tracking_system.backend.constants.CandidateConstants.ERROR_EMAIL_EXISTS;
import static com.interview_tracking_system.backend.constants.CandidateConstants.ERROR_PASSWORD_MISMATCH;
import static com.interview_tracking_system.backend.constants.CandidateConstants.ERROR_INVALID_CREDENTIALS;
import static com.interview_tracking_system.backend.constants.CandidateConstants.ERROR_ALREADY_APPLIED;
import static com.interview_tracking_system.backend.constants.CandidateConstants.ERROR_NOT_LOGGED_IN;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class CandidateServiceImpl implements CandidateService {

    @Value("${resume.upload-dir:uploads/resumes}")
    private String resumeUploadDir;

    /** Logger instance for logging service activity */
    private static final Logger LOGGER = LoggerFactory.getLogger(CandidateServiceImpl.class);

    /** Repository for all users (HR, Panel, Candidate) */
    private final UserRepository userRepository;

    /** Repository for candidate job applications */
    private final CandidateRepository candidateRepository;

    /** Email service for sending notifications */
    private final EmailService emailService;

    /** Password encoder for hashing passwords */
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor for dependency injection.
     *
     * @param userRepository      repository for users
     * @param candidateRepository repository for candidate data
     * @param emailService        email service
     * @param passwordEncoder     password encoder bean
     */
    public CandidateServiceImpl(
            final UserRepository userRepository,
            final CandidateRepository candidateRepository,
            final EmailService emailService,
            final PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.candidateRepository = candidateRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new candidate user in the users table with ROLE_CANDIDATE.
     *
     * @param request the candidate registration request
     * @throws IllegalArgumentException if validation fails
     */
    @Override
    public void register(final CandidateRegisterRequest request) {

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Register request received for email: {}", request.getEmail());
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Password mismatch for email: {}", request.getEmail());
            }
            throw new IllegalArgumentException(ERROR_PASSWORD_MISMATCH);
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Email already exists: {}", request.getEmail());
            }
            throw new IllegalArgumentException(ERROR_EMAIL_EXISTS);
        }

        User user = new User();
        user.setName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.CANDIDATE);
        user.setStatus(UserStatus.ACTIVE);

        userRepository.save(user);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Candidate registered successfully: {}", request.getEmail());
        }
    }

    /**
     * Authenticates a candidate user.
     *
     * @param request login request containing email and password
     * @return authenticated User
     * @throws IllegalArgumentException if credentials are invalid
     */
    @Override
    public User login(final LoginRequestDTO request) {

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Login attempt for email: {}", request.getEmail());
        }

        User user = userRepository.findByEmailIgnoreCase(request.getEmail())
                .orElseThrow(() -> {
                    if (LOGGER.isErrorEnabled()) {
                        LOGGER.error("User not found: {}", request.getEmail());
                    }
                    return new IllegalArgumentException(ERROR_INVALID_CREDENTIALS);
                });

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Invalid password for email: {}", request.getEmail());
            }
            throw new IllegalArgumentException(ERROR_INVALID_CREDENTIALS);
        }

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Login successful for email: {}", request.getEmail());
        }
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
            final MultipartFile resumeFile,
            final String email) {

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Apply job request received for email: {}", email);
        }

        userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> {
                    if (LOGGER.isErrorEnabled()) {
                        LOGGER.error("User not found: {}", email);
                    }
                    return new IllegalStateException(ERROR_NOT_LOGGED_IN);
                });
        String fullMobile = request.getMobileCode() + request.getMobileNumber();
        if (candidateRepository.existsByEmail(email)
                || candidateRepository.existsByMobile(fullMobile)) {

            LOGGER.error("Candidate already applied: {} / {}", email, fullMobile);
            throw new IllegalStateException(ERROR_ALREADY_APPLIED);
        }
        if (request.getJdId() == null) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Invalid or missing jdId for email: {}", email);
            }
            throw new IllegalArgumentException("Please provide a valid jdId.");
        }

        Candidate candidate = new Candidate();
        candidate.setName(request.getName());
        candidate.setEmail(request.getEmail());

        candidate.setMobile(fullMobile);

        candidate.setDateOfBirth(request.getDateOfBirth());
        candidate.setResumeUrl(storeResumeFile(resumeFile, email));
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

        Candidate saved = candidateRepository.save(candidate);

        emailService.sendProfilingCompletedEmail(email, request.getName());

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Candidate applied successfully for JD: {}", request.getJdId());
        }

        return mapToResponse(saved);
    }

    private String storeResumeFile(final MultipartFile resumeFile, final String email) {
        if (resumeFile == null || resumeFile.isEmpty()) {
            throw new IllegalArgumentException("Resume file is required");
        }

        try {
            String fileName = StringUtils.cleanPath(resumeFile.getOriginalFilename());
            String cleanedEmail = email.replaceAll("[^a-zA-Z0-9._-]", "_");
            String storedName = String.format("%s_%d_%s", cleanedEmail, System.currentTimeMillis(), fileName);

            Path uploadPath = Paths.get(resumeUploadDir).toAbsolutePath().normalize();
            Files.createDirectories(uploadPath);

            Path targetLocation = uploadPath.resolve(storedName);
            Files.copy(resumeFile.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return targetLocation.toString();
        } catch (IOException ex) {
            LOGGER.error("Failed to store resume file for email: {}", email, ex);
            throw new IllegalStateException("Could not store resume file. Please try again.", ex);
        }
    }

    /**
     * Retrieves the current application status of the candidate.
     *
     * @param email authenticated user's email (from JWT)
     * @return CandidateResponseDTO containing application status
     */
    @Override
    public CandidateResponseDTO getMyStatus(final String email) {

        LOGGER.info("Getting candidate status for email: {}", email);

        CandidateResponseDTO dto = new CandidateResponseDTO();

        Candidate candidate = candidateRepository.findByEmail(email).orElse(null);

        if (candidate == null) {
            LOGGER.warn("Candidate profile not found for email: {}", email);

            dto.setStatus(Stage.NOT_APPLIED);
            return dto;
        }

        dto.setId(candidate.getId());
        dto.setName(candidate.getName());
        dto.setEmail(candidate.getEmail());
        dto.setStatus(candidate.getStatus());
        dto.setJdId(candidate.getJdId());

        return dto;
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
