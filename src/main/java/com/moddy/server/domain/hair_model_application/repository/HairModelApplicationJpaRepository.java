package com.moddy.server.domain.hair_model_application.repository;

import com.moddy.server.domain.hair_model_application.HairModelApplication;
import com.moddy.server.domain.hair_service_offer.HairServiceOffer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HairModelApplicationJpaRepository extends JpaRepository<HairModelApplication, Long> {
    Boolean existsByUserId(Long userId);
    Optional<HairModelApplication> findByUserId(Long userId);
    List<HairModelApplication> findAllByUserId(Long userId);
    Page<HairModelApplication> findByUserId(Long userId, Pageable pageable);
}
