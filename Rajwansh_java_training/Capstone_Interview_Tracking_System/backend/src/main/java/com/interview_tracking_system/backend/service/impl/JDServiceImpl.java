package com.interview_tracking_system.backend.service.impl;

import com.interview_tracking_system.backend.dto.JDRequestDTO;
import com.interview_tracking_system.backend.dto.JDResponseDTO;
import com.interview_tracking_system.backend.enums.JDStatus;
import com.interview_tracking_system.backend.enums.JobType;
import com.interview_tracking_system.backend.entity.JobDescription;
import com.interview_tracking_system.backend.exception.InvalidRequestException;
import com.interview_tracking_system.backend.exception.ResourceNotFoundException;
import com.interview_tracking_system.backend.mapper.JDMapper;
import com.interview_tracking_system.backend.repository.JobDescriptionRepository;
import com.interview_tracking_system.backend.service.JDService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.Objects;

/**
 * Service implementation for Job Description module.
 * Handles all business logic related to JD CRUD operations.
 */
@Service
public class JDServiceImpl implements JDService {

    private static final Logger log = Logger.getLogger(JDServiceImpl.class.getName());

    private final JobDescriptionRepository jdRepository;
    private final JDMapper jdMapper;

    /**
     * Constructor injection
     */
    public JDServiceImpl(JobDescriptionRepository jdRepository,
            JDMapper jdMapper) {
        this.jdRepository = jdRepository;
        this.jdMapper = jdMapper;
    }

    /**
     * Create a new Job Description.
     */
    @Override
    @Transactional
    public JDResponseDTO createJD(JDRequestDTO requestDTO) {

        log.info("Creating new Job Description: " + requestDTO.getJobTitle());

        validateExperienceRange(requestDTO.getMinExperience(), requestDTO.getMaxExperience());
        validateSalaryRange(requestDTO);

        JobDescription jd = jdMapper.toEntity(requestDTO);
        JobDescription saved = jdRepository.save(jd);

        log.info("Job Description created with ID: " + saved.getId());
        return jdMapper.toResponseDTO(saved);
    }

    /**
     * Get Job Description by ID.
     */
    @Override
    public JDResponseDTO getJDById(UUID id) {

        log.info("Fetching JD with ID: " + id);

        JobDescription jd = findJDOrThrow(id);
        return jdMapper.toResponseDTO(jd);
    }

    /**
     * Get all Job Descriptions.
     */
    @Override
    public List<JDResponseDTO> getAllJDs() {

        log.info("Fetching all Job Descriptions");

        return jdRepository.findAll()
                .stream()
                .map(jdMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get all active Job Descriptions.
     */
    @Override
    public List<JDResponseDTO> getActiveJDs() {

        log.info("Fetching ACTIVE Job Descriptions");

        return jdRepository.findByStatus(JDStatus.ACTIVE)
                .stream()
                .map(jdMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Update existing Job Description.
     */
    @Override
    @Transactional
    public JDResponseDTO updateJD(UUID id, JDRequestDTO requestDTO) {

        log.info("Updating JD with ID: " + id);

        validateExperienceRange(requestDTO.getMinExperience(), requestDTO.getMaxExperience());
        validateSalaryRange(requestDTO);

        JobDescription jd = findJDOrThrow(id);

        jd.setJobTitle(requestDTO.getJobTitle());
        jd.setJobDescription(requestDTO.getJobDescription());
        jd.setSkillsRequired(requestDTO.getSkillsRequired());
        jd.setExperienceMin(requestDTO.getMinExperience());
        jd.setExperienceMax(requestDTO.getMaxExperience());
        jd.setSalaryMin(requestDTO.getMinSalary());
        jd.setSalaryMax(requestDTO.getMaxSalary());
        jd.setLocation(requestDTO.getLocation());
        jd.setJobType(requestDTO.getJobType());

        JobDescription updated = jdRepository.save(jd);

        log.info("JD updated successfully: " + id);
        return jdMapper.toResponseDTO(updated);
    }

    /**
     * Update JD status.
     */
    @Override
    @Transactional
    public JDResponseDTO updateJDStatus(UUID id, JDStatus status) {

        log.info("Updating status of JD " + id + " to " + status);

        JobDescription jd = findJDOrThrow(id);
        jd.setStatus(status);

        return jdMapper.toResponseDTO(jdRepository.save(jd));
    }

    /**
     * Delete Job Description.
     */
    @Override
    @Transactional
    public void deleteJD(UUID id) {

        log.info("Deleting JD with ID: " + id);

        JobDescription jd = findJDOrThrow(id);
        jdRepository.delete(jd);

        log.info("JD deleted: " + id);
    }

    /**
     * Search Job Descriptions.
     */
    @Override
    public List<JDResponseDTO> searchJDs(JDStatus status, JobType jobType,
            String location, String title) {

        log.info("Searching JDs");

        return jdRepository.searchJDs(status, jobType, location, title)
                .stream()
                .map(jdMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Find JD or throw exception if not found.
     */
    private JobDescription findJDOrThrow(UUID id) {
        return jdRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Job Description not found with ID: " + id));
    }

    /**
     * Validate experience range.
     */
    private void validateExperienceRange(Integer min, Integer max) {
        if (Objects.nonNull(min) && Objects.nonNull(max) && min > max) {
            throw new InvalidRequestException(
                    "Min experience cannot be greater than max experience");
        }
    }

    /**
     * Validate salary range.
     */
    private void validateSalaryRange(JDRequestDTO dto) {
        if (Objects.nonNull(dto.getMinSalary()) &&
                Objects.nonNull(dto.getMaxSalary()) &&
                dto.getMinSalary().compareTo(dto.getMaxSalary()) > 0) {
            throw new InvalidRequestException(
                    "Min salary cannot be greater than max salary");
        }
    }
}
