package com.moddy.server.controller.offer.response;

import com.moddy.server.controller.model.dto.response.DesignerInfoResponse;
import com.moddy.server.controller.model.dto.response.StyleDetailResponse;

public record DetailOfferResponse(
        DesignerInfoResponse designerInfo,
        StyleDetailResponse styleDetail
) {
}
