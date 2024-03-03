package com.moddy.server.controller.designer.dto.response;

import java.util.List;

public record ApplicationInfoResponse(
        Long applicationId,
        String modelImgUrl,
        String hairLength,
        List<String> preferHairstyles,
        List<HairRecordResponse> hairServiceRecords,
        String hairDetail,
        boolean isSend,
        String instagramId,
        String createdDate,
        String expiredDate
) {
}
