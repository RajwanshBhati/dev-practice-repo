package com.interviewtracker.interview_process_tracking.mapper;

import com.interviewtracker.interview_process_tracking.dto.JDRequestDTO;
import com.interviewtracker.interview_process_tracking.dto.JDResponseDTO;
import com.interviewtracker.interview_process_tracking.entity.JobDescription;
import org.springframework.stereotype.Component;

@Component
public class JDMapper {

    public JobDescription toEntity(JDRequestDTO dto) {
        return JobDescription.builder()
                .jobTitle(dto.getJobTitle())
                .jobDescription(dto.getJobDescription())
                .skillsRequired(dto.getSkillsRequired())
                .experience(dto.getExperience())
                .salary(dto.getSalary())
                .location(dto.getLocation())
                .jobType(dto.getJobType())
                .build();
    }

    public JDResponseDTO toResponseDTO(JobDescription jd) {
        return JDResponseDTO.builder()
                .id(jd.getId())
                .jobTitle(jd.getJobTitle())
                .jobDescription(jd.getJobDescription())
                .skillsRequired(jd.getSkillsRequired())
                .experience(jd.getExperience())
                .salary(jd.getSalary())
                .location(jd.getLocation())
                .jobType(jd.getJobType())
                .status(jd.getStatus())
                .createdAt(jd.getCreatedAt())
                .updatedAt(jd.getUpdatedAt())
                .build();
    }
}