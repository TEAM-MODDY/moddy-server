package com.moddy.server.controller.model.dto.response;

import com.moddy.server.domain.prefer_offer_condition.OfferCondition;
import java.util.List;

public record OfferResponse(
        Long offerId,
        String imgUrl,
        String name,
        String shopName,
        List<OfferCondition> conditions,
        boolean isClicked
) {
}
