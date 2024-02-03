package com.moddy.server.service.offer;

import com.moddy.server.domain.hair_service_offer.HairServiceOffer;
import com.moddy.server.domain.hair_service_offer.repository.HairServiceOfferJpaRepository;
import com.moddy.server.domain.prefer_offer_condition.repository.PreferOfferConditionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HairServiceOfferRegisterService {
    private final PreferOfferConditionJpaRepository preferOfferConditionJpaRepository;
    private final HairServiceOfferJpaRepository hairServiceOfferJpaRepository;

    public void deleteModelHairServiceOfferInfos(final Long modelId) {
        final List<HairServiceOffer> hairServiceOffers = hairServiceOfferJpaRepository.findAllByModelId(modelId);
        hairServiceOffers.forEach(hairServiceOffer -> {
            preferOfferConditionJpaRepository.deleteAllByHairServiceOffer(hairServiceOffer);
            hairServiceOfferJpaRepository.deleteById(hairServiceOffer.getId());
        });
    }

    public void deleteDesignerHairServiceOfferInfos(final Long designerId) {
        final List<HairServiceOffer> hairServiceOffers = hairServiceOfferJpaRepository.findAllByDesignerId(designerId);
        hairServiceOffers.forEach(hairServiceOffer -> {
            preferOfferConditionJpaRepository.deleteAllByHairServiceOffer(hairServiceOffer);
            hairServiceOfferJpaRepository.deleteById(hairServiceOffer.getId());
        });
    }
}
