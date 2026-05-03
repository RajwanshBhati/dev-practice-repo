package com.interview_tracking_system.backend.repository;

import com.interview_tracking_system.backend.entity.JobDescription;
import com.interview_tracking_system.backend.enums.JDStatus;
import com.interview_tracking_system.backend.enums.JobType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository for JobDescription entity.
 */
@Repository
public interface JobDescriptionRepository extends JpaRepository<JobDescription, UUID> {

        /**
         * Finds job descriptions by status.
         *
         * @param status the JD status to filter by
         * @return list of matching job descriptions
         */
        List<JobDescription> findByStatus(JDStatus status);

        /**
         * Finds job descriptions by job type.
         *
         * @param jobType the job type to filter by
         * @return list of matching job descriptions
         */
        List<JobDescription> findByJobType(JobType jobType);

        /**
         * Finds job descriptions by location.
         *
         * @param location the location to filter by
         * @return list of matching job descriptions
         */
        List<JobDescription> findByLocation(String location);

        /**
         * Finds all job descriptions ordered by creation date descending.
         *
         * @return list of all job descriptions newest first
         */
        List<JobDescription> findAllByOrderByCreatedAtDesc();

        /**
         * Finds job descriptions by status ordered by creation date descending.
         *
         * @param status the JD status to filter by
         * @return list of matching job descriptions newest first
         */
        List<JobDescription> findByStatusOrderByCreatedAtDesc(JDStatus status);

        /**
         * Custom search method to filter Job Descriptions based on multiple criteria.
         *
         * @param status   the status to filter by
         * @param jobType  the job type to filter by
         * @param location the location to filter by
         * @param title    the job title to filter by
         * @return list of matching job descriptions
         */
        @Query("""
                        SELECT jd FROM JobDescription jd
                        WHERE (:status IS NULL OR jd.status = :status)
                        AND (:jobType IS NULL OR jd.jobType = :jobType)
                        AND (CAST(:location AS string) IS NULL
                             OR LOWER(jd.location) LIKE LOWER(CONCAT('%', CAST(:location AS string), '%')))
                        AND (CAST(:title AS string) IS NULL
                             OR LOWER(jd.jobTitle) LIKE LOWER(CONCAT('%', CAST(:title AS string), '%')))
                        """)
        List<JobDescription> searchJDs(
                        @Param("status") JDStatus status,
                        @Param("jobType") JobType jobType,
                        @Param("location") String location,
                        @Param("title") String title);
}
