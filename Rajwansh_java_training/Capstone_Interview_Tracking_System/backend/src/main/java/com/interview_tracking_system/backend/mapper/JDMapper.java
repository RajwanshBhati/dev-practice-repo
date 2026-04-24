package com.interview_tracking_system.backend.mapper;

import com.interview_tracking_system.backend.dto.JDRequestDTO;
import com.interview_tracking_system.backend.dto.JDResponseDTO;
import com.interview_tracking_system.backend.entity.JobDescription;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JDMapper {

    /**
     * Convert Request DTO → Entity
     */
    public JobDescription toEntity(JDRequestDTO dto) {

        if (dto == null) {
            return null;
        }

        JobDescription jd = new JobDescription();

        jd.setJobTitle(dto.getJobTitle());
        jd.setJobDescription(dto.getJobDescription());
        jd.setSkillsRequired(safeList(dto.getSkillsRequired()));
        jd.setExperienceMin(dto.getMinExperience());
        jd.setExperienceMax(dto.getMaxExperience());
        jd.setSalaryMin(dto.getMinSalary());
        jd.setSalaryMax(dto.getMaxSalary());
        jd.setLocation(dto.getLocation());
        jd.setJobType(dto.getJobType());

        return jd;
    }

    /**
     * Convert Entity → Response DTO
     */
    public JDResponseDTO toResponseDTO(JobDescription jd) {

        if (jd == null) {
            return null;
        }

        JDResponseDTO dto = new JDResponseDTO();

        dto.setId(jd.getId());
        dto.setJobTitle(jd.getJobTitle());
        dto.setJobDescription(jd.getJobDescription());
        dto.setSkillsRequired(safeList(jd.getSkillsRequired()));
        dto.setMinExperience(jd.getExperienceMin());
        dto.setMaxExperience(jd.getExperienceMax());
        dto.setMinSalary(jd.getSalaryMin());
        dto.setMaxSalary(jd.getSalaryMax());
        dto.setLocation(jd.getLocation());
        dto.setJobType(jd.getJobType());
        dto.setStatus(jd.getStatus());
        dto.setCreatedAt(jd.getCreatedAt());
        dto.setUpdatedAt(jd.getUpdatedAt());

        return dto;
    }

    /**
     * Null-safe list handling
     */
    private List<String> safeList(List<String> list) {
        return (list == null) ? new ArrayList<>() : new ArrayList<>(list);
    }
}
