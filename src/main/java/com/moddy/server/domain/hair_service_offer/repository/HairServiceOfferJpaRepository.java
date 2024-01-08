package com.moddy.server.domain.hair_service_offer.repository;

import com.moddy.server.domain.hair_service_offer.HairServiceOffer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HairServiceOfferJpaRepository extends JpaRepository<HairServiceOffer, Long> {

    Page<HairServiceOffer> findByUserId(Long userId, Pageable pageable);

    Boolean existsByUserId(Long userId);

    List<HairServiceOffer> findAllByUserId(Long userId);
}
