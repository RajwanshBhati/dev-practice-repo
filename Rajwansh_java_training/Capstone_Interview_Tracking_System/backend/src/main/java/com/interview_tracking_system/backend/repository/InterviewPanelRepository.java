package com.interview_tracking_system.backend.repository;

import com.interview_tracking_system.backend.entity.InterviewPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for InterviewPanel mapping entity.
 */
public interface InterviewPanelRepository extends JpaRepository<InterviewPanel, Long> {

    Logger LOGGER = LoggerFactory.getLogger(InterviewPanelRepository.class);

    /**
     * Fetch all panel mappings for a given interview.
     *
     * @param interviewId interview id
     * @return list of interview-panel mappings
     */
    List<InterviewPanel> findByInterviewId(Long interviewId);

    /**
     * Fetch all interview mappings assigned to a panel.
     *
     * @param panelId panel id
     * @return list of interview-panel mappings
     */
    List<InterviewPanel> findByPanelId(Long panelId);
}
