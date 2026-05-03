package com.interview_tracking_system.backend.mapper;

import com.interview_tracking_system.backend.dto.JDRequestDTO;
import com.interview_tracking_system.backend.dto.JDResponseDTO;
import com.interview_tracking_system.backend.entity.JobDescription;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between JobDescription entity and DTOs.
 */
@Component
public final class JDMapper {

    /**
     * Converts request DTO to entity.
     *
     * @param dto request DTO
     * @return JobDescription entity
     */
    public JobDescription toEntity(final JDRequestDTO dto) {

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
     * Converts entity to response DTO.
     *
     * @param jd JobDescription entity
     * @return response DTO
     */
    public JDResponseDTO toResponseDTO(final JobDescription jd) {

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
     * Returns a null-safe copy of list.
     *
     * @param list input list
     * @return safe list
     */
    private List<String> safeList(final List<String> list) {
        return (list == null) ? new ArrayList<>() : new ArrayList<>(list);
    }
}
