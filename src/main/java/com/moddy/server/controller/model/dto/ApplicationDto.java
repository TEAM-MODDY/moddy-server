package com.moddy.server.controller.model.dto;

import com.moddy.server.controller.designer.dto.response.HairRecordResponse;

import java.util.List;

public record ApplicationDto(
        Long modelId,
        String modelImgUrl,
        String hairLength,
        List<String> preferHairStyleList,
        List<HairRecordResponse> recordResponseList,
        String hairDetail,
        String instgramId
) {
}
