package com.interview_tracking_system.backend.repository;

import com.interview_tracking_system.backend.entity.Feedback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Feedback entity.
 */
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    Logger LOGGER = LoggerFactory.getLogger(FeedbackRepository.class);

    /**
     * Fetch feedback submitted for a specific interview.
     *
     * @param interviewId interview id
     * @return list of feedback entries
     */
    List<Feedback> findByInterviewId(Long interviewId);

    /**
     * Fetch feedback submitted by a specific panel for one interview.
     *
     * @param interviewId interview id
     * @param panelId     panel id
     * @return feedback if already submitted
     */
    Optional<Feedback> findByInterviewIdAndPanelId(Long interviewId, Long panelId);

    /**
     * Fetch feedback submitted by a panel member.
     *
     * @param panelId panel id
     * @return list of feedback entries
     */
    List<Feedback> findByPanelId(Long panelId);

    /**
     * count by interview id
     * 
     * @param interviewId
     * @return
     */

    long countByInterviewId(Long interviewId);
}
