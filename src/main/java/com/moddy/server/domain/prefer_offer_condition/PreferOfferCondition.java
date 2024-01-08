package com.moddy.server.domain.prefer_offer_condition;

import com.moddy.server.domain.BaseTimeEntity;
import com.moddy.server.domain.har_service_offer.HairServiceOffer;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class PreferOfferCondition extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hair_service_offer_id_")
    @NotNull
    private HairServiceOffer hairServiceOffer;

    @Enumerated(value=EnumType.STRING)
    @NotNull
    private OfferCondition offerCondition;

}
