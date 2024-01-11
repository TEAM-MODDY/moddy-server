package com.moddy.server.controller.designer.dto.response;

import com.moddy.server.domain.hair_model_application.HairLength;
import com.moddy.server.domain.prefer_hair_style.HairStyle;

import java.util.List;

public record ApplicationInfoResponse(
        Long applicationId,
        String modelImgUrl,
        HairLength hairLength,
        List<String> preferHairstyles,
        List<HairRecordResponse> hairServiceRecords,
        String hairDetail,
        Boolean isSend
) {
}
