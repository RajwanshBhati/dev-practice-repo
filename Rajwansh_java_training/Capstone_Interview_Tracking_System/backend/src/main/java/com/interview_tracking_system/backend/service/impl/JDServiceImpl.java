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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.stream.Collectors;
import java.util.Objects;

/**
 * Service implementation for Job Description module.
 * Handles all business logic related to JD CRUD operations.
 */
@Service
public class JDServiceImpl implements JDService {

    /** Logger instance. */
    private static final Logger LOGGER = LoggerFactory.getLogger(JDServiceImpl.class);

    /** Job description repository. */
    private final JobDescriptionRepository jdRepository;

    /** JD mapper. */
    private final JDMapper jdMapper;

    /**
     * Constructor injection.
     *
     * @param jdRepository job description repository
     * @param jdMapper     mapper
     */
    public JDServiceImpl(final JobDescriptionRepository jdRepository,
            final JDMapper jdMapper) {
        this.jdRepository = jdRepository;
        this.jdMapper = jdMapper;
    }

    /**
     * Create a new Job Description.
     *
     * @param requestDTO request data
     * @return created JD
     */
    @Override
    @Transactional
    public JDResponseDTO createJD(final JDRequestDTO requestDTO) {

        LOGGER.info("Creating new Job Description: {}", requestDTO.getJobTitle());

        validateExperienceRange(requestDTO.getMinExperience(), requestDTO.getMaxExperience());
        validateSalaryRange(requestDTO);

        JobDescription jd = jdMapper.toEntity(requestDTO);
        JobDescription saved = jdRepository.saveAndFlush(jd);

        LOGGER.info("Job Description created with ID: {}", saved.getId());
        return jdMapper.toResponseDTO(saved);
    }

    /**
     * Get Job Description by ID.
     *
     * @param id JD id
     * @return JD response
     */
    @Override
    public JDResponseDTO getJDById(final UUID id) {

        LOGGER.info("Fetching JD with ID: {}", id);

        JobDescription jd = findJDOrThrow(id);
        return jdMapper.toResponseDTO(jd);
    }

    /**
     * Get all Job Descriptions.
     *
     * @return list of JDs
     */
    @Override
    public List<JDResponseDTO> getAllJDs() {

        LOGGER.info("Fetching all Job Descriptions");

        return jdRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(jdMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get all active Job Descriptions.
     *
     * @return list of active JDs
     */
    @Override
    public List<JDResponseDTO> getActiveJDs() {

        LOGGER.info("Fetching ACTIVE Job Descriptions");

        return jdRepository.findByStatusOrderByCreatedAtDesc(JDStatus.ACTIVE)
                .stream()
                .map(jdMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Update existing Job Description.
     *
     * @param id         JD id
     * @param requestDTO request data
     * @return updated JD
     */
    @Override
    @Transactional
    public JDResponseDTO updateJD(final UUID id, final JDRequestDTO requestDTO) {

        LOGGER.info("Updating JD with ID: {}", id);

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

        LOGGER.info("JD updated successfully: {}", id);
        return jdMapper.toResponseDTO(updated);
    }

    /**
     * Update JD status.
     *
     * @param id     JD id
     * @param status status
     * @return updated JD
     */
    @Override
    @Transactional
    public JDResponseDTO updateJDStatus(final UUID id, final JDStatus status) {

        LOGGER.info("Updating status of JD {} to {}", id, status);

        JobDescription jd = findJDOrThrow(id);
        jd.setStatus(status);

        return jdMapper.toResponseDTO(jdRepository.save(jd));
    }

    /**
     * Delete Job Description.
     *
     * @param id JD id
     */
    @Override
    @Transactional
    public void deleteJD(final UUID id) {

        LOGGER.info("Deleting JD with ID: {}", id);

        JobDescription jd = findJDOrThrow(id);
        jdRepository.delete(jd);

        LOGGER.info("JD deleted: {}", id);
    }

    /**
     * Search Job Descriptions.
     *
     * @param status   status
     * @param jobType  job type
     * @param location location
     * @param title    title
     * @return list of JDs
     */
    @Override
    public List<JDResponseDTO> searchJDs(final JDStatus status,
            final JobType jobType,
            final String location,
            final String title) {

        LOGGER.info("Searching JDs");

        return jdRepository.searchJDs(status, jobType, location, title)
                .stream()
                .map(jdMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Find JD or throw exception if not found.
     *
     * @param id JD id
     * @return JD entity
     */
    private JobDescription findJDOrThrow(final UUID id) {
        return jdRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Job Description not found with ID: " + id));
    }

    /**
     * Validate experience range.
     *
     * @param min minimum experience
     * @param max maximum experience
     */
    private void validateExperienceRange(final Integer min, final Integer max) {
        if (Objects.nonNull(min)
                && Objects.nonNull(max)
                && min > max) {
            throw new InvalidRequestException(
                    "Min experience cannot be greater than max experience");
        }
    }

    /**
     * Validate salary range.
     *
     * @param dto request DTO
     */
    private void validateSalaryRange(final JDRequestDTO dto) {
        if (Objects.nonNull(dto.getMinSalary())
                && Objects.nonNull(dto.getMaxSalary())
                && dto.getMinSalary().compareTo(dto.getMaxSalary()) > 0) {
            throw new InvalidRequestException(
                    "Min salary cannot be greater than max salary");
        }
    }
}
