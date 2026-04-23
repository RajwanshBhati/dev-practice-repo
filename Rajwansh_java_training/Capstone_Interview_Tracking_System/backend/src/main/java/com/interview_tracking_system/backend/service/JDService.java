package com.interview_tracking_system.backend.service;

import com.interview_tracking_system.backend.dto.JDRequestDTO;
import com.interview_tracking_system.backend.dto.JDResponseDTO;
import com.interview_tracking_system.backend.enums.JDStatus;
import com.interview_tracking_system.backend.enums.JobType;

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
