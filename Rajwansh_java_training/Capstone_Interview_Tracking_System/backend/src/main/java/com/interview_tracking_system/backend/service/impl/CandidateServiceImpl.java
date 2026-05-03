package com.interview_tracking_system.backend.service.impl;

import static com.interview_tracking_system.backend.constants.CandidateConstants.ERROR_ALREADY_APPLIED;
import static com.interview_tracking_system.backend.constants.CandidateConstants.ERROR_EMAIL_EXISTS;
import static com.interview_tracking_system.backend.constants.CandidateConstants.ERROR_INVALID_CREDENTIALS;
import static com.interview_tracking_system.backend.constants.CandidateConstants.ERROR_NOT_LOGGED_IN;

import com.interview_tracking_system.backend.dto.CandidateOnboardRequest;
import com.interview_tracking_system.backend.dto.CandidateProfileRequest;
import com.interview_tracking_system.backend.dto.CandidateRegisterRequest;
import com.interview_tracking_system.backend.dto.CandidateResponseDTO;
import com.interview_tracking_system.backend.dto.LoginRequestDTO;
import com.interview_tracking_system.backend.entity.Candidate;
import com.interview_tracking_system.backend.entity.User;
import com.interview_tracking_system.backend.enums.Role;
import com.interview_tracking_system.backend.enums.Stage;
import com.interview_tracking_system.backend.enums.UserStatus;
import com.interview_tracking_system.backend.repository.CandidateRepository;
import com.interview_tracking_system.backend.repository.UserRepository;
import com.interview_tracking_system.backend.service.CandidateService;
import com.interview_tracking_system.backend.service.EmailService;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service implementation for candidate operations.
 */
@Service
public class CandidateServiceImpl implements CandidateService {

    /**
     * Activation token expiry hours.
     */
    private static final int ACTIVATION_EXPIRY_HOURS = 24;

    /**
     * Mobile number validation pattern.
     */
    private static final String MOBILE_PATTERN = "^[6-9]\\d{9}$";

    /**
     * Resume URL prefix.
     */
    private static final String RESUME_API_PREFIX = "/api/resumes/";

    /**
     * Logger instance.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CandidateServiceImpl.class);

    /**
     * Resume upload directory.
     */
    @Value("${resume.upload-dir:uploads/resumes}")
    private String resumeUploadDir;

    /**
     * User repository.
     */
    private final UserRepository userRepository;

    /**
     * Candidate repository.
     */
    private final CandidateRepository candidateRepository;

    /**
     * Email service.
     */
    private final EmailService emailService;

    /**
     * Password encoder.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructs candidate service implementation.
     *
     * @param injectedUserRepository      user repository
     * @param injectedCandidateRepository candidate repository
     * @param injectedEmailService        email service
     * @param injectedPasswordEncoder     password encoder
     */
    public CandidateServiceImpl(
            final UserRepository injectedUserRepository,
            final CandidateRepository injectedCandidateRepository,
            final EmailService injectedEmailService,
            final PasswordEncoder injectedPasswordEncoder) {

        this.userRepository = injectedUserRepository;
        this.candidateRepository = injectedCandidateRepository;
        this.emailService = injectedEmailService;
        this.passwordEncoder = injectedPasswordEncoder;
    }

    /**
     * Registers a candidate user.
     *
     * @param request candidate registration request
     */
    @Override
    public void register(final CandidateRegisterRequest request) {
        LOGGER.info(
                "Candidate registration request received for email: {}",
                request.getEmail());

        String fullName = normalize(request.getFullName());
        String email = normalizeEmail(request.getEmail());
        String mobileNumber = normalize(request.getMobileNumber());

        validateCandidateBasicFields(
                fullName,
                email,
                mobileNumber,
                request.getDob(),
                request.getGender());

        if (userRepository.existsByEmail(email)) {
            LOGGER.warn(
                    "Candidate registration failed. Email already exists: {}",
                    email);
            throw new IllegalArgumentException(ERROR_EMAIL_EXISTS);
        }

        if (userRepository.existsByMobile(mobileNumber)) {
            LOGGER.warn(
                    "Candidate registration failed. Mobile already exists: {}",
                    mobileNumber);
            throw new IllegalArgumentException("Mobile number already exists.");
        }

        String activationToken = UUID.randomUUID().toString();

        User user = new User();
        user.setName(fullName);
        user.setEmail(email);
        user.setMobile(mobileNumber);
        user.setDateOfBirth(request.getDob());
        user.setGender(request.getGender());
        user.setPassword(null);
        user.setRole(Role.CANDIDATE);
        user.setStatus(UserStatus.ACTIVE);
        user.setActivationToken(activationToken);
        user.setActivationTokenExpiry(
                LocalDateTime.now().plusHours(ACTIVATION_EXPIRY_HOURS));

        userRepository.save(user);

        emailService.sendCandidateRegistrationEmail(
                email,
                fullName,
                activationToken);

        LOGGER.info("Candidate registered successfully with email: {}", email);
    }

