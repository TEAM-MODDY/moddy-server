package com.moddy.server.controller.model.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record StyleDetailResponse(
        Boolean isAgree,
        List<String> preferStyle,
        String designerOfferDetail,
        String modelApplicationDetail,
        List<Boolean> preferOfferConditions
) {
}