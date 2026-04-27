package com.interview_tracking_system.backend.repository;

import com.interview_tracking_system.backend.entity.Panel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PanelRepository extends JpaRepository<Panel, Long> {

    /**
     * Looks up a panel member by their one-time activation token.
     */
    Optional<Panel> findByActivationToken(String token);

    /**
     * Looks up a panel member by their registered email address.
     */
    Optional<Panel> findByEmail(String email);
}
