package com.moddy.server.controller.offer.dto.response;

import lombok.Builder;
import java.util.List;

@Builder
public record OfferInfoResponse(
        Long offerId,
        Boolean isAgree,
        String designerOfferDetail,
        List<Boolean> preferOfferConditions
) {
}