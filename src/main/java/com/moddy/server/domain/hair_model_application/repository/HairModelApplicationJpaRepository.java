package com.moddy.server.domain.hair_model_application.repository;

import com.moddy.server.domain.hair_model_application.HairModelApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface HairModelApplicationJpaRepository extends JpaRepository<HairModelApplication, Long> {
    Boolean existsByModelId(Long modelId);

    Optional<HairModelApplication> findFirstByModelIdOrderByCreatedAtDesc(Long modelId);

    Page<HairModelApplication> findAll(Pageable pageable);

    List<HairModelApplication> findAllByModelId(Long modelId);

    @Query("SELECT COUNT(*) FROM HairModelApplication h WHERE h.createdAt >= :startDate AND h.createdAt < :endDate")
    long countNonExpiredApplications(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

}
