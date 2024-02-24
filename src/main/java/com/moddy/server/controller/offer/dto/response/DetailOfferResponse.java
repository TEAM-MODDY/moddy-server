package com.moddy.server.controller.offer.dto.response;

import com.moddy.server.controller.application.dto.response.ApplicationInfoDetailResponse;
import com.moddy.server.controller.model.dto.response.DesignerInfoResponse;

import com.moddy.server.controller.offer.dto.response.OfferInfoResponse;

public record DetailOfferResponse(
        DesignerInfoResponse designerInfo,
        ApplicationInfoDetailResponse applicationInfo,
        OfferInfoResponse offerInfo
) {
}
