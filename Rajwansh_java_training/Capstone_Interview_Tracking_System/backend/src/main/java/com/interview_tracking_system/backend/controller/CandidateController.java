package com.interview_tracking_system.backend.controller;

import com.interview_tracking_system.backend.constants.ApiMessages;
import com.interview_tracking_system.backend.constants.CandidateApiConstants;
import com.interview_tracking_system.backend.dto.CandidateProfileRequest;
import com.interview_tracking_system.backend.dto.CandidateRegisterRequest;
import com.interview_tracking_system.backend.dto.CandidateResponseDTO;
import com.interview_tracking_system.backend.dto.LoginRequestDTO;
import com.interview_tracking_system.backend.entity.User;
import com.interview_tracking_system.backend.security.JwtUtil;
import com.interview_tracking_system.backend.service.CandidateService;
import jakarta.validation.Valid;
import java.beans.PropertyEditorSupport;
import java.util.Objects;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.interview_tracking_system.backend.dto.CandidateOnboardRequest;

/**
 * Controller for candidate operations.
 */
@RestController
@RequestMapping(CandidateApiConstants.BASE_URL)
public class CandidateController {

    /**
     * Logger instance.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CandidateController.class);

    /**
     * Candidate service.
     */
    private final CandidateService candidateService;

    /**
     * JWT utility.
     */
    private final JwtUtil jwtUtil;

    /**
     * Constructor for dependency injection.
     *
     * @param candidateService the candidate service
     * @param jwtUtil          the JWT utility
     */
    public CandidateController(final CandidateService candidateService,
            final JwtUtil jwtUtil) {
        this.candidateService = candidateService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Initializes binder for UUID conversion.
     *
     * @param binder the web data binder
     */
    @InitBinder
    public void initBinder(final WebDataBinder binder) {
        binder.registerCustomEditor(UUID.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(final String text) {

                final String value = Objects.isNull(text) ? "" : text.trim();

                if (value.isEmpty() || "NaN".equalsIgnoreCase(value)) {
                    setValue(null);
                    return;
                }

                setValue(UUID.fromString(value));
            }
        });
    }

    /**
     * Registers a new candidate.
     *
     * @param request request body
     * @return success message
     */
    @PostMapping(CandidateApiConstants.REGISTER_URL)
    public ResponseEntity<String> register(
            @Valid @RequestBody final CandidateRegisterRequest request) {

        LOGGER.info("Register request received for email: {}", request.getEmail());

        candidateService.register(request);

        LOGGER.info("Candidate registered successfully: {}", request.getEmail());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiMessages.REGISTER_SUCCESS);
    }

    /**
     * Logs in a candidate.
     *
     * @param request login request
     * @return JWT token
     */
    @PostMapping(CandidateApiConstants.LOGIN_URL)
    public ResponseEntity<String> login(@Valid @RequestBody final LoginRequestDTO request) {

        LOGGER.info("Login request received for email: {}", request.getEmail());

        User user = candidateService.login(request);

        String token = jwtUtil.generateAccessToken(
                user.getEmail(),
                "CANDIDATE");

        LOGGER.info("Login successful for email: {}", user.getEmail());

        return ResponseEntity.ok(token);
    }

    /**
     * Applies candidate to a job.
     *
     * @param request    profile request
     * @param resumeFile resume file
     * @param authHeader authorization header
     * @return response DTO
     */
    @PostMapping(value = CandidateApiConstants.APPLY_URL, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CandidateResponseDTO> applyToJob(
            @ModelAttribute final CandidateProfileRequest request,
            @RequestPart("resumeFile") final MultipartFile resumeFile,
            @RequestHeader("Authorization") final String authHeader) {

        LOGGER.info("Apply job request received");

        String token = authHeader.replace("Bearer ", "");
        String email = jwtUtil.extractEmail(token);

        LOGGER.info("Applying job for email: {}", email);

        CandidateResponseDTO response = candidateService.applyToJob(request, resumeFile, email);

        LOGGER.info("Application submitted successfully for email: {}", email);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Fetches candidate application status.
     *
     * @param authHeader authorization header
     * @return candidate response
     */
    @GetMapping(CandidateApiConstants.STATUS_URL)
    public ResponseEntity<CandidateResponseDTO> getMyStatus(
            @RequestHeader("Authorization") final String authHeader) {

        LOGGER.info("Status request received");

        String token = authHeader.replace("Bearer ", "");
        String email = jwtUtil.extractEmail(token);

        LOGGER.info("Fetching status for email: {}", email);

        CandidateResponseDTO response = candidateService.getMyStatus(email);

        return ResponseEntity.ok(response);
    }

    /**
     * Logs out candidate.
     *
     * @return logout message
     */
    @PostMapping(CandidateApiConstants.LOGOUT_URL)
    public ResponseEntity<String> logout() {

        LOGGER.info("Logout request received");

        return ResponseEntity.ok(ApiMessages.LOGOUT_SUCCESS);
    }

    /**
     * Onboards candidate by HR.
     *
     * @param request onboard request
     * @return success message
     */
    @PostMapping("/onboard")
    public ResponseEntity<String> onboardCandidate(
            @Valid @RequestBody final CandidateOnboardRequest request) {

        LOGGER.info("HR onboard candidate request received for email: {}", request.getEmail());

        candidateService.onboardCandidate(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Candidate onboarded successfully. Email sent.");
    }
}
