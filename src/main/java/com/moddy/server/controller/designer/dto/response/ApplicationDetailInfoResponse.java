package com.moddy.server.controller.designer.dto.response;

public record ApplicationDetailInfoResponse(
        ApplicationInfoResponse applicationInfo,
        ModelInfoResponse modelInfo
) {
}
