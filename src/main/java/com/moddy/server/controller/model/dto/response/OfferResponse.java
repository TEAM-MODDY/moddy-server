package com.moddy.server.controller.model.dto.response;

import com.moddy.server.domain.hair_service_offer.OfferStatus;

import java.util.List;

public record OfferResponse(
        Long offerId,
        String imgUrl,
        String name,
        String shopName,
        List<String> conditions,
        OfferStatus status
) {
}
