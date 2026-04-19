package com.interviewtracker.interview_process_tracking.service;

import com.interviewtracker.interview_process_tracking.dto.JDRequestDTO;
import com.interviewtracker.interview_process_tracking.dto.JDResponseDTO;
import com.interviewtracker.interview_process_tracking.enums.JDStatus;
import com.interviewtracker.interview_process_tracking.enums.JobType;
import com.interviewtracker.interview_process_tracking.entity.JobDescription;
import com.interviewtracker.interview_process_tracking.exception.InvalidRequestException;
import com.interviewtracker.interview_process_tracking.exception.ResourceNotFoundException;
import com.interviewtracker.interview_process_tracking.mapper.JDMapper;
import com.interviewtracker.interview_process_tracking.repository.JobDescriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class JDServiceImpl implements JDService {

    // Repository layer is injected to handle all DB operations related to Job Description
    private final JobDescriptionRepository jdRepository;

    // Mapper is used to convert between Entity and DTO (clean separation of layers)
    private final JDMapper jdMapper;


    @Override
    @Transactional
    public JDResponseDTO createJD(JDRequestDTO requestDTO) {

        // Logging for debugging and tracking JD creation flow
        log.info("Creating JD: {}", requestDTO.getJobTitle());

        // Validate business rules before saving data to database
        validateRequest(requestDTO);

        // Convert incoming request DTO to Entity (DB format)
        JobDescription jd = jdMapper.toEntity(requestDTO);

        // Save entity into database
        JobDescription saved = jdRepository.save(jd);

        log.info("JD created with ID: {}", saved.getId());

        // Convert saved entity back to response DTO for API response
        return jdMapper.toResponseDTO(saved);
    }


    @Override
    public JDResponseDTO getJDById(UUID id) {

        // Fetch JD or throw exception if not found (fail-fast approach)
        JobDescription jd = findJDOrThrow(id);

        return jdMapper.toResponseDTO(jd);
    }


    @Override
    public List<JDResponseDTO> getAllJDs() {

        // Fetch all records and convert each entity to DTO using stream mapping
        return jdRepository.findAll()
                .stream()
                .map(jdMapper::toResponseDTO)
                .collect(Collectors.toList());
    }


    @Override
    public List<JDResponseDTO> getActiveJDs() {

        // Fetch only ACTIVE job descriptions (business filter at DB level)
        return jdRepository.findByStatus(JDStatus.ACTIVE)
                .stream()
                .map(jdMapper::toResponseDTO)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public JDResponseDTO updateJD(UUID id, JDRequestDTO requestDTO) {

        // Ensure request is valid before updating existing record
        validateRequest(requestDTO);

        // Fetch existing JD or fail if it doesn't exist
        JobDescription jd = findJDOrThrow(id);

        // Manually updating fields (partial control over updates)
        jd.setJobTitle(requestDTO.getJobTitle());
        jd.setJobDescription(requestDTO.getJobDescription());
        jd.setSkillsRequired(requestDTO.getSkillsRequired());
        jd.setExperience(requestDTO.getExperience());
        jd.setSalary(requestDTO.getSalary());
        jd.setLocation(requestDTO.getLocation());
        jd.setJobType(requestDTO.getJobType());

        // Save updated entity back to DB
        JobDescription updated = jdRepository.save(jd);

        return jdMapper.toResponseDTO(updated);
    }


    @Override
    @Transactional
    public JDResponseDTO updateJDStatus(UUID id, JDStatus status) {

        // Only update status field (lightweight update operation)
        JobDescription jd = findJDOrThrow(id);
        jd.setStatus(status);

        return jdMapper.toResponseDTO(jdRepository.save(jd));
    }


    @Override
    @Transactional
    public void deleteJD(UUID id) {

        // Ensure JD exists before deleting to avoid silent failures
        JobDescription jd = findJDOrThrow(id);

        jdRepository.delete(jd);

        log.info("JD deleted: {}", id);
    }


    @Override
    public List<JDResponseDTO> searchJDs(JDStatus status,
                                         JobType jobType,
                                         String location,
                                         String title) {

        // Advanced dynamic search using optional filters
        // All filters are optional and handled inside repository query
        return jdRepository.searchJDs(status, jobType, location, title)
                .stream()
                .map(jdMapper::toResponseDTO)
                .collect(Collectors.toList());
    }


    // Helper method to avoid repeating "not found" logic everywhere
    private JobDescription findJDOrThrow(UUID id) {
        return jdRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("JD not found: " + id));
    }


    // Business validation rules to maintain data integrity
    private void validateRequest(JDRequestDTO dto) {

        // Experience should never be negative in real-world job descriptions
        if (dto.getExperience() < 0) {
            throw new InvalidRequestException("Experience cannot be negative");
        }

        // Salary must be valid and non-negative
        if (dto.getSalary() == null ||
                dto.getSalary().doubleValue() < 0) {
            throw new InvalidRequestException("Salary cannot be negative");
        }
    }
}