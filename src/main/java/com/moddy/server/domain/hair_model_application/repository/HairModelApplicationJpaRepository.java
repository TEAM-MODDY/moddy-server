package com.moddy.server.domain.hair_model_application.repository;

import com.moddy.server.domain.hair_model_application.HairModelApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface HairModelApplicationJpaRepository extends JpaRepository<HairModelApplication, Long> {
    Boolean existsByUserId(Long userId);
    Optional<HairModelApplication> findByUserId(Long userId);
}
