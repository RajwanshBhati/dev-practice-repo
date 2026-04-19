package com.interviewtracker.interview_process_tracking.service;

import com.interviewtracker.interview_process_tracking.dto.JDRequestDTO;
import com.interviewtracker.interview_process_tracking.dto.JDResponseDTO;
import com.interviewtracker.interview_process_tracking.enums.JDStatus;
import com.interviewtracker.interview_process_tracking.enums.JobType;

import java.util.List;
import java.util.UUID;


// Here I have defined the JDService interface, which declares the methods for managing job descriptions in the application. It includes methods for creating, retrieving, updating, and deleting job descriptions, as well as searching for job descriptions based on various criteria such as status, job type, location, and title.
public interface JDService {

    JDResponseDTO createJD(JDRequestDTO requestDTO);

    JDResponseDTO getJDById(UUID id);

    List<JDResponseDTO> getAllJDs();

    List<JDResponseDTO> getActiveJDs();

    JDResponseDTO updateJD(UUID id, JDRequestDTO requestDTO);

    JDResponseDTO updateJDStatus(UUID id, JDStatus status);

    void deleteJD(UUID id);

    List<JDResponseDTO> searchJDs(JDStatus status, JobType jobType, String location, String title);
}