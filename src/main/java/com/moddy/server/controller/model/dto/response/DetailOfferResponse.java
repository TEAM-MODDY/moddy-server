package com.moddy.server.controller.model.dto.response;

import java.util.List;

public record DetailOfferResponse(
        List<DesignerInfoResponse> designerInfo,
        List<StyleDetailResponse> styleDetail
) {
}
