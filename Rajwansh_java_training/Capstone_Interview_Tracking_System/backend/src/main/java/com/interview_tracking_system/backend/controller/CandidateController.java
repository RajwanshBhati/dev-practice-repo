package com.interview_tracking_system.backend.controller;

import com.interview_tracking_system.backend.constants.ApiMessages;
import com.interview_tracking_system.backend.constants.CandidateApiConstants;
import com.interview_tracking_system.backend.dto.CandidateProfileRequest;
import com.interview_tracking_system.backend.dto.CandidateRegisterRequest;
import com.interview_tracking_system.backend.dto.CandidateResponseDTO;
import com.interview_tracking_system.backend.dto.LoginRequestDTO;
import com.interview_tracking_system.backend.entity.CandidateUser;
import com.interview_tracking_system.backend.security.JwtUtil;
import com.interview_tracking_system.backend.service.CandidateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping(CandidateApiConstants.BASE_URL)
public class CandidateController {

    /**
     * Logger instance for logging controller activities.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CandidateController.class);

    /**
     * Service layer for candidate operations.
     */
    private final CandidateService candidateService;

    /**
     * Utility class for handling JWT operations.
     */
    private final JwtUtil jwtUtil;

    /**
     * Constructor for dependency injection.
     *
     * @param candidateService service handling candidate business logic
     * @param jwtUtil          utility for JWT generation and validation
     */
    public CandidateController(final CandidateService candidateService,
            final JwtUtil jwtUtil) {
        this.candidateService = candidateService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Registers a new candidate.
     *
     * @param request request body containing registration details
     * @return success message with HTTP 201 status
     */
    @PostMapping(CandidateApiConstants.REGISTER_URL)
    public ResponseEntity<String> register(
            @RequestBody final CandidateRegisterRequest request) {

        LOGGER.info("Register request received for email: {}", request.getEmail());

        candidateService.register(request);

        LOGGER.info("Candidate registered successfully: {}", request.getEmail());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiMessages.REGISTER_SUCCESS);
    }

    /**
     * Authenticates a candidate and returns a JWT token.
     *
     * @param request login request containing email and password
     * @return JWT token if authentication is successful
     */
    @PostMapping(CandidateApiConstants.LOGIN_URL)
    public ResponseEntity<String> login(
            @RequestBody final LoginRequestDTO request) {

        LOGGER.info("Login request received for email: {}", request.getEmail());

        CandidateUser user = candidateService.login(request);

        String token = jwtUtil.generateAccessToken(
                user.getEmail(),
                "CANDIDATE");

        LOGGER.info("Login successful for email: {}", user.getEmail());

        return ResponseEntity.ok(token);
    }

    /**
     * Applies the logged-in candidate to a job using JWT authentication.
     *
     * @param request    request body containing candidate profile details
     * @param authHeader Authorization header containing Bearer token
     * @return saved candidate response with HTTP 201 status
     */
    @PostMapping(CandidateApiConstants.APPLY_URL)
    public ResponseEntity<CandidateResponseDTO> applyToJob(
            @RequestBody final CandidateProfileRequest request,
            @RequestHeader("Authorization") final String authHeader) {

        LOGGER.info("Apply job request received");

        String token = authHeader.replace("Bearer ", "");
        String email = jwtUtil.extractEmail(token);

        LOGGER.info("Applying job for email: {}", email);

        CandidateResponseDTO response = candidateService.applyToJob(request, email);

        LOGGER.info("Application submitted successfully for email: {}", email);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Fetches the current application status of the logged-in candidate.
     *
     * @param authHeader Authorization header containing Bearer token
     * @return candidate application status
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
     * Logs out the candidate.
     *
     * Note: Since JWT is stateless, logout is handled on client-side
     * by discarding the token.
     *
     * @return logout success message
     */
    @PostMapping(CandidateApiConstants.LOGOUT_URL)
    public ResponseEntity<String> logout() {

        LOGGER.info("Logout request received");

        return ResponseEntity.ok(ApiMessages.LOGOUT_SUCCESS);
    }
}
