package com.moddy.server.controller.model.dto.response;

import com.moddy.server.domain.prefer_hair_style.PreferHairStyle;

import java.util.List;

public record StyleDetailResponse(
        Boolean isAgree,
        List<String> preferStyle,
        String designerOfferDetail,
        String modelApplicationDetail,
        List<Boolean> preferOfferConditions
) {
}