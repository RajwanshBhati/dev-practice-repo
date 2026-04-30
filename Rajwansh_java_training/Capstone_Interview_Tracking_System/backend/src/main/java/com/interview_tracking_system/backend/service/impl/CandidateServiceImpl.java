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
import com.interview_tracking_system.backend.dto.CandidateOnboardRequest;
import java.time.LocalDateTime;
import java.util.UUID;

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

        String fullName = request.getFullName() == null ? "" : request.getFullName().trim();
        String email = request.getEmail() == null ? "" : request.getEmail().trim().toLowerCase();
        String mobileNumber = request.getMobileNumber() == null ? "" : request.getMobileNumber().trim();

        if (fullName.isEmpty()
                || email.isEmpty()
                || mobileNumber.isEmpty()
                || request.getDob() == null
                || request.getGender() == null
                || request.getPassword() == null
                || request.getConfirmPassword() == null) {
            throw new IllegalArgumentException("All fields are required.");
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException(ERROR_PASSWORD_MISMATCH);
        }

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(ERROR_EMAIL_EXISTS);
        }

        if (userRepository.existsByMobile(mobileNumber)) {
            throw new IllegalArgumentException("Mobile number already exists.");
        }

        User user = new User();
        user.setName(fullName);
        user.setEmail(email);
        user.setMobile(mobileNumber);
        user.setDateOfBirth(request.getDob());
        user.setGender(request.getGender());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.CANDIDATE);
        user.setStatus(UserStatus.ACTIVE);

        userRepository.save(user);
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
        String mobileCode = request.getMobileCode() == null ? "" : request.getMobileCode().trim();
        String mobileNumber = request.getMobileNumber() == null ? "" : request.getMobileNumber().trim();

        if (!mobileNumber.matches("^[6-9]\\d{9}$")) {
            throw new IllegalArgumentException("Enter a valid 10-digit mobile number.");
        }

        String fullMobile = mobileCode + mobileNumber;
        if (candidateRepository.existsByEmailIgnoreCaseAndStatusNot(email, Stage.REJECTED)) {

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
        candidate.setEmail(email.trim().toLowerCase());

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

            return "/api/resumes/" + storedName;
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

        Candidate candidate = candidateRepository
                .findTopByEmailIgnoreCaseOrderByIdDesc(email)
                .orElse(null);

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

    @Override
    public void onboardCandidate(final CandidateOnboardRequest request) {

        /*
         * Trim input values to remove extra spaces.
         * Email is converted to lowercase to avoid duplicate case issues.
         */
        String fullName = request.getFullName() == null ? "" : request.getFullName().trim();
        String email = request.getEmail() == null ? "" : request.getEmail().trim().toLowerCase();
        String mobileNumber = request.getMobileNumber() == null ? "" : request.getMobileNumber().trim();

        /*
         * Validate mandatory fields.
         * Candidate cannot be onboarded if any required field is missing.
         */
        if (fullName.isEmpty()
                || email.isEmpty()
                || mobileNumber.isEmpty()
                || request.getDob() == null
                || request.getGender() == null) {
            throw new IllegalArgumentException("All fields are required.");
        }

        /*
         * Check whether email is already registered in the system.
         */
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists.");
        }

        /*
         * Check whether mobile number is already registered in the system.
         */
        if (userRepository.existsByMobile(mobileNumber)) {
            throw new IllegalArgumentException("Mobile number already exists.");
        }

        /*
         * Generate temporary password and activation token.
         * Temporary password is sent to candidate through email.
         * Activation token is used to activate the account securely.
         */
        String temporaryPassword = "TEMP@" + UUID.randomUUID().toString().substring(0, 8);
        String activationToken = UUID.randomUUID().toString();

        /*
         * Create a new user account for the candidate.
         * Candidate status remains PENDING until account activation.
         */
        User user = new User();
        user.setName(fullName);
        user.setEmail(email);
        user.setMobile(mobileNumber);
        user.setDateOfBirth(request.getDob());
        user.setGender(request.getGender());
        user.setPassword(passwordEncoder.encode(temporaryPassword));
        user.setRole(Role.CANDIDATE);
        user.setStatus(UserStatus.PENDING);
        user.setActivationToken(activationToken);
        user.setActivationTokenExpiry(LocalDateTime.now().plusHours(24));

        /*
         * Save candidate user details into the database.
         */
        userRepository.save(user);

        /*
         * Send onboarding email with temporary password and activation token.
         */
        emailService.sendCandidateOnboardEmail(
                email,
                fullName,
                temporaryPassword,
                activationToken);
    }
}
