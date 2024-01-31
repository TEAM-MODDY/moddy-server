package com.moddy.server.domain.prefer_offer_condition;

import com.moddy.server.domain.BaseTimeEntity;
import com.moddy.server.domain.hair_service_offer.HairServiceOffer;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PreferOfferCondition extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hair_service_offer_id")
    @NotNull
    private HairServiceOffer hairServiceOffer;

    @Enumerated(value=EnumType.STRING)
    @NotNull
    private OfferCondition offerCondition;

    public PreferOfferCondition(HairServiceOffer hairServiceOffer, OfferCondition offerCondition){
        this.hairServiceOffer = hairServiceOffer;
        this.offerCondition = offerCondition;
    }
}
