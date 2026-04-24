package com.interview_tracking_system.backend.service;

import com.interview_tracking_system.backend.dto.JDRequestDTO;
import com.interview_tracking_system.backend.dto.JDResponseDTO;
import com.interview_tracking_system.backend.enums.JDStatus;
import com.interview_tracking_system.backend.enums.JobType;

import java.util.List;
import java.util.UUID;

public interface JDService {

    /**
     * Create a new Job Description.
     */
    JDResponseDTO createJD(JDRequestDTO requestDTO);

    /**
     * Get a Job Description by its unique ID.
     */
    JDResponseDTO getJDById(UUID id);

    /**
     * Get all Job Descriptions.
     * 
     * @return List of JDResponseDTO
     */
    List<JDResponseDTO> getAllJDs();

    /**
     * Get all active Job Descriptions (status = ACTIVE).
     * 
     * @return List of active JDResponseDTO
     */

    List<JDResponseDTO> getActiveJDs();

    /**
     * Update an existing Job Description by ID.
     * 
     * @param id the UUID of the Job Description to update
     */

    JDResponseDTO updateJD(UUID id, JDRequestDTO requestDTO);

    /**
     * Update the status of a Job Description.
     * 
     * @param id the UUID of the Job Description to update
     */
    JDResponseDTO updateJDStatus(UUID id, JDStatus status);

    /**
     * Delete a Job Description by ID.
     * 
     * @param id the UUID of the Job Description to delete
     */

    void deleteJD(UUID id);

    /**
     * Search Job Descriptions based on multiple criteria.
     * 
     * @param status
     * @param jobType
     * @param location
     * @param title
     * @return
     */

    List<JDResponseDTO> searchJDs(JDStatus status, JobType jobType, String location, String title);
}
