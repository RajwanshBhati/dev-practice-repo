package com.interviewtracker.interview_process_tracking.service;

import com.interviewtracker.interview_process_tracking.dto.JDRequestDTO;
import com.interviewtracker.interview_process_tracking.dto.JDResponseDTO;
import com.interviewtracker.interview_process_tracking.enums.JDStatus;
import com.interviewtracker.interview_process_tracking.enums.JobType;

import java.util.List;
import java.util.UUID;

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