    /**
     * Logs in a candidate user.
     *
     * @param request login request
     * @return authenticated user
     */
    @Override
    public User login(final LoginRequestDTO request) {
        LOGGER.info("Login attempt for email: {}", request.getEmail());

        User user = userRepository.findByEmailIgnoreCase(request.getEmail())
                .orElseThrow(() -> {
                    LOGGER.error("User not found: {}", request.getEmail());
                    return new IllegalArgumentException(
                            ERROR_INVALID_CREDENTIALS);
                });

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {
            LOGGER.error("Invalid password for email: {}", request.getEmail());
            throw new IllegalArgumentException(ERROR_INVALID_CREDENTIALS);
        }

        LOGGER.info("Login successful for email: {}", request.getEmail());
        return user;
    }

    /**
     * Applies candidate to a job.
     *
     * @param request    candidate profile request
     * @param resumeFile resume file
     * @param email      authenticated email
     * @return candidate response
     */
    @Override
    public CandidateResponseDTO applyToJob(
            final CandidateProfileRequest request,
            final MultipartFile resumeFile,
            final String email) {

        LOGGER.info("Apply job request received for email: {}", email);

        userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> {
                    LOGGER.error("User not found: {}", email);
                    return new IllegalStateException(ERROR_NOT_LOGGED_IN);
                });

        String mobileCode = normalize(request.getMobileCode());
        String mobileNumber = normalize(request.getMobileNumber());

        if (!mobileNumber.matches(MOBILE_PATTERN)) {
            throw new IllegalArgumentException(
                    "Enter a valid 10-digit mobile number.");
        }

        String fullMobile = mobileCode + mobileNumber;

        Candidate existingCandidate = candidateRepository
                .findTopByEmailIgnoreCaseOrderByIdDesc(email)
                .orElse(null);

        Candidate mobileCandidate = candidateRepository
                .findTopByMobileOrderByIdDesc(fullMobile)
                .orElse(null);

        if (mobileCandidate != null
                && !mobileCandidate.getEmail().equalsIgnoreCase(email)
                && mobileCandidate.getStatus() != Stage.REJECTED) {
            throw new IllegalArgumentException("Mobile number already exists.");
        }

        if (existingCandidate != null
                && existingCandidate.getStatus() != Stage.REJECTED) {
            LOGGER.error("Candidate already has active application: {}", email);
            throw new IllegalStateException(ERROR_ALREADY_APPLIED);
        }

        if (request.getJdId() == null) {
            LOGGER.error("Invalid or missing jdId for email: {}", email);
            throw new IllegalArgumentException("Please provide a valid jdId.");
        }

        Candidate candidate = existingCandidate != null ? existingCandidate : new Candidate();

        candidate.setName(request.getName());
        candidate.setEmail(email.trim().toLowerCase());
        candidate.setRejectedStage(null);
        candidate.setMobile(fullMobile);
        candidate.setDateOfBirth(request.getDateOfBirth());

        LOGGER.info("Storing resume file for candidate email: {}", email);
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

        Candidate savedCandidate = candidateRepository.save(candidate);

        emailService.sendProfilingCompletedEmail(email, request.getName());

        LOGGER.info(
                "Candidate applied successfully for JD: {}",
                request.getJdId());

