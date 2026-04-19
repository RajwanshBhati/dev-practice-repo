package com.interviewtracker.interview_process_tracking.repository;

import com.interviewtracker.interview_process_tracking.entity.JobDescription;
import com.interviewtracker.interview_process_tracking.enums.JDStatus;
import com.interviewtracker.interview_process_tracking.enums.JobType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JobDescriptionRepository extends JpaRepository<JobDescription, UUID> {

    List<JobDescription> findByStatus(JDStatus status);

    List<JobDescription> findByJobType(JobType jobType);

    List<JobDescription> findByLocation(String location);

    @Query("SELECT jd FROM JobDescription jd WHERE " +
           "(:status IS NULL OR jd.status = :status) AND " +
           "(:jobType IS NULL OR jd.jobType = :jobType) AND " +
           "(:location IS NULL OR LOWER(jd.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
           "(:title IS NULL OR LOWER(jd.jobTitle) LIKE LOWER(CONCAT('%', :title, '%')))")
    List<JobDescription> searchJDs(
            @Param("status") JDStatus status,
            @Param("jobType") JobType jobType,
            @Param("location") String location,
            @Param("title") String title
    );
}