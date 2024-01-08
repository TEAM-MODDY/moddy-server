package com.moddy.server.domain.har_service_offer.repository;

import com.moddy.server.domain.har_service_offer.HairServiceOffer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HairServiceOfferRepository extends JpaRepository<HairServiceOffer, Long> {

    Page<HairServiceOffer> findAllByOrderByHairServiceOfferIdDesc(Pageable pageable);

    Boolean existsByUser(Long userId);

}
