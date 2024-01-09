package com.moddy.server.controller.model.dto.response;

import com.moddy.server.domain.prefer_hair_style.HairStyle;

import java.util.List;

public record StyleDetailResponse(
        Boolean isAgree,
        List<HairStyle> preferStyle,
        String designerOfferDetail,
        String modelApplicationDetail,
        List<Boolean> preferOfferConditions
) {
}