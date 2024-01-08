package com.moddy.server.controller.model.dto.response;

import java.util.List;

public record OfferResponse(
        int offerId,
        String imgUrl,
        String name,
        String shopName,
        List conditions,
        boolean isClicked
) {
}
