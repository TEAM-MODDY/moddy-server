package com.moddy.server.controller.application.dto.response;

import java.util.List;

public record ApplicationInfoDetailResponse(
        Long applicationId,
        List<String> preferStyle,
        String modelApplicationDetail
) {
}
