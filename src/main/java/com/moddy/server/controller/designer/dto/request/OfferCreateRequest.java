package com.moddy.server.controller.designer.dto.request;

import com.moddy.server.domain.prefer_offer_condition.OfferCondition;

import java.util.List;

public record OfferCreateRequest(
        String offerDetail,
        List<OfferCondition> preferOfferConditions
) {
}
