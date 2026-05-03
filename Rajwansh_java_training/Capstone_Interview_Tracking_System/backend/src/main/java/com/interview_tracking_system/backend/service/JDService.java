package com.interview_tracking_system.backend.service;

import com.interview_tracking_system.backend.dto.JDRequestDTO;
import com.interview_tracking_system.backend.dto.JDResponseDTO;
import com.interview_tracking_system.backend.enums.JDStatus;
import com.interview_tracking_system.backend.enums.JobType;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for Job Description operations.
 */
public interface JDService {

    /**
     * Create a new Job Description.
     *
     * @param requestDTO the job description request details
     * @return the created job description response
     */
    JDResponseDTO createJD(JDRequestDTO requestDTO);

    /**
     * Get a Job Description by its unique ID.
     *
     * @param id the UUID of the job description
     * @return the matching job description response
     */
    JDResponseDTO getJDById(UUID id);

    /**
     * Get all Job Descriptions.
     *
     * @return list of all job description responses
     */
    List<JDResponseDTO> getAllJDs();

    /**
     * Get all active Job Descriptions (status = ACTIVE).
     *
     * @return list of active job description responses
     */
    List<JDResponseDTO> getActiveJDs();

    /**
     * Update an existing Job Description by ID.
     *
     * @param id         the UUID of the job description to update
     * @param requestDTO the updated job description details
     * @return the updated job description response
     */
    JDResponseDTO updateJD(UUID id, JDRequestDTO requestDTO);

    /**
     * Update the status of a Job Description.
     *
     * @param id     the UUID of the job description to update
     * @param status the new status to set
     * @return the updated job description response
     */
    JDResponseDTO updateJDStatus(UUID id, JDStatus status);

    /**
     * Delete a Job Description by ID.
     *
     * @param id the UUID of the job description to delete
     */
    void deleteJD(UUID id);

    /**
     * Search Job Descriptions based on multiple criteria.
     *
     * @param status   the status to filter by
     * @param jobType  the job type to filter by
     * @param location the location to filter by
     * @param title    the job title to filter by
     * @return list of matching job description responses
     */
    List<JDResponseDTO> searchJDs(JDStatus status, JobType jobType, String location, String title);
}
