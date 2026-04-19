package com.interviewtracker.interview_process_tracking.dto;

import com.interviewtracker.interview_process_tracking.enums.JDStatus;
import com.interviewtracker.interview_process_tracking.enums.JobType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JDResponseDTO {

    private UUID id;
    private String jobTitle;
    private String jobDescription;
    private List<String> skillsRequired;
    private Integer experience;
    private BigDecimal salary;
    private String location;
    private JobType jobType;
    private JDStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}