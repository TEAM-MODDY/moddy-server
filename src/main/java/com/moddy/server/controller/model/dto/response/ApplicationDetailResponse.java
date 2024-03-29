package com.moddy.server.controller.model.dto.response;

import com.moddy.server.domain.hair_model_application.HairLength;
import com.moddy.server.domain.prefer_hair_style.HairStyle;

import java.util.List;

public record ApplicationDetailResponse(
        Long applicationId,
        String modelImgUrl,
        HairLength hairLength,
        List<HairStyle> preferHairStyles

) {
}
