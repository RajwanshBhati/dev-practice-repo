package com.interviewtracker.interview_process_tracking.repository;

import com.interviewtracker.interview_process_tracking.entity.JobDescription;
import com.interviewtracker.interview_process_tracking.enum.JDStatus;
import com.interviewtracker.interview_process_tracking.enum.JobType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JobDescriptionRepository extends JpaRepository<JobDescription, UUID> {

    List<JobDescription> findByStatus(JDStatus status);

    List<JobDescription> findByJobType(JobType jobType);

    List<JobDescription> findByLocationContainingIgnoreCase(String location);
}