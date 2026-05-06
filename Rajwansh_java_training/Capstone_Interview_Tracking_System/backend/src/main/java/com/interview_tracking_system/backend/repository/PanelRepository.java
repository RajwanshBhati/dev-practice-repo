package com.interview_tracking_system.backend.repository;

import com.interview_tracking_system.backend.entity.Panel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repository for Panel entity.
 */
public interface PanelRepository extends JpaRepository<Panel, Long> {

    /**
     * Looks up a panel member by their one-time activation token.
     *
     * @param token the activation token to search by
     * @return an Optional containing the panel if found
     */
    Optional<Panel> findByActivationToken(String token);

    /**
     * Looks up a panel member by their registered email address.
     *
     * @param email the email address to search by
     * @return an Optional containing the panel if found
     */
    Optional<Panel> findByEmail(String email);
}
