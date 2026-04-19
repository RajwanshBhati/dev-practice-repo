package com.interviewtracker.interview_process_tracking.service;

import com.interviewtracker.interview_process_tracking.dto.JDRequestDTO;
import com.interviewtracker.interview_process_tracking.dto.JDResponseDTO;
import com.interviewtracker.interview_process_tracking.enum.JDStatus;
import com.interviewtracker.interview_process_tracking.enum.JobType;
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

    private final JobDescriptionRepository jdRepository;
    private final JDMapper jdMapper;


    @Override
    @Transactional
    public JDResponseDTO createJD(JDRequestDTO requestDTO) {

        log.info("Creating JD: {}", requestDTO.getJobTitle());

        validateRequest(requestDTO);

        JobDescription jd = jdMapper.toEntity(requestDTO);
        JobDescription saved = jdRepository.save(jd);

        log.info("JD created with ID: {}", saved.getId());

        return jdMapper.toResponseDTO(saved);
    }


    @Override
    public JDResponseDTO getJDById(UUID id) {

        JobDescription jd = findJDOrThrow(id);
        return jdMapper.toResponseDTO(jd);
    }

    @Override
    public List<JDResponseDTO> getAllJDs() {

        return jdRepository.findAll()
                .stream()
                .map(jdMapper::toResponseDTO)
                .collect(Collectors.toList());
    }


    @Override
    public List<JDResponseDTO> getActiveJDs() {

        return jdRepository.findByStatus(JDStatus.ACTIVE)
                .stream()
                .map(jdMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

   
    @Override
    @Transactional
    public JDResponseDTO updateJD(UUID id, JDRequestDTO requestDTO) {

        validateRequest(requestDTO);

        JobDescription jd = findJDOrThrow(id);

        jd.setJobTitle(requestDTO.getJobTitle());
        jd.setJobDescription(requestDTO.getJobDescription());
        jd.setSkillsRequired(requestDTO.getSkillsRequired());
        jd.setExperience(requestDTO.getExperience());
        jd.setSalary(requestDTO.getSalary());
        jd.setLocation(requestDTO.getLocation());
        jd.setJobType(requestDTO.getJobType());

        JobDescription updated = jdRepository.save(jd);

        return jdMapper.toResponseDTO(updated);
    }


    @Override
    @Transactional
    public JDResponseDTO updateJDStatus(UUID id, JDStatus status) {

        JobDescription jd = findJDOrThrow(id);
        jd.setStatus(status);

        return jdMapper.toResponseDTO(jdRepository.save(jd));
    }


    @Override
    @Transactional
    public void deleteJD(UUID id) {

        JobDescription jd = findJDOrThrow(id);
        jdRepository.delete(jd);

        log.info("JD deleted: {}", id);
    }


    @Override
    public List<JDResponseDTO> searchJDs(JDStatus status,
                                         JobType jobType,
                                         String location,
                                         String title) {

        return jdRepository.searchJDs(status, jobType, location, title)
                .stream()
                .map(jdMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

 
    private JobDescription findJDOrThrow(UUID id) {
        return jdRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("JD not found: " + id));
    }

    private void validateRequest(JDRequestDTO dto) {

        if (dto.getExperience() < 0) {
            throw new InvalidRequestException("Experience cannot be negative");
        }

        if (dto.getSalary() == null ||
                dto.getSalary().doubleValue() < 0) {
            throw new InvalidRequestException("Salary cannot be negative");
        }
    }
}