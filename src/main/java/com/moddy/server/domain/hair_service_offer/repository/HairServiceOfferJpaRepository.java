package com.moddy.server.domain.hair_service_offer.repository;

import com.moddy.server.domain.hair_service_offer.HairServiceOffer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HairServiceOfferJpaRepository extends JpaRepository<HairServiceOffer, Long> {
    Page<HairServiceOffer> findByUserId(Long userId, Pageable pageable);

    Optional<HairServiceOffer> findByHairModelApplicationIdAndUserId(Long applicationId, Long userId);

    Boolean existsByUserId(Long userId);

    List<HairServiceOffer> findAllByUserId(Long userId);

    List<HairServiceOffer> findAllByDesignerId(Long userId);
}
