package com.interview_tracking_system.backend.repository;

import com.interview_tracking_system.backend.entity.Interview;
import com.interview_tracking_system.backend.enums.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for Interview entity.
 */
public interface InterviewRepository extends JpaRepository<Interview, Long> {

    Logger LOGGER = LoggerFactory.getLogger(InterviewRepository.class);

    /**
     * Fetch all interviews for a candidate.
     *
     * @param candidateId candidate id
     * @return list of interviews
     */
    List<Interview> findByCandidateId(Long candidateId);

    /**
     * Fetch interviews by stage (L1 / L2).
     *
     * @param stage stage
     * @return list of interviews
     */
    List<Interview> findByStage(Stage stage);
}
