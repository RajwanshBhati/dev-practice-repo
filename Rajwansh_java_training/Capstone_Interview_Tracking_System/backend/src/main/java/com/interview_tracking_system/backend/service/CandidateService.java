package com.interview_tracking_system.backend.service;

import com.interview_tracking_system.backend.dto.LoginRequestDTO;
import com.interview_tracking_system.backend.dto.CandidateOnboardRequest;
import com.interview_tracking_system.backend.dto.CandidateProfileRequest;
import com.interview_tracking_system.backend.dto.CandidateRegisterRequest;
import com.interview_tracking_system.backend.dto.CandidateResponseDTO;
import com.interview_tracking_system.backend.entity.User;

/**
 * Service interface for candidate operations.
 */
public interface CandidateService {

    /**
     * Registers a new candidate user in the users table with ROLE_CANDIDATE.
     *
     * @param request the registration details
     */
    void register(CandidateRegisterRequest request);

    /**
     * Logs in a candidate.
     *
     * @param request the login credentials
     * @return the logged in User with ROLE_CANDIDATE
     */
    User login(LoginRequestDTO request);

    /**
     * Submits the candidate profiling form for a job application.
     *
     * @param request    the profile details
     * @param resumeFile the uploaded resume PDF
     * @param email      extracted from JWT
     * @return the saved candidate response
     */
    CandidateResponseDTO applyToJob(CandidateProfileRequest request,
            org.springframework.web.multipart.MultipartFile resumeFile, String email);

    /**
     * Returns the current application status of the logged in candidate.
     *
     * @param email extracted from JWT
     * @return the candidate response DTO with current stage
     */
    CandidateResponseDTO getMyStatus(String email);

    /**
     * Onboard candidate into the system.
     *
     * @param request the onboard request details
     */
    void onboardCandidate(CandidateOnboardRequest request);
}
