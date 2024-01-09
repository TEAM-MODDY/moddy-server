package com.moddy.server.domain.prefer_offer_condition.repository;

import com.moddy.server.domain.prefer_offer_condition.PreferOfferCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreferOfferConditionJpaRepository extends JpaRepository<PreferOfferCondition, Long> {

    List<PreferOfferCondition> findTop2ByHairServiceOfferId(Long offerId);

    List<PreferOfferCondition> findAllByHairServiceOfferId(Long offerId);
}