        return mapToResponse(savedCandidate);
    }

    /**
     * Returns candidate application status.
     *
     * @param email authenticated email
     * @return candidate response
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
        dto.setCurrentStage(candidate.getStatus());
        dto.setRejectedStage(candidate.getRejectedStage());

        return dto;
    }

    /**
     * Onboards candidate by HR.
     *
     * @param request candidate onboard request
     */
    @Override
    public void onboardCandidate(final CandidateOnboardRequest request) {
        String fullName = normalize(request.getFullName());
        String email = normalizeEmail(request.getEmail());
        String mobileNumber = normalize(request.getMobileNumber());

        validateCandidateBasicFields(
                fullName,
                email,
                mobileNumber,
                request.getDob(),
                request.getGender());

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists.");
        }

        if (userRepository.existsByMobile(mobileNumber)) {
            throw new IllegalArgumentException("Mobile number already exists.");
        }

        String activationToken = UUID.randomUUID().toString();

        User user = new User();
        user.setName(fullName);
        user.setEmail(email);
        user.setMobile(mobileNumber);
        user.setDateOfBirth(request.getDob());
        user.setGender(request.getGender());
        user.setPassword(null);
        user.setRole(Role.CANDIDATE);
        user.setStatus(UserStatus.PENDING);
        user.setActivationToken(activationToken);
        user.setActivationTokenExpiry(
                LocalDateTime.now().plusHours(ACTIVATION_EXPIRY_HOURS));

        userRepository.save(user);

        emailService.sendCandidateOnboardEmail(
                email,
                fullName,
                activationToken);
    }

    /**
     * Stores resume file.
     *
     * @param resumeFile resume file
     * @param email      candidate email
     * @return resume URL
     */
    private String storeResumeFile(
            final MultipartFile resumeFile,
            final String email) {

        if (resumeFile == null || resumeFile.isEmpty()) {
            LOGGER.warn(
                    "Resume upload failed. Empty resume file for email: {}",
                    email);
            throw new IllegalArgumentException("Resume file is required");
        }

        String originalFileName = resumeFile.getOriginalFilename();

        if (originalFileName == null || originalFileName.isBlank()) {
            LOGGER.warn(
                    "Resume upload failed. Invalid file name for email: {}",
                    email);
            throw new IllegalArgumentException(
                    "Resume file name is required");
        }

        try {
            String fileName = StringUtils.cleanPath(originalFileName);
            String cleanedEmail = email.replaceAll("[^a-zA-Z0-9._-]", "_");
            String storedName = String.format(
                    "%s_%d_%s",
                    cleanedEmail,
                    System.currentTimeMillis(),
                    fileName);

            Path uploadPath = Paths.get(resumeUploadDir)
                    .toAbsolutePath()
                    .normalize();

            Files.createDirectories(uploadPath);

            Path targetLocation = uploadPath.resolve(storedName);

            Files.copy(
                    resumeFile.getInputStream(),
                    targetLocation,
                    StandardCopyOption.REPLACE_EXISTING);

            LOGGER.info(
                    "Resume stored successfully for email: {} with file name: {}",
                    email,
                    storedName);

            return RESUME_API_PREFIX + storedName;
        } catch (IOException ex) {
            LOGGER.error("Failed to store resume file for email: {}", email, ex);
            throw new IllegalStateException(
                    "Could not store resume file. Please try again.",
                    ex);
        }
    }

    /**
     * Maps candidate entity to DTO.
     *
     * @param candidate candidate entity
     * @return candidate response
     */
    private CandidateResponseDTO mapToResponse(final Candidate candidate) {
        CandidateResponseDTO dto = new CandidateResponseDTO();
        dto.setId(candidate.getId());
        dto.setName(candidate.getName());
        dto.setEmail(candidate.getEmail());
        dto.setStatus(candidate.getStatus());
        dto.setJdId(candidate.getJdId());
        dto.setCurrentStage(candidate.getStatus());
        dto.setRejectedStage(candidate.getRejectedStage());

        return dto;
    }

    /**
     * Normalizes text value.
     *
     * @param value input value
     * @return normalized value
     */
    private String normalize(final String value) {
        return value == null ? "" : value.trim();
    }

    /**
     * Normalizes email value.
     *
     * @param email email value
     * @return normalized email
     */
    private String normalizeEmail(final String email) {
        return email == null ? "" : email.trim().toLowerCase();
    }

    /**
     * Validates candidate basic fields.
     *
     * @param fullName     full name
     * @param email        email
     * @param mobileNumber mobile number
     * @param dob          date of birth
     * @param gender       gender
     */
    private void validateCandidateBasicFields(
            final String fullName,
            final String email,
            final String mobileNumber,
            final Object dob,
            final Object gender) {

        if (fullName.isEmpty()
                || email.isEmpty()
                || mobileNumber.isEmpty()
                || dob == null
                || gender == null) {
            LOGGER.warn(
                    "Candidate validation failed due to missing fields for email: {}",
                    email);
            throw new IllegalArgumentException("All fields are required.");
        }
    }
}
